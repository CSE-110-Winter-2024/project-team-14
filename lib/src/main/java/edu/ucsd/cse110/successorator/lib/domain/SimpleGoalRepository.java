package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

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
    public Subject<Goal> findTomorrow(int id) {
        return null;
    }

    @Override
    public Subject<List<Goal>> findAll() {
        return dataSource.getAllGoalsSubject();
    }

    @Override
    public Subject<List<Goal>> findTomorrowAll() {
        return null;
    }

    @Override
    public Subject<List<Goal>> findByContext(@NonNull String context) {
        return dataSource.getAllGoalsSubject();
    }

    @Override
    public Subject<List<Goal>> findTomorrowByContext(@NonNull String context) {
        return null;
    }

    @Override
    public void save(List<Goal> goals) {
        dataSource.addGoals(goals);
    }

    @Override
    public void saveTomorrowGoal(List<TomorrowGoal> goals) {

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

    @Override
    public void appendTomorrowGoal(TomorrowGoal goal) {

    }

    @Override
    public void removeTomorrowGoal(Integer id) {
        dataSource.removeGoal(id);
    }

}
