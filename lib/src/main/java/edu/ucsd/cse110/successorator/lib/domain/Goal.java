package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Goal implements Serializable {
    private final @Nullable Integer id;

    private final @NonNull String taskText;
    private final boolean completed;

    private final @NonNull Integer sortOrder;
    private final @NonNull String context;
    private final LocalDateTime dateAdded;
    private final boolean isPending;


    public Goal(@Nullable Integer id, @NonNull String taskText, boolean completed, int sortOrder, @NonNull String context,
                LocalDateTime dateAdded, boolean isPending) {
        this.id = id;
        this.taskText = taskText;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.context = context;
        this.dateAdded = dateAdded;
        this.isPending = isPending;
    }

    public @Nullable Integer id() {
        return id;
    }

    public @NonNull String taskText() {
        return taskText;
    }

    public boolean completed() {
        return completed;
    }

    public int sortOrder() {
        return sortOrder;
    }

    public @NonNull String context() { return context; }

    public LocalDateTime dateAdded() {return dateAdded;
    }
    public boolean isPending() { return isPending; }

    public Goal withId(int id) {
        return new Goal(id, this.taskText, this.completed, this.sortOrder, this.context, this.dateAdded, this.isPending);
    }

    public Goal withSortOrder(int sortOrder) {
        return new Goal(this.id, this.taskText, this.completed, sortOrder, this.context, this.dateAdded, this.isPending);
    }

    public Goal toggleCompleted() {
        return new Goal(this.id, this.taskText, !this.completed, this.sortOrder, this.context, this.dateAdded, this.isPending);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return completed == goal.completed && isPending() == goal.isPending() && Objects.equals(id, goal.id) && Objects.equals(taskText, goal.taskText) && Objects.equals(sortOrder, goal.sortOrder) && Objects.equals(context, goal.context) && Objects.equals(dateAdded.toLocalDate(), goal.dateAdded.toLocalDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskText, completed, sortOrder, context, dateAdded, isPending());
    }
}