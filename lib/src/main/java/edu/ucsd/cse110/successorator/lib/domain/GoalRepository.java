package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;
import java.util.Optional;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class GoalRepository {
    private final InMemoryDataSource dataSource;

    public GoalRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer count() {
        return dataSource.getGoals().size();
    }

    public Subject<Goal> find(int id) {
        return dataSource.getGoalSubject(id);
    }

    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalsSubject();
    }

    public void save(List<Goal> goals) {
        dataSource.addGoals(goals);
    }

    public void append(Goal goal) {
        dataSource.addGoal(goal);
    }

    public void remove(int id) {
        dataSource.removeGoal(id);
    }

    public void updateGoal(Goal goal) {
        dataSource.updateGoal(goal);
    }

}