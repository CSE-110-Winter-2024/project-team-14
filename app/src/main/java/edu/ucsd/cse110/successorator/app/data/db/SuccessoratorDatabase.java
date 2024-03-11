package edu.ucsd.cse110.successorator.app.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GoalEntity.class, TomorrowGoalEntity.class}, version = 1)
public abstract class SuccessoratorDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
    public abstract TomorrowGoalDao tomorrowGoalDao();
}
