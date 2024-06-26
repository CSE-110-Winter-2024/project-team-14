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
    Long insert(GoalEntity flashcard);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<GoalEntity> flashcards);

    @Query("SELECT * FROM goals WHERE id = :id")
    GoalEntity find(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    List<GoalEntity> findAll();

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<GoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    LiveData<List<GoalEntity>> findAllAsLiveData();

    @Query("SELECT * FROM goals WHERE context = :context ORDER BY sort_order")
    LiveData<List<GoalEntity>> findByContextAsLiveData(String context);

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
    default int append(GoalEntity goal) {
        var maxSortOrder = getMaxSortOrderForUncompletedGoals();
        var newGoal = new GoalEntity(goal.taskText, goal.completed, maxSortOrder + 1, goal.context,
                goal.dateAdded, goal.recurrence, goal.isPending);
        return Math.toIntExact(insert(newGoal));
    }

    @Query("DELETE FROM goals WHERE id = :id")
    void delete(int id);

    @Transaction
    default int updateGoal(GoalEntity goal) {
        int newGoalId;
        if (goal.completed == false) {
            // append
            var maxSortOrder = getMaxSortOrder();
            var newGoal = new GoalEntity(goal.taskText, !goal.completed, maxSortOrder + 1000, goal.context,
                    goal.dateAdded, goal.recurrence, goal.isPending);
            newGoalId = Math.toIntExact(insert(newGoal));
        }
        else {
            // prepend
            shiftSortOrders(getMinSortOrder(), getMaxSortOrder(), 1);
            var newFlashcard = new GoalEntity(goal.taskText, !goal.completed, getMinSortOrder() - 1000, goal.context,
                    goal.dateAdded, goal.recurrence, goal.isPending);
            newGoalId = Math.toIntExact(insert(newFlashcard));
        }

        delete(goal.id);
        return newGoalId;
    }

    @Query("UPDATE goals SET dateAdded = :date WHERE id = :id")
    void setDateDB(int id, LocalDateTime date);

    @Query("UPDATE goals SET isPending = false WHERE id = :id")
    void switchPendingDB(int id);

    @Transaction
    default void setDate(GoalEntity goal, LocalDateTime date) {
        setDateDB(goal.id, date);
    }

    @Transaction
    default void switchPending(GoalEntity goal) {
        switchPendingDB(goal.id);
    }
}
