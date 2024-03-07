package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface GoalRepository {
    Integer count();

    Subject<Goal> find(int id);

    Subject<List<Goal>> findAll();

    void save(List<Goal> goals);

    void addOneTimeGoal(Goal goal);

    void addRecurringGoal(RecurringGoal goal);

    void removeRecurringGoal(int id);

    void removeOneTimeGoal(int id);

    void updateOneTimeGoal(Goal goal);
}
