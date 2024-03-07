package edu.ucsd.cse110.successorator.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.RecurringGoal;

@Entity(tableName = "goals")

public class RecurringGoalEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public Integer id = null;

    @ColumnInfo(name = "taskText")
    public String taskText;

    @ColumnInfo(name = "completed")
    public boolean completed;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "start_date")
    public LocalDateTime startDate;

    @ColumnInfo(name= "next_date")
    public LocalDateTime nextDate;


    RecurringGoalEntity(@NonNull String taskText, boolean completed,
                        int sortOrder, LocalDateTime startDate, LocalDateTime nextDate) {
        this.taskText = taskText;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.nextDate = nextDate;
    }

    public static RecurringGoalEntity fromRecurringGoal(@NonNull RecurringGoal goal) {
        var recurringGoal = new RecurringGoalEntity(goal.taskText(), goal.completed(),
                goal.sortOrder(), goal.getStartDate(), goal.getNextDate());
        recurringGoal.id = goal.id();
        return recurringGoal;
    }

//    public @NonNull RecurringGoal toRecurringGoal() {
//        return new weeklyGoal(id, taskText, completed, sortOrder, startDate);
//    }
}

