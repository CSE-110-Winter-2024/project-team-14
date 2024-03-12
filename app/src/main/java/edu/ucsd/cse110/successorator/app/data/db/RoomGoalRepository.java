package edu.ucsd.cse110.successorator.app.data.db;
import androidx.lifecycle.Transformations;
import java.util.List;
import java.util.stream.Collectors;
import edu.ucsd.cse110.successorator.app.util.LiveDataSubjectAdapter;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.util.Subject;
public class RoomGoalRepository implements GoalRepository {
    private final GoalDao goalDao;
    public RoomGoalRepository(GoalDao goalDao) {
        this.goalDao = goalDao;
    }
    @Override
    public Integer countTodayGoals() {
        return goalDao.countTodayGoals();
    }
    @Override
    public Integer countRecurringGoals() {
        return goalDao.countRecurringGoals();
    }
    @Override
    public Integer countTomorrowGoals() {
        return goalDao.countTomorrowGoals();
    }
    @Override
    public Subject<List<Goal>> findAllTodayGoals() {
        var entitiesLiveData = goalDao.findAllTodayGoalsAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }
    @Override
    public Subject<List<Goal>> findAllTomorrowGoals() {
        var entitiesLiveData = goalDao.findAllTomorrowGoalsAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
    }
    @Override
    public Subject<List<Goal>> findAllRecurringGoals() {
        var entitiesLiveData = goalDao.findAllRecurringGoalsAsLiveData();
        var goalsLiveData = Transformations.map(entitiesLiveData, entities -> {
            return entities.stream()
                    .map(GoalEntity::toGoal)
                    .collect(Collectors.toList());
        });
        return new LiveDataSubjectAdapter<>(goalsLiveData);
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
    public void append(Goal goal) {
        goalDao.append(GoalEntity.fromGoal(goal));
    }
    @Override
    public void remove(int id) {
        goalDao.delete(id);
    }
    @Override
    public void updateGoal(Goal goal) { goalDao.updateGoal(GoalEntity.fromGoal(goal)); }
    //The following two methods might be unnecessary
    @Override
    public Integer countAllGoals() { return goalDao.countAll(); }
    @Override
    public Subject<Goal> find(int id) {
        var entityLiveData = goalDao.findAsLiveData(id);
        var goalLiveData = Transformations.map(entityLiveData, GoalEntity::toGoal);
        return new LiveDataSubjectAdapter<>(goalLiveData);
    }
}