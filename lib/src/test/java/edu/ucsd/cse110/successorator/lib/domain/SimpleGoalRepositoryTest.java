package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class SimpleGoalRepositoryTest {
    SimpleGoalRepository gr;
    InMemoryDataSource data = new InMemoryDataSource();
    List<Goal> mockDataSource;
    @Before
    public void setup() {
        this.data = new InMemoryDataSource();
        mockDataSource = List.of(
                new Goal(0, "Wash dishes", false, 0),
                new Goal(1, "Do laundry", false, 1),
                new Goal(2, "Cook lunch", false, 2));
        data.addGoals(mockDataSource);
        this.gr = new SimpleGoalRepository(data);
    }

    @Test
    public void addGoalTest() {
        assertEquals(Integer.valueOf(3), gr.count());
        Goal goal1 = new Goal(null, "water plants", false, -1);
        gr.append(goal1);
        assertEquals(Integer.valueOf(4), gr.count());

        List<Goal> newGoalList1 = List.of(
                new Goal(0, "Wash dishes", false, 0),
                new Goal(1, "Do laundry", false, 1),
                new Goal(2, "Cook lunch", false, 2),
                new Goal(3, "water plants", false, 3));
        assertEquals(newGoalList1, data.getGoals());

        Goal goal2 = new Goal(null, "sweep floor", false, -1);
        gr.append(goal2);
        assertEquals(Integer.valueOf(5), gr.count());

        List<Goal> newGoalList2 = List.of(
                new Goal(0, "Wash dishes", false, 0),
                new Goal(1, "Do laundry", false, 1),
                new Goal(2, "Cook lunch", false, 2),
                new Goal(3, "water plants", false, 3),
                new Goal(4, "sweep floor", false, 4));
        assertEquals(newGoalList2, data.getGoals());
    }

    @Test
    public void updateGoalTest() {
        assertEquals(Integer.valueOf(3), gr.count());
        gr.updateGoal(data.getGoals().get(1));
        List<Goal> newGoalList1 = List.of(
                new Goal(0, "Wash dishes", false, 0),
                new Goal(1, "Do laundry", true, 2),
                new Goal(2, "Cook lunch", false, 1));

        assertEquals(newGoalList1, data.getGoals());

        // bring back to uncompleted list
        gr.updateGoal(data.getGoals().get(1));
        List<Goal> newGoalList2 = List.of(
                new Goal(0, "Wash dishes", false, 1),
                new Goal(1, "Do laundry", false, 0),
                new Goal(2, "Cook lunch", false, 2));

        assertEquals(newGoalList2, data.getGoals());

        // completing last one, should remain same sortorder
        gr.updateGoal(data.getGoals().get(2));
        List<Goal> newGoalList3 = List.of(
                new Goal(0, "Wash dishes", false, 1),
                new Goal(1, "Do laundry", false, 0),
                new Goal(2, "Cook lunch", true, 2));

        assertEquals(newGoalList3, data.getGoals());

        gr.updateGoal(data.getGoals().get(0));
        List<Goal> newGoalList4 = List.of(
                new Goal(0, "Wash dishes", true, 2),
                new Goal(1, "Do laundry", false, 0),
                new Goal(2, "Cook lunch", true, 1));

        assertEquals(newGoalList4, data.getGoals());
    }


    @Test
    public void updateAndAddGoalTest() {
        assertEquals(Integer.valueOf(3), gr.count());
        gr.updateGoal(data.getGoals().get(1));
        List<Goal> newGoalList1 = List.of(
                new Goal(0, "Wash dishes", false, 0),
                new Goal(1, "Do laundry", true, 2),
                new Goal(2, "Cook lunch", false, 1));
        assertEquals(newGoalList1, data.getGoals());

        Goal goal1 = new Goal(null, "water plants", false, -1);
        gr.append(goal1);
        assertEquals(Integer.valueOf(4), gr.count());
        List<Goal> newGoalList2 = List.of(
                new Goal(0, "Wash dishes", false, 0),
                new Goal(1, "Do laundry", true, 3),
                new Goal(2, "Cook lunch", false, 1),
                new Goal(3, "water plants", false, 2));
        assertEquals(newGoalList2, data.getGoals());
    }

}
