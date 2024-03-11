package edu.ucsd.cse110.successorator.app.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface TomorrowGoalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TomorrowGoalEntity flashcard);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<TomorrowGoalEntity> flashcards);

    @Query("SELECT * FROM goals WHERE id = :id")
    TomorrowGoalEntity find(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    List<TomorrowGoalEntity> findAll();

    @Query("SELECT * FROM goals WHERE id = :id")
    LiveData<TomorrowGoalEntity> findAsLiveData(int id);

    @Query("SELECT * FROM goals ORDER BY sort_order")
    LiveData<List<TomorrowGoalEntity>> findAllAsLiveData();

    @Query("SELECT * FROM goals WHERE context = :context ORDER BY sort_order")
    LiveData<List<TomorrowGoalEntity>> findByContextAsLiveData(String context);

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
    default int appendTomorrowGoal(TomorrowGoalEntity goal) {
        var maxSortOrder = getMaxSortOrderForUncompletedGoals();
        var newGoal = new TomorrowGoalEntity(goal.taskText, goal.completed, maxSortOrder + 1, goal.context);
        return Math.toIntExact(insert(newGoal));
    }

    @Query("DELETE FROM goals WHERE id = :id")
    void delete(int id);

}
