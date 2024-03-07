package edu.ucsd.cse110.successorator.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.MonthlyGoal;
import edu.ucsd.cse110.successorator.lib.domain.RecurringGoal;
import edu.ucsd.cse110.successorator.lib.domain.WeeklyGoal;

@Entity(tableName = "Recurring Goals")

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

    @ColumnInfo(name="type")
    public String type;


    RecurringGoalEntity(@NonNull String taskText, boolean completed, int sortOrder,
                        LocalDateTime startDate, LocalDateTime nextDate, String type) {
        this.taskText = taskText;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.startDate = startDate;
        this.nextDate = nextDate;
        this.type = type;
    }

    public static RecurringGoalEntity fromRecurringGoal(@NonNull RecurringGoal goal) {
        var recurringGoal = new RecurringGoalEntity(goal.taskText(), goal.completed(),
                goal.sortOrder(), goal.getStartDate(), goal.getNextDate(), goal.GetType());
        recurringGoal.id = goal.id();
        return recurringGoal;
    }

    public RecurringGoal toRecurringGoal() {
        switch(type) {
            case "monthly":
                return new MonthlyGoal(id, taskText, completed, sortOrder, startDate);

            case "weekly" :
                return new WeeklyGoal(id, taskText, completed, sortOrder, startDate);

            case "yearly":
                 //return new YearlyGoal(id, taskText, completed, sortOrder, startDate);
                 break;

        }

        return null;
    }

    public Goal toOneTimeGoal() {
        return new Goal(id, taskText, completed, sortOrder);
    }
}

