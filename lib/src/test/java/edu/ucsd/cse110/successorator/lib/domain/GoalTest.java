package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GoalTest {

   @Test
   public void taskId() {
       var goal = new Goal(0, "get groceries", false, 0,
               "School", LocalDateTime.now(), "one_time", true);
       Integer id = 0;
       assertEquals(id, goal.id());
   }

    @Test
    public void taskText() {
        var goal = new Goal(0, "get groceries", false, 0,
                "School", LocalDateTime.now(),"one_time",  true);
        String text = "get groceries";
        assertEquals(text, goal.taskText());
    }

    @Test
    public void taskCompleted() {
        var goal = new Goal(0, "get groceries", false, 0, "School", LocalDateTime.now(), "one_time", true);
        assertFalse(goal.completed());
        goal = goal.toggleCompleted();
        assertTrue(goal.completed());
    }

    @Test
    public void sortOrder() {
        var goal = new Goal(0, "get groceries", false, 0, "School", LocalDateTime.now(), "one_time", true);
        assertEquals(goal.sortOrder(), 0);
        goal = goal.withSortOrder(3);
        assertEquals(goal.sortOrder(), 3);
    }


    @Test
    public void taskContext() {
        var goal = new Goal(0, "get groceries", false, 0,
                "School", LocalDateTime.now(), "one_time", true);
        String context = "School";
        assertEquals(context, goal.context());
    }

    @Test
    public void taskDateAdded() {
        var goal = new Goal(0, "get groceries", false, 0,
                "School", LocalDateTime.now().plusDays(1), "one_time", true);
        LocalDate dateAdded = LocalDateTime.now().plusDays(1).toLocalDate();
        assertEquals(dateAdded, goal.dateAdded().toLocalDate());
    }

    @Test
    public void taskIsPending() {
        var goal = new Goal(0, "get groceries", false, 0,
                "School", LocalDateTime.now().plusDays(1), "one_time", true);
        boolean isPending = true;
        assertEquals(isPending, goal.isPending());
    }

    @Test
    public void withIdTest() {
        var goal = new Goal(0, "get groceries", false, 0,
                "School", LocalDateTime.now().plusDays(1), "one_time", true);
        Goal newGoal = goal.withId(1);
        assertEquals((Integer)1, newGoal.id());
    }

    @Test
    public void withSortOrderTest() {
        var goal = new Goal(0, "get groceries", false, 0,
                "School", LocalDateTime.now().plusDays(1), "one_time", true);
        Goal newGoal = goal.withSortOrder(1);
        assertEquals(1, newGoal.sortOrder());
    }

    @Test
    public void toggleCompletedTest() {
        var goal = new Goal(0, "get groceries", true, 0,
                "School", LocalDateTime.now().plusDays(1), "one_time", true);
        Goal newGoal = goal.toggleCompleted();
        assertEquals(false, newGoal.completed());
    }

    @Test
    public void testEquals() {
        var goal1 = new Goal(0, "get groceries", false, 0, "School", LocalDateTime.now(), "one_time", true);
        var goal2 = new Goal(0, "get groceries", false, 0, "School", LocalDateTime.now(), "one_time", true);
        assertEquals(goal1, goal2);
    }
}