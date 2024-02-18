package edu.ucsd.cse110.successorator.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import edu.ucsd.cse110.successorator.lib.domain.Goal;

@Entity(tableName = "goals")

public class GoalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "taskText")
    public String taskText;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    GoalEntity(@NonNull String taskText, boolean completed, int sortOrder) {
        this.taskText = taskText;
        this.completed = completed;
        this.sortOrder = sortOrder;
    }

    public static GoalEntity fromGoal(@NonNull Goal goal) {
        var card = new GoalEntity(goal.taskText(), goal.completed(), goal.sortOrder());
        card.id = goal.id();
        return card;
    }

    public @NonNull Goal toGoal() {
        return new Goal(id, taskText, completed, sortOrder);
    }
}