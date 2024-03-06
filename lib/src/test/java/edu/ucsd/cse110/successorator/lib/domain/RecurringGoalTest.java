package edu.ucsd.cse110.successorator.lib.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.LocalDateTime;

public class RecurringGoalTest {

    @Test
    public void setNextDateWeekly() {
        LocalDateTime currentTime = LocalDateTime.now();
        RecurringGoal weeklyGoal = new WeeklyGoal(0, "take out trash",
                false, 1, currentTime);
        assertEquals(currentTime.plusDays(7), weeklyGoal.nextDate);
    }

    @Test
    public void setNextDateMonthlyNoRollover() {
        LocalDateTime currentTime = LocalDateTime.now();
        RecurringGoal monthlyGoal = new MonthlyGoal(1, "take out trash",
                false, 2, currentTime);
        var expected = currentTime.plusMonths(1).withDayOfMonth(3);
        assertEquals(expected, monthlyGoal.nextDate);
    }

    @Test
    public void setNextDateMonthlyRollover() {
        LocalDateTime currentTime = LocalDateTime.now();
        currentTime = currentTime.withDayOfMonth(31);
        RecurringGoal monthlyGoal = new MonthlyGoal(2, "take out trash",
                false, 3, currentTime);
        var expected = currentTime.withMonth(5).withDayOfMonth(5);
        assertEquals(expected, monthlyGoal.nextDate);
    }
}