package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel extends ViewModel {
    private final GoalRepository goalRepository;

    private final MutableSubject<List<Goal>> orderedGoals;
    private final MutableSubject<String> goalDescription;

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(app.getGoalRepository());
            });

    public MainViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;

        this.orderedGoals = new SimpleSubject<>();
        this.goalDescription = new SimpleSubject<>();

        // When the list of goals changes (or is first loaded), reset the ordering.
        goalRepository.findAll().observe(goals -> {
            if (goals == null) return; // not ready yet, ignore

            var newOrderedGoals = goals.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());

            orderedGoals.setValue(newOrderedGoals);
        });

//        // When the ordering changes, update the current goal.
//        orderedGoals.observe(goals -> {
//            if (goals == null || goals.isEmpty()) return;
//            var goal = goals.get(0);
//            goalDescription.setValue(goal.taskText()); // Assuming Goal has a getDescription method.
//        });
    }

    public Subject<String> getGoalDescription() {
        return goalDescription;
    }

    public Subject<List<Goal>> getOrderedGoals() {
        return orderedGoals;
    }

    public void completeGoal() {
        var goals = this.orderedGoals.getValue();
        if (goals == null || goals.isEmpty()) return;

        // Complete the current goal and update the list.
        var completedGoal = goals.remove(0);
        goalRepository.remove(completedGoal.sortOrder());

        // Optionally, save the updated list back to the repository.
        orderedGoals.setValue(goals);
    }

    public void append(Goal goal) {
        goalRepository.append(goal);
    }

    public void remove(int id) {
        goalRepository.remove(id);
    }

    public void updateGoal(Goal goal) {
        goalRepository.updateGoal(goal);
    }
}