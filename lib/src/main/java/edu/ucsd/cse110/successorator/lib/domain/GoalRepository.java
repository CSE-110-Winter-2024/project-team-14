package edu.ucsd.cse110.successorator.lib.domain;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface GoalRepository {
    Integer countAllGoals();

    Integer countOneTimeGoals();

    Integer countRecurringGoals();

    Subject<Goal> find(int id);

    Subject<List<Goal>> findAll();

    Subject<List<Goal>> findAllOneTimeGoals();

    Subject<List<Goal>> findAllRecurringGoals();

    //Do we need findAllMonthlyGoals() and findAllWeeklyGoals??

    void save(List<Goal> goals);

    void append(Goal goal);

    void remove(int id);

    void updateGoal(Goal goal);
}
