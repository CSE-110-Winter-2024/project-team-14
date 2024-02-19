package edu.ucsd.cse110.successorator.lib.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class InMemoryDataSource {

    private int nextId = 1;
    private int nextUncompletedSortOrder = 3;


    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;

    private final Map<Integer, Goal> goals = new HashMap<>();
    private final Map<Integer, SimpleSubject<Goal>> goalSubjects = new HashMap<>();
    private final SimpleSubject<List<Goal>> allGoalsSubject = new SimpleSubject<>();

    public InMemoryDataSource() {
        // Initialize `nextId` based on DEFAULT_GOALS to ensure uniqueness
//
        //fromDefault();
    }

    public final static List<Goal> DEFAULT_GOALS = List.of(

    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        data.addGoals(DEFAULT_GOALS);
        return data;
    }

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }

    public int getNextUncompletedSortOrder() {
        return nextUncompletedSortOrder;
    }

    public boolean hasCompletedGoals() {
        var completedGoals = getGoals().stream()
                .filter(Goal::completed)
                .collect(Collectors.toList());

        return completedGoals.size() > 0;
    }

    public List<Goal> getGoals() {
        return new ArrayList<>(goals.values());
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
        allGoalsSubject.setValue(getGoals()); // Ensure the latest list is always set
        return allGoalsSubject;
    }

    public void addGoal(Goal goal) {
        Goal fixedGoal;
        if (goal.id() == null) {
            fixedGoal = preInsert(goal);
            fixedGoal = fixedGoal.withSortOrder(getNextUncompletedSortOrder());
        } else {
            fixedGoal = preInsert(goal);
        }
        assert fixedGoal.id() != null;

        if (hasCompletedGoals()) {
            shiftSortOrders(getNextUncompletedSortOrder(), getMaxSortOrder(), 1);
        }

        this.goals.put(fixedGoal.id(), fixedGoal);
        postInsert();
        assertSortOrderConstraints();

        if (goalSubjects.containsKey(fixedGoal.id())) {
            goalSubjects.get(fixedGoal.id()).setValue(fixedGoal);
        }
        allGoalsSubject.setValue(getGoals());
    }

    public void addGoals(List<Goal> goals) {
        var fixedGoals = goals.stream()
                .map(this::preInsert)
                .collect(Collectors.toList());

        fixedGoals.forEach(goal -> this.goals.put(goal.id(), goal));
        postInsert();
        assertSortOrderConstraints();

        fixedGoals.forEach(card -> {
            if (goalSubjects.containsKey(card.id())) {
                goalSubjects.get(card.id()).setValue(card);
            }
        });
        allGoalsSubject.setValue(getGoals());
    }

    public void removeGoal(int id) {
        if (!goals.containsKey(id)) return; // No action if goal doesn't exist
        var card = this.goals.get(id);
        var sortOrder = card.sortOrder();

        goals.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (goalSubjects.containsKey(id)) {
            goalSubjects.get(id).setValue(null);
        }
        allGoalsSubject.setValue(getGoals());
    }

    public void updateGoal(Goal goal) {
        if (this.goals.containsKey(goal.id())) {
            Goal updatedGoal = goal.toggleCompleted();

            if (updatedGoal.completed()) {
                removeGoal(updatedGoal.id()); // remove and shift sort orders
                postInsert(); //update maxSortOrder
                Goal newSortedGoal = updatedGoal.withSortOrder(getMaxSortOrder() + 1);
                addGoal(newSortedGoal);
                nextUncompletedSortOrder--;
            } else {
                //shiftSortOrders(0, getMaxSortOrder(), 1); //make space at beginning for it
                removeGoal(updatedGoal.id());
                postInsert();
                Goal newSortedGoal = updatedGoal.withSortOrder(getMinSortOrder() - 1);
                addGoal(newSortedGoal);
                nextUncompletedSortOrder++;
            }

            if (goalSubjects.containsKey(goal.id())) {
                goalSubjects.get(updatedGoal.id()).setValue(updatedGoal);
            }
        }
        allGoalsSubject.setValue(getGoals());
    }

    public void shiftSortOrders(int from, int to, int by) {
        var goals = this.goals.values().stream()
                .filter(goal -> goal.sortOrder() >= from && goal.sortOrder() <= to)
                .map(goal -> goal.withSortOrder(goal.sortOrder() + by)) // Adjust sortOrder
                .collect(Collectors.toList());

        addGoals(goals); // Re-insert adjusted goals
    }

    private Goal preInsert(Goal goal) {
        var id = goal.id();
        if (id == null) {
            // If the card has no id, give it one.
            goal = goal.withId(nextId++);
        }
        else if (id > nextId) {
            // If the card has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load cards like in fromDefault().
            nextId = id + 1;
        }
        return goal;
    }

    private void postInsert() {
        // Keep the min and max sort orders up to date.
        minSortOrder = goals.values().stream()
                .map(Goal::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = goals.values().stream()
                .map(Goal::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);

        if (maxSortOrder >= 1000){
            maxSortOrder--;
        }
    }

        private void assertSortOrderConstraints() {
            // Get all the sort orders...
            var sortOrders = goals.values().stream()
                .map(Goal::sortOrder)
                .collect(Collectors.toList());

            // Non-negative...
            assert sortOrders.stream().allMatch(i -> i >= 0);

            // Unique...
            assert sortOrders.size() == sortOrders.stream().distinct().count();

            // Between min and max...
            assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
            assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
        }
}
