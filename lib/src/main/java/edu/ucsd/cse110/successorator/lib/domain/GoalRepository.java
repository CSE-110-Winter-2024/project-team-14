package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface GoalRepository {
    Integer count();

    Subject<Goal> find(int id);

    Subject<List<Goal>> findAll();

    Subject<List<Goal>> findByContext(@NonNull String context);

    void save(List<Goal> goals);

    void append(Goal goal);

    void remove(int id);

    void updateGoal(Goal goal);
}
