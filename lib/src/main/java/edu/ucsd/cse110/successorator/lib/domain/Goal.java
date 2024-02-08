package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

public class Goal implements Serializable {

    @NonNull
    private final String text;
    private final boolean completed;
    private final int id;

    Goal(@NonNull String text, int id, boolean completed) {
        this.text = text;
        this.id = id;
        this.completed = completed;
    }

    public String getGoalText() {
        return this.text;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public Goal toggleCompleted() {
        return new Goal(this.text, this.id, !completed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return completed == goal.completed && Objects.equals(text, goal.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, completed, id);
    }


}
