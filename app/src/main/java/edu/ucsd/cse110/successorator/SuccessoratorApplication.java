package edu.ucsd.cse110.successorator;

import android.app.Application;

import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.ToDoList;

public class SuccessoratorApplication extends Application {
    private InMemoryDataSource dataSource;
    private ToDoList toDoList;

    @Override
    public void onCreate() {
        super.onCreate();

        this.dataSource = InMemoryDataSource.fromDefault();
        this.toDoList = new ToDoList(dataSource);
    }

    public ToDoList getToDoList() {
        return toDoList;
    }
}
