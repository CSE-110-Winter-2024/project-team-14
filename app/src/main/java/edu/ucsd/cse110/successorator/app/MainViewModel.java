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
import edu.ucsd.cse110.successorator.lib.domain.TomorrowGoal;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    private final GoalRepository goalRepository;

    private final MutableSubject<LocalDateTime> currentDateTime;
    private final MutableSubject<List<Goal>> orderedTodayGoals;
    private final MutableSubject<List<Goal>> orderedTomorrowGoals;
//    private final MutableSubject<List<Goal>> orderedPendingGoals;
//
//    private final MutableSubject<List<Goal>> orderedRecurringGoals;
    private final TimeKeeper timeKeeper;

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
        this.orderedTodayGoals = new SimpleSubject<>();
//        this.orderedRecurringGoals = new SimpleSubject<>();
//        this.orderedPendingGoals = new SimpleSubject<>();
        this.orderedTomorrowGoals = new SimpleSubject<>();


        // When the list of goals changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedTodayGoals = goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            orderedTodayGoals.setValue(newOrderedTodayGoals);
        });

        //find all tmrow goals
        goalRepository.findTomorrowAll().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedTomorrowGoals = goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            orderedTomorrowGoals.setValue(newOrderedTomorrowGoals);
        });

        currentDateTime.observe(dateTime -> {
            if(dateTime != null && timeKeeper.getMarkedDateTime().getValue() != null) {
                var twoAMNextDay = (timeKeeper.getMarkedDateTime().getValue())
                        .plusDays(1).withHour(2).withMinute(0).withSecond(0);
                if (dateTime.isAfter(twoAMNextDay)) {
                    rollover();
                    //add/pull tomorrow tasks from tmrowDAO, and delete
                }
            }
            // THEN mark the new past time.
            timeKeeper.markDateTime(dateTime);
        });
    }

    public Subject<List<Goal>> getOrderedTodayGoals() {
        return orderedTodayGoals;
    }

    public Subject<List<Goal>> getOrderedTomorrowGoals() {
        return orderedTomorrowGoals;
    }


    public void remove(int id) {
        goalRepository.remove(id);
    }

    public void append(Goal goal){
        goalRepository.append(goal);
    }

    public void appendTomorrow(TomorrowGoal goal){
        goalRepository.appendTomorrowGoal(goal);
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
        for (var g : orderedTodayGoals.getValue()) {
            if (g.completed()) {
                goalRepository.remove(g.id());
            }
        }
    }

//    private void pullTomorrowTasks() {
//        for (var tg : )
//    }

    public void filterByContext(String context) {
        goalRepository.findByContext(context).observe(goals -> {
            if (goals == null) return;

            var newOrderedGoals = goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            orderedTodayGoals.setValue(newOrderedGoals);
        });
    }


    public void cancelFilter() {
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return;

            var newOrderedGoals = goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            orderedTodayGoals.setValue(newOrderedGoals);
        });
    }

    public void deleteTomorrowGoal(TomorrowGoal goal) {
        goalRepository.removeTomorrowGoal(goal.id());
    }
}

