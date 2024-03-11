package edu.ucsd.cse110.successorator.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.TomorrowGoal;

@Entity(tableName = "Tomorrow Goals")

public class TomorrowGoalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "taskText")
    public String taskText;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "context")
    public String context;

    TomorrowGoalEntity(@NonNull String taskText, boolean completed, int sortOrder, @NonNull String context) {
        this.taskText = taskText;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.context = context;
    }

    public static TomorrowGoalEntity fromTomorrowGoal(@NonNull TomorrowGoal goal) {
        var tomorrowGoal = new TomorrowGoalEntity(goal.taskText(), goal.completed(), goal.sortOrder(), goal.context());
        tomorrowGoal.id = goal.id();
        return tomorrowGoal;
    }

    public @NonNull Goal toTomorrowGoal() {
        return new Goal(id, taskText, completed, sortOrder, context);
    }
}
