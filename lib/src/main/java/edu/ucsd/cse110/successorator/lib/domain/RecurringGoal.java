package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public abstract class RecurringGoal extends Goal{
    protected LocalDateTime nextDate;
    protected LocalDateTime startDate;
    protected String type;

    public RecurringGoal(@Nullable Integer id, @NonNull String taskText, boolean completed,
                         int sortOrder, LocalDateTime startDate, String type) {
        super(id, taskText, completed, sortOrder);
        this.startDate = startDate;
        this.nextDate = setNextDate();
        this.type = type;
    }

    public abstract LocalDateTime setNextDate();

    public abstract String GetType ();


    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getNextDate() {
        return this.nextDate;
    }

}
