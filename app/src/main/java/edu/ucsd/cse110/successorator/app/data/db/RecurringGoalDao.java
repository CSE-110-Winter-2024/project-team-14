package edu.ucsd.cse110.successorator.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface RecurringGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(RecurringGoalEntity goal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<RecurringGoalEntity> goals);

    @Query("SELECT * FROM goals WHERE id = :id")
    RecurringGoalEntity find(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    List<RecurringGoalEntity> findAll();

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<RecurringGoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    LiveData<List<RecurringGoalEntity>> findAllAsLiveData();

    @Query("SELECT COUNT(*) FROM goals")
    int count();

    @Query("SELECT MIN(sort_order) FROM goals")
    int getMinSortOrder();

    @Query("SELECT MAX(sort_order) FROM goals")
    int getMaxSortOrder();

    @Query("UPDATE goals SET sort_order = sort_order + :by " + "WHERE sort_order >= :from AND sort_order <= :to")
    void shiftSortOrders(int from, int to, int by);

    @Query("SELECT MAX(sort_order) FROM goals WHERE completed = false")
    int getMaxSortOrderForUncompletedGoals();

    @Transaction
    default int addRecurringGoal(RecurringGoalEntity goal) {
        var maxSortOrder = getMaxSortOrderForUncompletedGoals();
        var newRecurringGoal = new RecurringGoalEntity(goal.taskText,
                goal.completed, maxSortOrder + 1, goal.startDate, goal.nextDate, goal.type);
        return Math.toIntExact(insert(newRecurringGoal));
    }

    @Query("DELETE FROM goals WHERE id = :id")
    void delete(int id);

//    @Transaction
//    default int updateGoal(GoalEntity goal) {
//        int newGoalId;
//        if (goal.completed == false) {
//            // append
//            var maxSortOrder = getMaxSortOrder();
//            var newGoal = new GoalEntity(goal.taskText, !goal.completed, maxSortOrder + 1000);
//            newGoalId = Math.toIntExact(insert(newGoal));
//        }
//        else {
//            // prepend
//            shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
//            var newFlashcard = new GoalEntity(goal.taskText, !goal.completed, getMinSortOrder() - 1000);
//            newGoalId = Math.toIntExact(insert(newFlashcard));
//        }
//
//        delete(goal.id);
//        return newGoalId;
//    }
}
