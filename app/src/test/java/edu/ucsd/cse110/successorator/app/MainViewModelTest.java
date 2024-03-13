package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.*;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

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

        Goal g = new Goal(null, "do homework", false, 4, "school");
        mvm.append(g);

        assertEquals("do homework", mvm.getOrderedGoals().getValue().get(0).taskText());
    }

    @Test
    public void getOrderedGoals() {
        InMemoryDataSource data = new InMemoryDataSource();
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        GoalRepository repo = new SimpleGoalRepository(data);
        var mvm = new MainViewModel(repo, timeKeeper);

        mvm.append(new Goal(null, "do homework", false, 4));
        var orderedGoals = mvm.getOrderedGoals();
        assertEquals(1, orderedGoals.getValue().size());
    }

    @Test
    public void remove() {
        InMemoryDataSource data = new InMemoryDataSource();
        TimeKeeper timeKeeper = new InMemoryTimeKeeper();
        GoalRepository repo = new SimpleGoalRepository(data);
        var mvm = new MainViewModel(repo, timeKeeper);

        Goal g1 = new Goal(null, "do homework", false, 0);
        mvm.append(g1);
        mvm.append(new Goal(null, "wash dishes", false, 1));
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

        Goal g = new Goal(null, "do homework", false, 1);
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

        Goal g = new Goal(null, "do homework", false, 1);
        mainViewModel.append(g);
        Goal g2 = new Goal(null, "do homework2", false, 2);
        mainViewModel.append(g2);
        Goal g3 = new Goal(null, "do homework3", false, 3);
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

        Goal g = new Goal(null, "do homework", false, 1);
        mainViewModel.append(g);
        Goal g2 = new Goal(null, "do homework2", false, 2);
        mainViewModel.append(g2);
        Goal g3 = new Goal(null, "do homework3", true, 3);
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

        Goal g = new Goal(null, "do homework", true, 1);
        mainViewModel.append(g);
        Goal g2 = new Goal(null, "do homework2", true, 2);
        mainViewModel.append(g2);
        Goal g3 = new Goal(null, "do homework3", true, 3);
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
}