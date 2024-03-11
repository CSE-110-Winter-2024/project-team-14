package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class TomorrowGoal extends Goal {
    public TomorrowGoal(@Nullable Integer id, @NonNull String taskText, boolean completed, int sortOrder, @NonNull String context) {
        super(id, taskText, completed, sortOrder, context);
    }
}
