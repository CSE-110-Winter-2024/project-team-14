package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class GoalTest {

    @Test
    public void taskText() {
        var goal = new Goal(0, "get groceries", false, 0);
        String text = "get groceries";
        assertEquals(text, goal.taskText());
    }

    @Test
    public void completed() {
        var goal = new Goal(0, "get groceries", false, 0);
        assertFalse(goal.completed());
        goal = goal.toggleCompleted();
        assertTrue(goal.completed());
    }

    @Test
    public void sortOrder() {
        var goal = new Goal(0, "get groceries", false, 0);
        assertEquals(goal.sortOrder(), 0);
        goal = goal.withSortOrder(3);
        assertEquals(goal.sortOrder(), 3);
    }

    @Test
    public void testEquals() {
        var goal1 = new Goal(0, "get groceries", false, 0);
        var goal2 = new Goal(0, "get groceries", false, 0);
        assertEquals(goal1, goal2);
    }
}