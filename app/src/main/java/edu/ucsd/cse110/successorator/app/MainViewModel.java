package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

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
    private final TimeKeeper timeKeeper;

    private final MutableSubject<LocalDateTime> currentDateTime;

    private final MutableSubject<List<Goal>> orderedGoals;

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(app.getGoalRepository(), new InMemoryTimeKeeper());
            });

    public MainViewModel(GoalRepository goalRepository, TimeKeeper timeKeeper) {
        this.goalRepository = goalRepository;
        this.timeKeeper = timeKeeper;

        this.currentDateTime = new SimpleSubject<>();
        this.currentDateTime.setValue(LocalDateTime.now());
        this.orderedGoals = new SimpleSubject<>();

        // When the list of goals changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedGoals = goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            orderedGoals.setValue(newOrderedGoals);
        });


        currentDateTime.observe(dateTime -> {
            // TODO: Do some comparison between dateTime and TimeKeeper's marked.
            if (false) {
                rollover();
            }

            // THEN mark the new past time.
            timeKeeper.markDateTime(dateTime);
        });
    }

    public MutableSubject<LocalDateTime> getCurrentDateTime() {
        return currentDateTime;
    }

    public Subject<List<Goal>> getOrderedGoals() {
        return orderedGoals;
    }

    public void setCurrentDateTime(LocalDateTime newDateTime) {
        currentDateTime.setValue(newDateTime);
    }

//    public void completeGoal() {
//        var goals = this.orderedGoals.getValue();
//        if (goals == null || goals.isEmpty()) return;
//
//        // Complete the current goal and update the list.
//        var completedGoal = goals.remove(0);
//        goalRepository.remove(completedGoal.sortOrder());
//
//        // Optionally, save the updated list back to the repository.
//        orderedGoals.setValue(goals);
//    }

    public void append(Goal goal) {
        goalRepository.append(goal);
    }

    public void remove(int id) {
        goalRepository.remove(id);
    }

    public void updateGoal(Goal goal) {
        goalRepository.updateGoal(goal);
    }

    private void rollover() {
        // TODO: do the rollover
    }
}