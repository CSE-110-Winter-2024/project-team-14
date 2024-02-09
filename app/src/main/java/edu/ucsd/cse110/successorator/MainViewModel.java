package edu.ucsd.cse110.successorator;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;

import edu.ucsd.cse110.successorator.lib.domain.ToDoList;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;


public class MainViewModel extends ViewModel {
    private final ToDoList goals;
    private final MutableSubject<List<Integer>> goalOrdering;
    private final MutableSubject<List<Goal>> orderedGoals;


    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getToDoList());
                    });

    public MainViewModel(ToDoList goals) {
        this.goals = goals;
        this.goalOrdering = new SimpleSubject<>();
        this.orderedGoals = new SimpleSubject<>();

        goals.findAll().observe(goalList -> {
           if (goalList == null) return;

           var ordering = new ArrayList<Integer>();
           for (int i = 0; i < goalList.size(); i++) {
               ordering.add(i);
           }

           goalOrdering.setValue(ordering);
        });

        goalOrdering.observe(ordering -> {
            if (ordering == null) return;

            var cards = new ArrayList<Goal>();
            for (var id : ordering) {
                var card = goals.find(id).getValue();
                if (card == null) return;
                cards.add(card);
            }
            this.orderedGoals.setValue(cards);
        });
    }

    public Subject<List<Goal>> getOrderedGoals() { return orderedGoals; }

}
