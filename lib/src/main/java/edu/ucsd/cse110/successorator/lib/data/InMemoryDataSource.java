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
    private int nextId = 5;
    private int minSortOrder = Integer.MAX_VALUE;
    private int maxSortOrder = Integer.MIN_VALUE;
    private final Map<Integer, Goal> goals = new HashMap<>();
    private final Map<Integer, SimpleSubject<Goal>> goalSubjects = new HashMap<>();
    private final SimpleSubject<List<Goal>> allGoalsSubject = new SimpleSubject<>();

    public InMemoryDataSource() {

    }

    public final static List<Goal> DEFAULT_GOALS = List.of(
            new Goal(0, "Wash dishes", false, 0),
            new Goal(1, "Do laundry", false, 1),
            new Goal(2, "Cook lunch", false, 2),
            new Goal(3, "Cook lunch1", false, 3),
            new Goal(4, "Cook lunch2", false, 4)
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Goal goal : DEFAULT_GOALS) {
            data.addGoal(goal);
        }
        return data;
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

    public int getMinSortOrder() {
        return minSortOrder;
    }

    public int getMaxSortOrder() {
        return maxSortOrder;
    }

//    public void putGoal(Goal goal) {
//        goals.put(goal.id(), goal); // Assume Goal class has getId() method.
//        goalSubjects.computeIfAbsent(goal.id(), k -> new SimpleSubject<>()).setValue(goal);
//        allGoalsSubject.setValue(getGoals());
//    }

    public void updateGoal(Goal goal) {
        if (this.goals.containsKey(goal.id())) {
            System.out.println(goal.completed() + "before");
            Goal updatedGoal = goal.toggleCompleted();
            System.out.println(updatedGoal.completed() + "after");

            goal.withSortOrder(getMaxSortOrder() + 1);
            this.goals.put(goal.id(), updatedGoal);

            if(goalSubjects.containsKey(goal.id())) {
                goalSubjects.get(goal.id()).setValue(updatedGoal);
            }
        }


        allGoalsSubject.setValue(getGoals());
    }

    public void addGoal(Goal goal) {
        var fixedGoal = preInsert(goal);

        this.goals.put(fixedGoal.id(), fixedGoal);
        postInsert();
        assertSortOrderConstraints();

        if(goalSubjects.containsKey(fixedGoal.id())) {
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

        fixedGoals.forEach(goal -> {
            if (goalSubjects.containsKey(goal.id())) {
                goalSubjects.get(goal.id()).setValue(goal);
            }
        });
        allGoalsSubject.setValue(getGoals());
    }

    public void removeGoal(int id) {
        var goal = this.goals.get(id);
        var sortOrder = goal.sortOrder();

        this.goals.remove(id);
        shiftSortOrders(sortOrder, maxSortOrder, -1);

        if (goalSubjects.containsKey(id)) {
            goalSubjects.get(id).setValue(null);
        }
        allGoalsSubject.setValue(getGoals());
    }

    public void shiftSortOrders(int from, int to, int by) {
        var goals = this.goals.values().stream()
                .filter(goal -> goal.sortOrder() >= from && goal.sortOrder() <= to)
                .map(goal -> goal.withSortOrder(goal.sortOrder() + by))
                .collect(Collectors.toList());
        addGoals(goals);
    }



    // setup method to make sure new goal has a unique id before adding to "DB"
    private Goal preInsert(Goal goal) {
        var id = goal.id();
        if (id == null) {
            goal = goal.withId(nextId++);
        }
        else if (id > nextId)  {
            nextId = id + 1;
        }

        return goal;
    }

    // clean up method to make sure min and max sort orders are up to date after adding a goal
    private void postInsert() {
        minSortOrder = goals.values().stream()
                .map(Goal::sortOrder)
                .min(Integer::compareTo)
                .orElse(Integer.MAX_VALUE);

        maxSortOrder = goals.values().stream()
                .map(Goal::sortOrder)
                .max(Integer::compareTo)
                .orElse(Integer.MIN_VALUE);
    }

    private void assertSortOrderConstraints() {
        // get all of the sort orders
        var sortOrders = goals.values().stream()
                .map(Goal::sortOrder)
                .collect(Collectors.toList());

        // non-negative
        assert sortOrders.stream().allMatch(i -> i >= 0);

        // unique
        assert sortOrders.size() == sortOrders.stream().distinct().count();

        // between min and max
        assert sortOrders.stream().allMatch(i -> i >= minSortOrder);
        assert sortOrders.stream().allMatch(i -> i <= maxSortOrder);
    }
}
