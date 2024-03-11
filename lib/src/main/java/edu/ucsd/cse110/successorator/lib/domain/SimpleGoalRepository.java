package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {
    private final InMemoryDataSource dataSource;

    public SimpleGoalRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }


    public Integer count() {
        return dataSource.getGoals().size();
    }

    @Override
    public Integer countAllGoals() {
        return null;
    }

    @Override
    public Integer countOneTimeGoals() {
        return null;
    }

    @Override
    public Integer countRecurringGoals() {
        return null;
    }

    @Override
    public Subject<Goal> find(int id) {
        return dataSource.getGoalSubject(id);
    }

    @Override
    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalsSubject();
    }

    @Override
    public Subject<List<Goal>> findAllOneTimeGoals() {
        return null;
    }

    @Override
    public Subject<List<Goal>> findAllRecurringGoals() {
        return null;
    }

    @Override
    public void save(List<Goal> goals) {
        dataSource.addGoals(goals);
    }

    @Override
    public void append(Goal goal) {
        dataSource.addGoal(goal.withSortOrder(dataSource.getMaxSortOrder() + 1));
    }

    @Override
    public void remove(int id) {
        dataSource.removeGoal(id);
    }

    @Override
    public void updateGoal(Goal goal) {
        dataSource.updateGoal(goal);
    }

}
