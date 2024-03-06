package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class WeeklyGoal extends RecurringGoal {

    public WeeklyGoal(@Nullable Integer id, @NonNull String taskText, boolean completed, int sortOrder, LocalDateTime startDate) {
        super(id, taskText, completed, sortOrder, startDate);
    }

    @Override
    public LocalDateTime setNextDate() {
        LocalDateTime nextDate;
        nextDate = this.startDate.plusDays(7);
        return nextDate;
    }
}
