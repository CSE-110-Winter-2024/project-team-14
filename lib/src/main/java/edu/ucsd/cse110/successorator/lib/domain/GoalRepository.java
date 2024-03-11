package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;

import java.util.List;

import edu.ucsd.cse110.successorator.lib.util.Subject;

public interface GoalRepository {
    Integer count();

    Subject<Goal> find(int id);

    Subject<Goal> findTomorrow(int id);

    Subject<List<Goal>> findAll();

    Subject<List<Goal>> findTomorrowAll();

    Subject<List<Goal>> findByContext(@NonNull String context);

    Subject<List<Goal>> findTomorrowByContext(@NonNull String context);

    void save(List<Goal> goals);

    void saveTomorrowGoal(List<TomorrowGoal> goals);

    void append(Goal goal);

    void remove(int id);

    void updateGoal(Goal goal);


    void appendTomorrowGoal(TomorrowGoal goal);

    void removeTomorrowGoal(Integer id);
}
