package edu.ucsd.cse110.successorator.lib.data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.MutableSubject;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
public class InMemoryDataSource {
    private final Map<Integer, Goal> goals = new HashMap<>();
    private final Map<Integer, MutableSubject<Goal>> goalSubjects = new HashMap<>();
    private final MutableSubject<List<Goal>> allGoalsSubject = new SimpleSubject<>();
    public InMemoryDataSource() {
    }

    public List<Goal> getGoals() {
        return List.copyOf(goals.values());
    }

    public Goal getGoal(int id) {
        return goals.get(id);
    }

    public Subject<Goal> getGoalSubject(int id) {
        if (!goalSubjects.containsKey(id)) {
            var subject = new SimpleSubject<Goal>();
            subject.setValue(getGoal(id));
            goalSubjects.put(id, subject);
        }
        return goalSubjects.get(id);
    }

    public Subject<List<Goal>> getAllGoalsSubject() {
        return allGoalsSubject;
    }

    //this gets updated in lab5
    public void putGoal(Goal goal) {
//        var fixedCard = preInsert(card);
//        goals.put(fixedCard.id(), fixedCard);
//        postInsert();
//        assertSortOrderConstraints();
//
//        if (goalSubjects.containsKey(fixedCard.id())) {
//            goalSubjects.get(fixedCard.id()).setValue(fixedCard);
//        }
//        allGoalsSubject.setValue(getGoals());
        goals.put(goal.id(), goal);
        if (goalSubjects.containsKey(goal.id())) {
            goalSubjects.get(goal.id()).setValue(goal);
        }
        allGoalsSubject.setValue(getGoals());
    }

    public final static List<Goal> DEFAULT_CARDS = List.of(
            new Goal(0,"Wash dishes",  false, 0),
            new Goal(1,"Do laundry",  false, 1),
            new Goal(2,"Take out trash",  false, 2)
    );

    //this gets updated in lab5
    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Goal goal : DEFAULT_CARDS) {
            data.putGoal(goal);
        }
        return data;
    }

    //implemented commented code from Lab5 later
//
//
//
//
//
//    public int getMinSortOrder() {
//        return minSortOrder;
//    }
//
//    public int getMaxSortOrder() {
//        return maxSortOrder;
//    }
//
//
//
//    public void putGoals(List<Goal> cards) {
//        var fixedCards = cards.stream()
//                .map(this::preInsert)
//                .collect(Collectors.toList());
//
//        fixedCards.forEach(card -> goals.put(card.id(), card));
//        postInsert();
//        assertSortOrderConstraints();
//
//        fixedCards.forEach(card -> {
//            if (goalSubjects.containsKey(card.id())) {
//                goalSubjects.get(card.id()).setValue(card);
//            }
//        });
//        allGoalsSubject.setValue(getGoals());
//    }
}