package edu.ucsd.cse110.successorator.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface GoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(GoalEntity goal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<GoalEntity> goals);

    @Query("SELECT * FROM goals WHERE id = :id")
    GoalEntity find(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    List<GoalEntity> findAll();

    @Query("SELECT * FROM goals WHERE type_of_goal = 'one_time' ORDER BY sort_order")
    List<GoalEntity> findAllOneTime();

    @Query("SELECT * FROM goals WHERE type_of_goal != 'one_time' ORDER BY sort_order")
    List<GoalEntity> findAllRecurringGoals();

    @Query("SELECT * FROM goals WHERE type_of_goal = 'weekly' ORDER BY sort_order")
    List<GoalEntity> findAllWeekly();

    @Query("SELECT * FROM goals WHERE type_of_goal = 'monthly' ORDER BY sort_order")
    List<GoalEntity> findAllMonthly();

    @Query("SELECT * FROM goals WHERE type_of_goal = 'yearly' ORDER BY sort_order")
    List<GoalEntity> findAllYearly();

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<GoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    LiveData<List<GoalEntity>> findAllAsLiveData();

    @Query("SELECT * FROM goals ORDER BY sort_order AND type_of_goal = 'one_time'")
    LiveData<List<GoalEntity>> findAllOneTimeGoalsAsLiveData();

    @Query("SELECT * FROM goals ORDER BY sort_order AND type_of_goal != 'one_time'")
    LiveData<List<GoalEntity>> findAllRecurringGoalsAsLiveData();

    @Query("SELECT * FROM goals ORDER BY sort_order AND type_of_goal = 'weekly'")
    LiveData<List<GoalEntity>> findAllWeeklyGoalsAsLiveData();

    @Query("SELECT * FROM goals ORDER BY sort_order AND type_of_goal = 'monthly'")
    LiveData<List<GoalEntity>> findAllMonthlyGoalsAsLiveData();

    @Query("SELECT * FROM goals ORDER BY sort_order AND type_of_goal = 'yearly'")
    LiveData<List<GoalEntity>> findAllYearlyGoalsAsLiveData();

    @Query("SELECT COUNT(*) FROM goals")
    int countAll();

    @Query("SELECT COUNT(*) FROM goals WHERE type_of_goal = 'one_time'")
    int countOneTimeGoals();

    @Query("SELECT COUNT(*) FROM goals  WHERE type_of_goal != 'one_time'")
    int countRecurringGoals();

    @Query("SELECT MIN(sort_order) FROM goals  WHERE type_of_goal = 'one_time'")
    int getMinSortOrderOneTime();

    @Query("SELECT MAX(sort_order) FROM goals  WHERE type_of_goal = 'one_time'")
    int getMaxSortOrderOneTime();

    @Query("UPDATE goals SET sort_order = sort_order + :by " + "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    @Query("SELECT MAX(sort_order) FROM goals WHERE completed = false AND type_of_goal = 'one_time'")
    int getMaxSortOrderForUncompletedOneTimeGoals();

    @Transaction
    default int append(GoalEntity goal) {
        var maxSortOrder = getMaxSortOrderForUncompletedOneTimeGoals();
        var newGoal = new GoalEntity(goal.id, goal.taskText, goal.completed, maxSortOrder + 1,
                goal.typeOfGoal, goal.nextDate);
        return Math.toIntExact(insert(newGoal));
    }

    @Query("DELETE FROM goals WHERE id = :id")
    void delete(int id);

    @Transaction
    default int updateGoal(GoalEntity goal) {
        int newGoalId;
        if (!goal.completed) {
            // append
            var maxSortOrder = getMaxSortOrderOneTime();
            var newGoal = new GoalEntity(goal.id, goal.taskText, !goal.completed, maxSortOrder + 1000, goal.typeOfGoal, goal.nextDate);
            newGoalId = Math.toIntExact(insert(newGoal));
        }
        else {
            // prepend
            shiftSortOrders(getMinSortOrderOneTime(), getMaxSortOrderOneTime(), 1);
            var newFlashcard = new GoalEntity(goal.id, goal.taskText, !goal.completed, getMinSortOrderOneTime() - 1000, goal.typeOfGoal, goal.nextDate);
            newGoalId = Math.toIntExact(insert(newFlashcard));
        }

        delete(goal.id);
        return newGoalId;
    }
}
