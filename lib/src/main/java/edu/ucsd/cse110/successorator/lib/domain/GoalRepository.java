package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface GoalRepository {
    Integer countOneTimeGoals();

    Integer countRecurringGoals();

    Subject<Goal> findOneTimeGoal(int id);

    Subject<RecurringGoal> findRecurringGoal(int id);

    Subject<List<Goal>> findAllOneTimeGoals();

    Subject<List<RecurringGoal>> findAllRecurringGoals();

    void saveOneTimeGoal(List<Goal> goals);

    void saveRecurringGoal(List<RecurringGoal> goals);

    void addOneTimeGoal(Goal goal);

    void addRecurringGoal(RecurringGoal goal);

    void removeRecurringGoal(int id);

    void removeOneTimeGoal(int id);

    void updateOneTimeGoal(Goal goal);
}
