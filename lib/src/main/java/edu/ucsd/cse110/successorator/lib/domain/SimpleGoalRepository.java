package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class SimpleGoalRepository implements GoalRepository {
    private final InMemoryDataSource dataSource;

    public SimpleGoalRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Integer count() {
        return dataSource.getGoals().size();
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
    public void save(List<Goal> goals) {
        dataSource.addGoals(goals);
    }

    @Override
    public void addOneTimeGoal(Goal goal) {
        dataSource.addGoal(goal.withSortOrder(dataSource.getMaxSortOrder() + 1));
    }

    @Override
    public void addRecurringGoal(RecurringGoal goal) {

    }

    @Override
    public void removeRecurringGoal(int id) {

    }

    @Override
    public void removeOneTimeGoal(int id) {
        dataSource.removeGoal(id);
    }

    @Override
    public void updateOneTimeGoal(Goal goal) {
        dataSource.updateGoal(goal);
    }

}
