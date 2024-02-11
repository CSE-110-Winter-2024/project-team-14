package edu.ucsd.cse110.successorator.app;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class MainViewModel {
    private static final String LOG_TAG = "MainViewModel";

    // domain state (true "Model" state)
    private final GoalRepository goalRepository;
//
//    // UI state
//    private final Subject<List<Integer>> cardOrdering;
    private final MutableSubject<List<Goal>> orderedCards;

    public MainViewModel(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
        this.orderedCards = new SimpleSubject<>();

        goalRepository.findAll().observe(cards -> {
            if (cards == null) return;

            var newOrderedCards = cards.stream()
                    .sorted(Comparator.comparingInt(Goal::sortOrder))
                    .collect(Collectors.toList());
            orderedCards.setValue(newOrderedCards);
        });
    }

    // methods for functionality go here and sparks subject/observer chain
    // once these methods are sparked by the mainActivity onclicklisteners,
    // they set off the above observable pathways in the mainviewmodel constructor
    public Subject<List<Goal>> getOrderedCards() {
        return orderedCards;
    }
}
