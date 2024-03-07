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
    public Integer countOneTimeGoals() { return goalDao.count(); }

    @Override
    public Integer countRecurringGoals() { return recurringGoalDao.count(); }

    @Override
    public Subject<Goal> findOneTimeGoal(int id) {
        var entityLiveData = goalDao.findAsLiveData(id);
        var goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }

    @Override
    public Subject<RecurringGoal> findRecurringGoal(int id) {
        var entityLiveData = recurringGoalDao.findAsLiveData(id);
        var goalLiveData = Transformations.map(entityLiveData, RecurringGoalEntity::toRecurringGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }

    @Override
    public Subject<List<Goal>> findAllOneTimeGoals() {
        var entitiesLiveData = goalDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<RecurringGoal>> findAllRecurringGoals() {
        var entitiesLiveData = recurringGoalDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(RecurringGoalEntity::toRecurringGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public void saveOneTimeGoal(List<Goal> goals) {
        var entities = goals.stream()
                .map(GoalEntity::fromGoal)
                .collect(Collectors.toList());
        goalDao.insert(entities);
    }

    @Override
    public void saveRecurringGoal(List<RecurringGoal> goals) {
        var entities = goals.stream()
                .map(RecurringGoalEntity::fromRecurringGoal)
                .collect(Collectors.toList());
        recurringGoalDao.insert(entities);
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
