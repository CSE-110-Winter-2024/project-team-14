package edu.ucsd.cse110.successorator.lib.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class MonthlyGoal extends RecurringGoal {
    public MonthlyGoal(@Nullable Integer id, @NonNull String taskText, boolean completed, int sortOrder, LocalDateTime startDate) {
        super(id, taskText, completed, sortOrder, startDate);
    }

    @Override
    public LocalDateTime setNextDate() {

        int dayOfMonth = findDayNumberInMonth();
        DayOfWeek dayOfWeekToRepeat = startDate.getDayOfWeek();
        LocalDateTime nextMonth = startDate.withDayOfMonth(1).plusMonths(1);
        int maxDayOfMonth = getLastDayOfMonth(nextMonth.getMonthValue());

        //iterate through the next month until you get to the specific day of the week
        //e.g. the second Tuesday of the next month
        int daysFound = 0;
        for(int j = 1; j <= maxDayOfMonth; j++) {
            var currDate = nextMonth.withDayOfMonth(j);
            var currDay = currDate.getDayOfWeek();
            if(currDay.equals(dayOfWeekToRepeat)) {
                daysFound += 1;
            }
            if (daysFound == dayOfMonth) {
                nextDate = currDate;
                return nextDate;
            }
        }

        //if the day doesn't exist in the next month, rollover
        nextMonth = nextMonth.plusMonths(1);
        var currDate = nextMonth;
        var currDay = nextMonth.getDayOfWeek();
        while(!currDay.equals(dayOfWeekToRepeat)) {
            currDate = currDate.plusDays(1);
            currDay = currDate.getDayOfWeek();
        }
        nextDate = currDate;
        return nextDate;
    }

    public int getLastDayOfMonth(int month) {
        if(month == 1|| month == 3|| month == 5|| month == 7|| month == 8|| month ==12) {
            return 31;
        }

        else if(month == 4|| month == 6|| month == 9|| month == 11) {
            return 30;
        }
        //february
        //TODO: Account for leap year
        else {
            return 28;
        }

    }

    //Checks which day of the month the start day is,
    // e.g. the second Tuesday of the month

    public int findDayNumberInMonth() {
        DayOfWeek dayOfWeekToRepeat = this.startDate.getDayOfWeek();;
        LocalDateTime currentDayOfMonth;
        int count = 0;
        int maxDayOfMonth = getLastDayOfMonth(this.startDate.getMonthValue());
        int i = 1;

        do {
            currentDayOfMonth = startDate.withDayOfMonth(i);
            if (currentDayOfMonth.getDayOfWeek().equals(dayOfWeekToRepeat)) {
                count +=1;
            }
            i+=1;
        } while (!startDate.equals(currentDayOfMonth));
        return count;
    }
}
