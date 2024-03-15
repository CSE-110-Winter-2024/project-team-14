package edu.ucsd.cse110.successorator.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @ColumnInfo(name = "context")
    public String context;

    @ColumnInfo(name = "dateAdded")
    public LocalDateTime dateAdded;

    @ColumnInfo(name = "recurrence")
    public String recurrence;

    @ColumnInfo(name = "isPending")
    public boolean isPending;

    GoalEntity(@NonNull String taskText, boolean completed, int sortOrder, @NonNull String context,
               LocalDateTime dateAdded, @NonNull String recurrence, boolean isPending) {
        this.taskText = taskText;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.context = context;
        this.dateAdded = dateAdded;
        this.recurrence = recurrence;
        this.isPending = isPending;
    }

    public static GoalEntity fromGoal(@NonNull Goal goal) {
        var card = new GoalEntity(goal.taskText(), goal.completed(), goal.sortOrder(), goal.context(),
                goal.dateAdded(), goal.getRecurrence(), goal.isPending());
        card.id = goal.id();
        return card;
    }

    public @NonNull Goal toGoal() {
        return new Goal(id, taskText, completed, sortOrder, context, dateAdded, recurrence, isPending);
    }
}
