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

    private int nextId = 0;

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

    public void putGoal(Goal goal) {
        var fixedCard = preInsert(goal);

        assert fixedCard.id() != null;

        fixedCard = fixedCard.withSortOrder(fixedCard.id());

        goals.put(fixedCard.id(), fixedCard);
        postInsert();
        //assertSortOrderConstraints();

        if (goalSubjects.containsKey(fixedCard.id())) {
            goalSubjects.get(fixedCard.id()).setValue(fixedCard);
        }
        allGoalsSubject.setValue(getGoals());


    }

    public void putGoals(List<Goal> goals) {
        goals.forEach(goal -> this.goals.put(goal.id(), goal));
        goals.forEach(goal -> goalSubjects.computeIfAbsent(goal.id(), k -> new SimpleSubject<>()).setValue(goal));
        allGoalsSubject.setValue(getGoals()); // Update the list of all goals
    }

    public int getMinSortOrder() {
        return Integer.MAX_VALUE;
    }

    public int getMaxSortOrder() {
        return Integer.MIN_VALUE;
    }

    public void addGoal(Goal goal) {

        //var nextId  = goals.values().stream().mapToInt(Goal::id).max().orElse(0) + 1;

        // Assign a new ID if the goal's ID is null
        int newId = goal.id() == null ? nextId : goal.id();

        // Create a new goal with the assigned ID if necessary
        Goal newGoal = goal.id() == null ? goal.withId(newId) : goal;

        // Add the new goal to the map
        goals.put(newGoal.id(), newGoal);
        goalSubjects.computeIfAbsent(newGoal.id(), k -> new SimpleSubject<>()).setValue(newGoal);
        allGoalsSubject.setValue(getGoals()); // Notify observers of the change
        System.out.println(goal.taskText());
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
    }

    private Goal preInsert(Goal goal) {
        assert goal != null;
        var id = goal.id();
        if (id == null) {
            // If the card has no id, give it one.
            goal = goal.withId(nextId++ + 1);
        }
        else if (id > nextId) {
            // If the card has an id, update nextId if necessary to avoid giving out the same
            // one. This is important for when we pre-load cards like in fromDefault().
            nextId = id + 1;
        }

        return goal;
    }

    public void removeGoal(int id) {
        if (!goals.containsKey(id)) return; // No action if goal doesn't exist

        var goal = goals.get(id);
        var sortOrder = goal.sortOrder(); // Assuming sortOrder exists

        goals.remove(id);
        shiftSortOrders(sortOrder, getMaxSortOrder(), -1); // Adjust sort orders

        goalSubjects.remove(id); // Remove associated subject
        allGoalsSubject.setValue(getGoals()); // Update the list of all goals
    }

    public void shiftSortOrders(int from, int to, int by) {
        var adjustedGoals = goals.values().stream()
                .filter(goal -> goal.sortOrder() >= from && goal.sortOrder() <= to)
                .map(goal -> goal.withSortOrder(goal.sortOrder() + by)) // Adjust sortOrder
                .collect(Collectors.toList());

        putGoals(adjustedGoals); // Re-insert adjusted goals
    }

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Goal goal : DEFAULT_GOALS) {
            data.putGoal(goal);
        }
        return data;
    }

    public final static List<Goal> DEFAULT_GOALS = List.of(
            new Goal(0, "Wash dishes", false, 0),
            new Goal(1, "Do laundry", false, 1),
            new Goal(2, "Cook lunch", false, 2),
            new Goal(3, "Cook lunch1", false, 3),
            new Goal(4, "Cook lunch2", false, 4)
    );

//    private void assertSortOrderConstraints() {
//        // Get all the sort orders...
//        var sortOrders = goals.values().stream()
//                .map(Goal::sortOrder)
//                .collect(Collectors.toList());
//
//        // Non-negative...
//        assert sortOrders.stream().allMatch(i -> i >= 0);
//
//        // Unique...
//        assert sortOrders.size() == sortOrders.stream().distinct().count();
//
//        // Between min and max...
//        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
//        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
//    }
}
