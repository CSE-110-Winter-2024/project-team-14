package edu.ucsd.cse110.successorator.app.data.db;

import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.RecurringGoal;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class RoomGoalRepository implements GoalRepository {
    private final GoalDao goalDao;
    private final RecurringGoalDao recurringGoalDao;

    public RoomGoalRepository(GoalDao goalDao, RecurringGoalDao recurringGoalDao) {
        this.goalDao = goalDao;
        this.recurringGoalDao = recurringGoalDao;
    }

    @Override
    public Integer count() { return goalDao.count(); }

    @Override
    public Subject<Goal> find(int id) {
        var entityLiveData = goalDao.findAsLiveData(id);
        var goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }

    @Override
    public Subject<List<Goal>> findAll() {
        var entitiesLiveData = goalDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public void save(List<Goal> goals) {
        var entities = goals.stream()
                .map(GoalEntity::fromGoal)
                .collect(Collectors.toList());
        goalDao.insert(entities);
    }

    @Override
    public void addOneTimeGoal(Goal goal) {
        goalDao.append(GoalEntity.fromGoal(goal));
    }

    @Override
    public void removeOneTimeGoal(int id) {
        goalDao.delete(id);
    }

    @Override
    public void updateOneTimeGoal(Goal goal) { goalDao.updateGoal(GoalEntity.fromGoal(goal)); }

    @Override
    public void addRecurringGoal(RecurringGoal goal) {
        recurringGoalDao.addRecurringGoal(RecurringGoalEntity.fromRecurringGoal(goal));
    }

    @Override
    public void removeRecurringGoal(int id) {
        recurringGoalDao.delete(id);
    }
}
