package edu.ucsd.cse110.successorator.app;

import static androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

// current implementation is from lab3
public class MainViewModel extends ViewModel {
    private static final String LOG_TAG = "MainViewModel";

    // Domain state (true "Model" state)
    private final GoalRepository goalRepository;

    // UI state
    private final SimpleSubject<List<Integer>> cardOrdering;
    private final SimpleSubject<List<Goal>> orderedCards;
    private final MutableSubject<Goal> topCard;

    public static final ViewModelInitializer<MainViewModel> initializer =
            new ViewModelInitializer<>(
                    MainViewModel.class,
                    creationExtras -> {
                        var app = (SuccessoratorApplication) creationExtras.get(APPLICATION_KEY);
                        assert app != null;
                        return new MainViewModel(app.getGoalRepository());
                    }
            );
    public MainViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;

        // create the observable subjects
        this.cardOrdering = new SimpleSubject<>();
        this.orderedCards = new SimpleSubject<>();
        this.topCard = new SimpleSubject<>();

        // when the list of cards changes or is first loaded, reset the ordering
        goalRepository.findAll().observe(cards -> {
            if (cards == null) return; // not ready yet, ignore

            var ordering = new ArrayList<Integer>();
            for (int i = 0; i < cards.size(); i++) {
                ordering.add(i);
            }
            cardOrdering.setValue(ordering);
        });

        // when the ordering changes, update the ordered cards
        cardOrdering.observe(ordering -> {
            if (ordering == null) return;

            var cards = new ArrayList<Goal>();
            for (var id : ordering) {
                var card = goalRepository.find(id).getValue();
                if (card == null) return;
                cards.add(card);
            }
            this.orderedCards.setValue(cards);
        });

        orderedCards.observe(cards -> {
            if (cards == null || cards.size() == 0) return;
            var card = cards.get(0);
            this.topCard.setValue(card);
        });




//        public void stepForward() {
//            var ordering = this.cardOrdering.getValue();
//            if (ordering == null) return;
//
//            var newOrdering = new ArrayList<>(ordering);
//            Collections.rotate(newOrdering, -1);
//            this.cardOrdering.setValue(newOrdering);
//        }
//
//        public void stepBackward() {
//            var ordering = this.cardOrdering.getValue();
//            if (ordering == null) return;
//
//            var newOrdering = new ArrayList<>(ordering);
//            Collections.rotate(newOrdering, 1);
//            this.cardOrdering.setValue(newOrdering);
//        }
    }

    public Subject<List<Goal>> getOrderedCards() {
        return orderedCards;
    }

}
