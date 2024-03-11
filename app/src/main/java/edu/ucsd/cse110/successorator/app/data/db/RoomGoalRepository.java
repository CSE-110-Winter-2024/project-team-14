package edu.ucsd.cse110.successorator.app.data.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.Transformations;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.domain.TomorrowGoal;

public class RoomGoalRepository implements GoalRepository {
    private final GoalDao goalDao;
    private final TomorrowGoalDao tomorrowGoalDao;

    public RoomGoalRepository(GoalDao goalDao, TomorrowGoalDao tomorrowGoalDao) {
        this.goalDao = goalDao;
        this.tomorrowGoalDao = tomorrowGoalDao;
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
    public Subject<Goal> findTomorrow(int id) {
        var entityLiveData = tomorrowGoalDao.findAsLiveData(id);
        var goalLiveData = Transformations.map(entityLiveData, TomorrowGoalEntity::toTomorrowGoal);
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
    public Subject<List<Goal>> findTomorrowAll() {
        var entitiesLiveData = tomorrowGoalDao.findAllAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TomorrowGoalEntity::toTomorrowGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }


    @Override
    public Subject<List<Goal>> findByContext(@NonNull String context) {
        var entitiesLiveData = goalDao.findByContextAsLiveData(context);
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }

    @Override
    public Subject<List<Goal>> findTomorrowByContext(@NonNull String context) {
        var entitiesLiveData = tomorrowGoalDao.findByContextAsLiveData(context);
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(TomorrowGoalEntity::toTomorrowGoal)
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
    public void saveTomorrowGoal(List<TomorrowGoal> goals) {
        var entities = goals.stream()
                .map(TomorrowGoalEntity::fromTomorrowGoal)
                .collect(Collectors.toList());
        tomorrowGoalDao.insert(entities);
    }

    @Override
    public void append(Goal goal) {
        goalDao.append(GoalEntity.fromGoal(goal));
    }

    @Override
    public void appendTomorrowGoal(TomorrowGoal goal) {
        tomorrowGoalDao.appendTomorrowGoal(TomorrowGoalEntity.fromTomorrowGoal(goal));

    }

    @Override
    public void remove(int id) {
        goalDao.delete(id);
    }

    @Override
    public void removeTomorrowGoal(Integer id) {
        tomorrowGoalDao.delete(id);
    }

    @Override
    public void updateGoal(Goal goal) { goalDao.updateGoal(GoalEntity.fromGoal(goal)); }




}
