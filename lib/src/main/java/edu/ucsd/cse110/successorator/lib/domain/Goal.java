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
        return this.id;
    }
    public @Nullable String taskText() {
        return this.taskText;
    }
    public boolean completed() {
        return this.completed;
    }
    public @Nullable Integer sortOrder() {
        return this.sortOrder;
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
}