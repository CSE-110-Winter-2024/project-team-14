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
    private final Map<Integer, Goal> goals = new HashMap<>();
    private final Map<Integer, SimpleSubject<Goal>> goalSubjects = new HashMap<>();
    private final SimpleSubject<List<Goal>> allGoalsSubject = new SimpleSubject<>();

    public InMemoryDataSource() {

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
        goals.put(goal.id(), goal); // Assume Goal class has getId() method.
        goalSubjects.computeIfAbsent(goal.id(), k -> new SimpleSubject<>()).setValue(goal);
        allGoalsSubject.setValue(getGoals());
    }

    public void addGoal(Goal goal) {
        // Add a new goal to the map and update all goals subject.
        goals.put(goal.id(), goal);
        allGoalsSubject.setValue(getGoals());
    }

    public void removeGoal(int id) {
        // Remove the goal by ID and update all goals subject.
        if (goals.containsKey(id)) {
            goals.remove(id);
            goalSubjects.remove(id); // Also remove the subject for this goal if it exists.
            allGoalsSubject.setValue(getGoals());
        }
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
}
