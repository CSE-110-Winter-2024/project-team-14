package edu.ucsd.cse110.successorator.app;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

public class MainViewModelTest {

//    @Test
//    public void append() {
//        InMemoryDataSource data = new InMemoryDataSource();
//        GoalRepository repo = new GoalRepository(data);
//        var mvm = new MainViewModel(repo);
//        Goal g = new Goal(0, "do homework", false, 4);
//        mvm.append(g);
//
//        assertEquals("do homework", mvm.getOrderedGoals().getValue().get(0).taskText());
//    }
//
//    @Test
//    public void getOrderedGoals() {
//        InMemoryDataSource data = new InMemoryDataSource();
//        GoalRepository repo = new GoalRepository(data);
//        var mvm = new MainViewModel(repo);
//        mvm.append(new Goal(3, "do homework", false, 4));
//        var orderedGoals = mvm.getOrderedGoals();
//        assertEquals(1, orderedGoals.getValue().size());
//    }
//
//    @Test
//    public void remove() {
//        InMemoryDataSource data = new InMemoryDataSource();
//        GoalRepository repo = new GoalRepository(data);
//        var mvm = new MainViewModel(repo);
//        Goal g1 = new Goal(0, "do homework", false, 1);
//        mvm.append(g1);
//        mvm.append(new Goal(1, "wash dishes", false, 2));
//        mvm.remove(1);
//        var orderedGoals = mvm.getOrderedGoals();
//
//        assertEquals(1, orderedGoals.getValue().size());
//        assertEquals("do homework", mvm.getOrderedGoals().getValue().get(0).taskText());
//    }
//
//    @Test
//    public void updateGoal() {
//        InMemoryDataSource data = new InMemoryDataSource();
//        GoalRepository repo = new GoalRepository(data);
//        var mvm = new MainViewModel(repo);
//
//        Goal g = new Goal(null, "do homework", false, 1);
//        mvm.append(g);
//        System.out.println(data.getGoals().get(0).taskText());
//        //mvm.updateGoal(g);
//    }
}