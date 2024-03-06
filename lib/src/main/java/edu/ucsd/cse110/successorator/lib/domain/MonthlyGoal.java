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

        //The following code checks which day of the month the start day is,
        // e.g. the second Tuesday of the month

        DayOfWeek dayOfWeekToRepeat;
        LocalDateTime nextDate;
        LocalDateTime checkDayOfMonth;
        int count = 0;
        dayOfWeekToRepeat = this.startDate.getDayOfWeek();
        int maxDayOfMonth = getLastDayOfMonth(this.startDate.getMonthValue());

        int i = 1;
        do {
            checkDayOfMonth = startDate.withDayOfMonth(i);
            if (checkDayOfMonth.getDayOfWeek().equals(dayOfWeekToRepeat)) {
                count +=1;
            }
            i+=1;
        } while (!startDate.equals(checkDayOfMonth));

        //iterate through the next month until you get to the specific day of the week
        //e.g. the second Tuesday of the next month
        LocalDateTime nextMonth = startDate.withDayOfMonth(1).plusMonths(1);
        int days = 0;
        for(int j = 1; j <= maxDayOfMonth; j++) {
            var currDate = nextMonth.withDayOfMonth(j);
            var currDay = currDate.getDayOfWeek();
            if(currDay.equals(dayOfWeekToRepeat)) {
                days+= 1;
            }
            if (days == count) {
                nextDate = currDate;
                return nextDate;
            }
        }

        //if the day doesn't exist in the next month, rollover
        var currDate = nextMonth.plusMonths(1);
        var currDay = currDate.getDayOfWeek();
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
}
