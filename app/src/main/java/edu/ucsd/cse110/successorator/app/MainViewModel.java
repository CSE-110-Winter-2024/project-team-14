package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.InMemoryTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    private final GoalRepository goalRepository;

    private final MutableSubject<LocalDateTime> currentDateTime;
    private final MutableSubject<List<Goal>> orderedGoals;
    private List<Goal> originalGoals;
    private String currentFilterContext;
    private final TimeKeeper timeKeeper;
    public int buttonCount;

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(app.getGoalRepository(), new InMemoryTimeKeeper());
            });

    public MainViewModel(GoalRepository goalRepository, TimeKeeper timeKeeper) {
        this.timeKeeper = timeKeeper;
        this.goalRepository = goalRepository;
        this.currentDateTime = new SimpleSubject<>();
        this.currentDateTime.setValue(LocalDateTime.now());
        this.orderedGoals = new SimpleSubject<>();
        currentFilterContext = null;
        buttonCount = 0;

        // Retrieve all goals and store them as the original list
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return;

            originalGoals = goals;
            if (currentFilterContext != null) {
                filterByContext(currentFilterContext);
            } else {
                updateOrderedGoals(originalGoals);
            }
        });

        currentDateTime.observe(dateTime -> {
            if(dateTime != null && timeKeeper.getMarkedDateTime().getValue() != null) {
                var twoAMNextDay = (timeKeeper.getMarkedDateTime().getValue())
                        .plusDays(1).withHour(2).withMinute(0).withSecond(0);
                if (dateTime.isAfter(twoAMNextDay)) {
                    rollover();
                }
            }
            // THEN mark the new past time.
            timeKeeper.markDateTime(dateTime);
        });
    }

    public Subject<List<Goal>> getOrderedGoals() {
        return orderedGoals;
    }

    public void remove(int id) {
        goalRepository.remove(id);
    }

    public void append(Goal goal){
        goalRepository.append(goal);
    }

    public void updateGoal(Goal goal) {
        goalRepository.updateGoal(goal);
    }

    public void setDate(Goal goal, LocalDateTime date) {
        goalRepository.setDate(goal, date);
    }

    public void switchPending(Goal goal) {
        goalRepository.switchPending(goal);
    }

    public void setCurrentDateTime(LocalDateTime newDateTime) {
        currentDateTime.setValue(newDateTime);

    }
    public MutableSubject<LocalDateTime> getCurrentDateTime() {
        return currentDateTime;
    }
    public void rollover() {
        for (var g : orderedGoals.getValue()) {
            if (g.completed() && g.getRecurrence().equals("one_time")) {
                goalRepository.remove(g.id());
            }

            if(g.completed() && !g.getRecurrence().equals("one_time")){
                goalRepository.updateGoal(g);
            }
        }
    }

    public void cleanDUMMY() {
        for (var g : orderedGoals.getValue()) {
            if(g.taskText().equals("DUMMY")){
                goalRepository.remove(g.id());
            }
        }
    }

    private void updateOrderedGoals(List<Goal> goals) {
        var newOrderedGoals = goals.stream()
                .sorted(Comparator.comparingInt(Goal::sortOrder))
                .collect(Collectors.toList());

        orderedGoals.setValue(newOrderedGoals);
    }

    public void filterByContext(String context) {
        currentFilterContext = context;

        List<Goal> filteredGoals = originalGoals.stream()
                .filter(goal -> goal.context().equals(context))
                .collect(Collectors.toList());

        updateOrderedGoals(filteredGoals);
    }


    public void cancelFilter() {
        currentFilterContext = null;
        updateOrderedGoals(originalGoals);
    }

    public String getContext() {
        return this.currentFilterContext;
    }
}

