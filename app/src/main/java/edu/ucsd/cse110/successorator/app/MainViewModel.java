package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    private final TimeKeeper timeKeeper;
    private final MutableSubject<List<Goal>> recurringOrderedGoals;

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
        this.recurringOrderedGoals = new SimpleSubject<>();

        // When the list of goals changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedGoals = goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            orderedGoals.setValue(newOrderedGoals);
        });

        goalRepository.findAllRecurringGoals().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedGoals = goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            recurringOrderedGoals.setValue(newOrderedGoals);
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

    public Subject<List<Goal>> getRecurringOrderedGoals() {
        return recurringOrderedGoals;
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

    public void setCurrentDateTime(LocalDateTime newDateTime) {
        currentDateTime.setValue(newDateTime);
    }
    public MutableSubject<LocalDateTime> getCurrentDateTime() {
        return currentDateTime;
    }
    private void rollover() {
        for (var g : orderedGoals.getValue()) {
            if (g.completed()) {
                goalRepository.remove(g.id());
            }
        }
    }

}

