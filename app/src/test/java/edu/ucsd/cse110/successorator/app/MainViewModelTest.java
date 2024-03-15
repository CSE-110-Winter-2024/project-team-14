package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.InMemoryTimeKeeper;
import edu.ucsd.cse110.successorator.lib.domain.SimpleGoalRepository;
import edu.ucsd.cse110.successorator.lib.domain.TimeKeeper;

public class MainViewModelTest {

    @Test
    public void append() {
        InMemoryDataSource data = new InMemoryDataSource();
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        GoalRepository repo = new SimpleGoalRepository(data);
        var mvm = new MainViewModel(repo, timeKeeper);

        Goal g = new Goal(null, "do homework", false, 4, "school", LocalDateTime.now(), false);
        mvm.append(g);

        assertEquals("do homework", mvm.getOrderedGoals().getValue().get(0).taskText());
    }

    @Test
    public void getOrderedGoals() {
        InMemoryDataSource data = new InMemoryDataSource();
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        GoalRepository repo = new SimpleGoalRepository(data);
        var mvm = new MainViewModel(repo, timeKeeper);

        mvm.append(new Goal(null, "do homework", false, 4,"school", LocalDateTime.now(), false));
        var orderedGoals = mvm.getOrderedGoals();
        assertEquals(1, orderedGoals.getValue().size());
    }

    @Test
    public void remove() {
        InMemoryDataSource data = new InMemoryDataSource();
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        GoalRepository repo = new SimpleGoalRepository(data);
        var mvm = new MainViewModel(repo, timeKeeper);

        Goal g1 = new Goal(null, "do homework", false, 0, "school", LocalDateTime.now(), false);
        mvm.append(g1);
        mvm.append(new Goal(null, "wash dishes", false, 1, "school", LocalDateTime.now(), false));
        mvm.remove(1);
        var orderedGoals = mvm.getOrderedGoals();

        assertEquals(1, orderedGoals.getValue().size());
        assertEquals("wash dishes", mvm.getOrderedGoals().getValue().get(0).taskText());
    }

    @Test
    public void updateGoal() {
        InMemoryDataSource data = new InMemoryDataSource();
        GoalRepository repo = new SimpleGoalRepository(data);
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        var mvm = new MainViewModel(repo, timeKeeper);

        Goal g = new Goal(null, "do homework", false, 1, "school", LocalDateTime.now(), false);
        mvm.append(g);
        System.out.println(data.getGoals().get(0).taskText());
        //mvm.updateGoal(g);
    }

    @Test
    public void updateTime() {
        InMemoryDataSource data = new InMemoryDataSource();
        GoalRepository repo = new SimpleGoalRepository(data);
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        var mvm = new MainViewModel(repo, timeKeeper);

        mvm.setCurrentDateTime(LocalDateTime.now());
        assertEquals(LocalDateTime.now().getHour(), mvm.getCurrentDateTime().getValue().getHour());
        assertEquals(LocalDateTime.now().getMinute(), mvm.getCurrentDateTime().getValue().getMinute());

    }
    @Test
    public void listPersistence3TaskRollover() {
        InMemoryDataSource data = new InMemoryDataSource();
        GoalRepository repo = new SimpleGoalRepository(data);
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        var mainViewModel = new MainViewModel(repo, timeKeeper);

        mainViewModel.setCurrentDateTime(LocalDateTime.now());

        Goal g = new Goal(null, "do homework", false, 1, "school", LocalDateTime.now(), false);
        mainViewModel.append(g);
        Goal g2 = new Goal(null, "do homework2", false, 2, "school", LocalDateTime.now(), false);
        mainViewModel.append(g2);
        Goal g3 = new Goal(null, "do homework3", false, 3, "school", LocalDateTime.now(), false);
        mainViewModel.append(g3);

        // Get the current date and time
        LocalDateTime currentDateTime = mainViewModel.getCurrentDateTime().getValue();
        // Add one day to get to the next day
        LocalDateTime nextDayDateTime = currentDateTime.plusDays(1);
        // Set the time to 2 AM
        LocalDateTime nextDay2AM = nextDayDateTime.withHour(2).withMinute(0);
        // Set the updated date and time in the mainViewModel
        mainViewModel.setCurrentDateTime(nextDay2AM);

        List<Goal> orderedGoals = mainViewModel.getOrderedGoals().getValue();

        assertEquals(orderedGoals.size(), 3);
        assertEquals(orderedGoals.get(0).taskText(), g.taskText());
        assertEquals(orderedGoals.get(1).taskText(), g2.taskText());
        assertEquals(orderedGoals.get(2).taskText(), g3.taskText());
    }

    @Test
    public void listPersistence2TaskRollover1Complete() {
        InMemoryDataSource data = new InMemoryDataSource();
        GoalRepository repo = new SimpleGoalRepository(data);
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        var mainViewModel = new MainViewModel(repo, timeKeeper);

        mainViewModel.setCurrentDateTime(LocalDateTime.now());

        Goal g = new Goal(null, "do homework", false, 1, "school", LocalDateTime.now(), false);
        mainViewModel.append(g);
        Goal g2 = new Goal(null, "do homework2", false, 2, "school", LocalDateTime.now(), false);
        mainViewModel.append(g2);
        Goal g3 = new Goal(null, "do homework3", true, 3, "school", LocalDateTime.now(), false);
        mainViewModel.append(g3);

        List<Goal> orderedGoals = mainViewModel.getOrderedGoals().getValue();
        assertEquals(orderedGoals.size(), 3);

        // Get the current date and time
        LocalDateTime currentDateTime = mainViewModel.getCurrentDateTime().getValue();
        // Add one day to get to the next day
        LocalDateTime nextDayDateTime = currentDateTime.plusDays(1);
        // Set the time to 2 AM
        LocalDateTime nextDay2AM = nextDayDateTime.withHour(2).withMinute(0);
        // Set the updated date and time in the mainViewModel
        mainViewModel.setCurrentDateTime(nextDay2AM);

        List<Goal> orderedGoals2 = mainViewModel.getOrderedGoals().getValue();

        assertEquals(orderedGoals2.size(), 2);

        assertEquals(orderedGoals2.get(0).taskText(), g.taskText());
        assertEquals(orderedGoals2.get(1).taskText(), g2.taskText());
    }

    @Test
    public void listPersistenceAllTasksComplete() {
        InMemoryDataSource data = new InMemoryDataSource();
        GoalRepository repo = new SimpleGoalRepository(data);
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        var mainViewModel = new MainViewModel(repo, timeKeeper);

        mainViewModel.setCurrentDateTime(LocalDateTime.now());

        Goal g = new Goal(null, "do homework", true, 1, "school", LocalDateTime.now(), false);
        mainViewModel.append(g);
        Goal g2 = new Goal(null, "do homework2", true, 2, "school", LocalDateTime.now(), false);
        mainViewModel.append(g2);
        Goal g3 = new Goal(null, "do homework3", true, 3, "school", LocalDateTime.now(), false);
        mainViewModel.append(g3);

        List<Goal> orderedGoals = mainViewModel.getOrderedGoals().getValue();
        assertEquals(orderedGoals.size(), 3);

        // Get the current date and time
        LocalDateTime currentDateTime = mainViewModel.getCurrentDateTime().getValue();
        // Add one day to get to the next day
        LocalDateTime nextDayDateTime = currentDateTime.plusDays(1);
        // Set the time to 2 AM
        LocalDateTime nextDay2AM = nextDayDateTime.withHour(2).withMinute(0);
        // Set the updated date and time in the mainViewModel
        mainViewModel.setCurrentDateTime(nextDay2AM);
        List<Goal> orderedGoals2 = mainViewModel.getOrderedGoals().getValue();

        assertEquals(orderedGoals2.size(), 0);

    }

    @Test
    public void movePendingToToday() {
        InMemoryDataSource data = new InMemoryDataSource();
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        GoalRepository repo = new SimpleGoalRepository(data);
        var mvm = new MainViewModel(repo, timeKeeper);

        // add a goal here where pending is true. this goal shouldn't be in the assert list count
        Goal g0 = new Goal(null, "do homework", false, 0,
                "school", LocalDateTime.now(), true);
        mvm.append(g0);

        // move pending task to today
        Goal g1 = new Goal(null, "do homework", false, 0,
                "school", LocalDateTime.now(), false);
        mvm.append(g1);

        // this is a tomorrow goal - should not be included in todayGoals list
        Goal g2 = new Goal(null, "wash laundry", false, 0,
                "school", LocalDateTime.now().plusDays(1), false);
        mvm.append(g2);

        // add another today goal
        Goal g3 = new Goal(null, "do dishes", false, 0,
                "school", LocalDateTime.now(), false);
        mvm.append(g3);

        var orderedGoals = mvm.getOrderedGoals();
        assertEquals(4, orderedGoals.getValue().size());

        // check if todayGoals list should only have 1 today goal
        List<Goal> todayGoals = orderedGoals.getValue().stream().filter(goal -> goal.dateAdded()
                .toLocalDate().equals(LocalDateTime.now().toLocalDate()) && !goal.isPending()).collect(Collectors.toList());
        assertEquals(2, todayGoals.size());
    }

    @Test
    public void movePendingToTomorrow() {
        InMemoryDataSource data = new InMemoryDataSource();
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        GoalRepository repo = new SimpleGoalRepository(data);
        var mvm = new MainViewModel(repo, timeKeeper);

        // add a goal here where pending is true. this goal shouldn't be in the assert list count
        Goal g0 = new Goal(null, "do homework", false, 0,
                "school", LocalDateTime.now(), true);
        mvm.append(g0);

        // move pending task to tomorrow
        Goal g1 = new Goal(null, "do homework", false, 0,
                "school", LocalDateTime.now().plusDays(1), false);
        mvm.append(g1);

        // this is a today goal - should not be included in tomorrowGoals list
        Goal g2 = new Goal(null, "wash laundry", false, 0,
                "school", LocalDateTime.now(), false);
        mvm.append(g2);

        // add another tomorrow goal
        Goal g3 = new Goal(null, "do homework", false, 0,
                "school", LocalDateTime.now().plusDays(1), false);
        mvm.append(g3);

        var orderedGoals = mvm.getOrderedGoals();
        assertEquals(4, orderedGoals.getValue().size());

        // check if tomorrowGoals list should only have 1 today goal
        List<Goal> tomorrowGoals = orderedGoals.getValue().stream().filter(goal -> goal.dateAdded()
                .toLocalDate().equals(LocalDateTime.now().plusDays(1).toLocalDate()) && !goal.isPending()).collect(Collectors.toList());
        assertEquals(2, tomorrowGoals.size());
    }


    @Test
    public void filterContextTest() {
        InMemoryDataSource data = new InMemoryDataSource();
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        GoalRepository repo = new SimpleGoalRepository(data);
        var mvm = new MainViewModel(repo, timeKeeper);

        // add a goal with "School" context
        Goal g0 = new Goal(null, "do homework", false, 0,
                "School", LocalDateTime.now(), false);
        mvm.append(g0);

        // add another goal with "School" context
        Goal g1 = new Goal(null, "do homework", false, 0,
                "School", LocalDateTime.now(), false);
        mvm.append(g1);

        // add a goal with "Work" context
        Goal g2 = new Goal(null, "wash laundry", false, 0,
                "Work", LocalDateTime.now(), false);
        mvm.append(g2);

        // add a goal with "Home" context
        Goal g3 = new Goal(0, "do homework", false, 0,
                "Home", LocalDateTime.now(), false);
        mvm.append(g3);

        var orderedGoals = mvm.getOrderedGoals();
        assertEquals(4, orderedGoals.getValue().size());

        List<Goal> schoolContextGoals = orderedGoals.getValue().stream().filter(goal -> goal.context().equals("School")).collect(Collectors.toList());
        assertEquals(2, schoolContextGoals.size());

        List<Goal> workContextGoals = orderedGoals.getValue().stream().filter(goal -> goal.context().equals("Work")).collect(Collectors.toList());
        assertEquals(1, workContextGoals.size());

        List<Goal> homeContextGoals = orderedGoals.getValue().stream().filter(goal -> goal.context().equals("Home")).collect(Collectors.toList());
        assertEquals(1, homeContextGoals.size());

        mvm.remove(g3.id());
        List<Goal> newHomeContextGoals = orderedGoals.getValue().stream().filter(goal -> goal.context().equals("Home")).collect(Collectors.toList());
        assertEquals(0, newHomeContextGoals.size());
    }

    @Test
    public void pendingPersistenceTest() {
        InMemoryDataSource data = new InMemoryDataSource();
        GoalRepository repo = new SimpleGoalRepository(data);
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        var mainViewModel = new MainViewModel(repo, timeKeeper);
        Goal g = new Goal(null, "do homework", true, 1, "school", LocalDateTime.now(), true);
        mainViewModel.append(g);
        Goal g2 = new Goal(null, "do homework2", true, 2, "school", LocalDateTime.now(), true);
        mainViewModel.append(g2);
        Goal g3 = new Goal(null, "do homework3", true, 3, "school", LocalDateTime.now(), false);
        mainViewModel.append(g3);
        var orderedGoals = mainViewModel.getOrderedGoals().getValue();
        assertEquals(3, orderedGoals.size());
        List<Goal> pendingGoals = orderedGoals.stream().filter(goal -> goal.dateAdded()
                .toLocalDate().equals(LocalDateTime.now().toLocalDate()) && goal.isPending()).collect(Collectors.toList());
        assertEquals(2, pendingGoals.size());
        // Get the current date and time
        LocalDateTime currentDateTime = mainViewModel.getCurrentDateTime().getValue();
        // Add one day to get to the next day
        LocalDateTime nextDayDateTime = currentDateTime.plusDays(1);
        // Set the time to 2 AM
        LocalDateTime nextDay2AM = nextDayDateTime.withHour(2).withMinute(0);
        // Set the updated date and time in the mainViewModel
        mainViewModel.setCurrentDateTime(nextDay2AM);
        List<Goal> pendingGoalsNextDay = orderedGoals.stream().filter(goal -> goal.dateAdded()
                .toLocalDate().equals(LocalDateTime.now().toLocalDate()) && goal.isPending()).collect(Collectors.toList());
        assertEquals(2, pendingGoalsNextDay.size());
    }
}