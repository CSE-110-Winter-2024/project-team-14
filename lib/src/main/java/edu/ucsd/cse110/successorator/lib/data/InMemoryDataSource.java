package edu.ucsd.cse110.successorator.lib.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void putGoal(Goal goal) {
        goals.put(goal.id(), goal);
        if (goalSubjects.containsKey(goal.id())) {
            goalSubjects.get(goal.id()).setValue(goal);
        }
        allGoalsSubject.setValue(getGoals());
    }

    public final static List<Goal> DEFAULT_CARDS = List.of(
            new Goal(0, "Wash dishes", false, 0),
            new Goal(1, "Do laundry", false, 1),
            new Goal(2, "Cook lunch", false, 2)
    );

    public static InMemoryDataSource fromDefault() {
        var data = new InMemoryDataSource();
        for (Goal goal : DEFAULT_CARDS) {
            data.putGoal(goal);
        }
        return data;
    }
}
