package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public abstract class RecurringGoal extends Goal{
    protected LocalDateTime nextDate;
    protected LocalDateTime startDate;

    public RecurringGoal(@Nullable Integer id, @NonNull String taskText, boolean completed, int sortOrder, LocalDateTime startDate) {
        super(id, taskText, completed, sortOrder);
        this.startDate = startDate;
        this.nextDate = setNextDate();
    }

    public abstract LocalDateTime setNextDate();

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getNextDate() {
        return this.nextDate;
    }

}
