package edu.ucsd.cse110.successorator.lib.domain;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Objects;
public class Goal implements Serializable {
    private final @Nullable Integer id;
    private final @NonNull String taskText;
    private final boolean completed;
    private final @NonNull Integer sortOrder;

    private final @NonNull String context;

    private LocalDateTime nextDate;

    private final String typeOfGoal;


    public Goal(@Nullable Integer id, @NonNull String taskText, boolean completed, int sortOrder, @NonNull String context, @NonNull String typeOfGoal, @Nullable String nextDate) {
        this.id = id;
        this.taskText = taskText;
        this.completed = completed;
        this.sortOrder = sortOrder;
        this.context = context;
        this.nextDate = setNextDate();
        this.typeOfGoal = typeOfGoal;
    }

    public @Nullable Integer id() {
        return id;
    }
    public @NonNull String taskText() {
        return taskText;
    }
    public boolean completed() {
        return completed;
    }
    public int sortOrder() {
        return sortOrder;
    }

    public @NonNull String context() {
        return context;
    }

    public String type() {
        return typeOfGoal;
    }
    public LocalDateTime nextDate() {
        return this.nextDate;
    }

    public Goal withId(int id) {
        return new Goal(id, this.taskText, this.completed, this.sortOrder, this.context, this.typeOfGoal, this.nextDate.toString());
    }

    public Goal withSortOrder(int sortOrder) {
        return new Goal(this.id, this.taskText, this.completed, sortOrder, this.context, this.typeOfGoal, this.nextDate.toString());
    }

    public Goal toggleCompleted() {
        return new Goal(this.id, this.taskText, !this.completed, this.sortOrder, this.context, this.typeOfGoal, this.nextDate.toString());
    }

    public LocalDateTime setNextDate() {
        var startDate =  LocalDateTime.now();
        switch(typeOfGoal) {
            case "daily":
                this.nextDate = LocalDateTime.now().plusDays(1);
            case "weekly":
                this.nextDate = LocalDateTime.now().plusDays(7);
                break;
            case "monthly":
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
            case "yearly":
                return LocalDateTime.now().plusYears(1);
            default: return LocalDateTime.MIN;
        }
        return startDate;
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
    private int findDayNumberInMonth() {
        var startDate = LocalDateTime.now();
        DayOfWeek dayOfWeekToRepeat = startDate.getDayOfWeek();;
        LocalDateTime currentDayOfMonth;
        int count = 0;
        int maxDayOfMonth = getLastDayOfMonth(startDate.getMonthValue());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return completed == goal.completed && Objects.equals(id, goal.id) && Objects.equals(taskText, goal.taskText) && Objects.equals(sortOrder, goal.sortOrder) && Objects.equals(context, goal.context) && Objects.equals(nextDate, goal.nextDate) && Objects.equals(typeOfGoal, goal.typeOfGoal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskText, completed, sortOrder, context, typeOfGoal, nextDate);
    }

}