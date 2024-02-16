package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class Goal implements Serializable {
    private final @Nullable Integer id;

    private final @NonNull String taskText;
    private final boolean completed;

    private final @NonNull Integer sortOrder;


    public Goal(@Nullable Integer id, @NonNull String taskText, boolean completed, int sortOrder) {
        this.id = id;
        this.taskText = taskText;
        this.completed = completed;
        this.sortOrder = sortOrder;
    }

    public @Nullable Integer id() {
        return id;
    }
    public @Nullable String taskText() {
        return taskText;
    }
    public boolean completed() {
        return completed;
    }
    public int sortOrder() {
        return sortOrder;
    }

    public Goal withId(int id) {
        return new Goal(id, this.taskText, this.completed, this.sortOrder);
    }

    public Goal withSortOrder(int sortOrder) {
        return new Goal(this.id, this.taskText, this.completed, sortOrder);
    }

    public Goal toggleCompleted() {
        return new Goal(this.id, this.taskText, !this.completed, this.sortOrder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return completed == goal.completed && Objects.equals(id, goal.id) && Objects.equals(taskText, goal.taskText) && Objects.equals(sortOrder, goal.sortOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskText, completed, sortOrder);
    }

}