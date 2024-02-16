package edu.ucsd.cse110.successorator.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.app.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

// Assuming CardListAdapter is suitable for displaying Goal objects.
// If not, replace CardListAdapter with your Goal-specific adapter.
import edu.ucsd.cse110.successorator.app.ui.cardlist.CardListAdapter;
import edu.ucsd.cse110.successorator.lib.util.Observer;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private MainViewModel model;
    private CardListAdapter adapter;
    //private Calendar calendar;
    TextView dateTextView;

    private List<Observer<String>> observers = new ArrayList<Observer<String>>();

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the title for the activity; replace R.string.app_title with your actual title resource ID
       // setTitle(R.string.app_title);

        // Set the content view to activity_main
        setContentView(R.layout.activity_main);


        // Find the dateTextView
        dateTextView = findViewById(R.id.dateTextView);


        // Initialize the InMemoryDataSource, GoalRepository, and MainViewModel
        var dataSource = InMemoryDataSource.fromDefault();
        var goalRepository = new GoalRepository(dataSource);
        this.model = new MainViewModel(goalRepository);

        // Initialize the binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the adapter with an empty list
        this.adapter = new CardListAdapter(this, new ArrayList<>());

//         Observe changes in the ordered goals from the ViewModel and update the adapter accordingly
        //doesnt require live data so we do not need this, goals
        model.getOrderedGoals().observe((List<Goal> goals) -> {
            if (goals == null) return;

            adapter.clear();
            adapter.addAll(goals);
            //Goal g1 = new Goal(10, "abc", false, -1);
           // adapter.add(g1);
            adapter.notifyDataSetChanged();
        });

        binding.cardList.setAdapter(adapter);

        binding.addButton.setOnClickListener(v -> {
            CreateGoalDialogFragment dialog = CreateGoalDialogFragment.newInstance();
            // Assuming getParentFragmentManager() is valid. If you face issues, try getSupportFragmentManager() instead.
            dialog.show(getSupportFragmentManager(), "CreateGoalDialog");
        });

        // Set the adapter on the ListView using the binding
        binding.cardList.setAdapter(adapter);
       // binding.dateTextView.setText(currentDateandTime);


        // Set the adapter on the ListView using the binding
        binding.cardList.setAdapter(adapter);
        binding.cardList.setOnItemClickListener((parent, view, position, id) -> {
            Goal clickedGoal = adapter.getItem(position);
            if (clickedGoal == null) return;

            model.updateGoal(clickedGoal);
            adapter.notifyDataSetChanged();
        });

        // Update the date immediately
        updateDate();


        // Schedule the alarm to trigger DateUpdateReceiver at 2 AM every day
     //   scheduleAlarm();


        //---------------------------- Attempt 2---------------------------------------
        /*
        Attempt 2: trying to implement with button at bottom with onCLicked
        */

//        // Find the arrow button and set a click listener
//        Button arrowButton = findViewById(R.id.arrowButton); // Replace `arrow_button` with the actual ID of your arrow button
//        arrowButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                moveNextDate(); // Call the `moveNextDate` method when the arrow button is clicked
//            }
//        });

    }



//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
//                             Bundle savedInstanceState){
//
//    }


    //------------------------------Attempt 1 with menu---------------------------------

//    //add menu option for ->
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    //
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();
        if (itemId == R.id.action_bar_menu_move_views) {
            moveNextDate();
        }
        return super.onOptionsItemSelected(item);
    }

    public void moveNextDate() {
        // Update the date immediately
        updateDateButton();

        // Clear all finished items in the list
        List<Goal> goals = model.getOrderedGoals().getValue();
        if (goals != null) {
            List<Goal> updatedGoals = new ArrayList<>();
            for (Goal goal : goals) {
                if (!goal.completed()) {
                    updatedGoals.add(goal);
                }
            }
            // Display the items in the list that rolled over to the next day
            adapter.clear();
            adapter.addAll(updatedGoals);
            adapter.notifyDataSetChanged();
        }
    }




    // Update the date immediately
    void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(calendar.getTime());
        binding.dateTextView.setText(currentDateandTime);
    }

    //update date with button click
    void updateDateButton() {
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(calendar.getTime());
        binding.dateTextView.setText(currentDateandTime);
    }


//-----------------------------Real time update at 2am-----------------------
    //    // Schedule the alarm to trigger DateUpdateReceiver at 2 AM every day
//    private void scheduleAlarm() {
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, DateUpdateReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        // Set the alarm to trigger at 2 AM every day
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 2);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//    }

    //write another updateDate method for calender.add to add the date proper
//
//    // BroadcastReceiver used to update the date at 2 AM every day
//    public static class DateUpdateReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // Update the date immediately
//            MainActivity mainActivity = new MainActivity();
//            mainActivity.updateDate();
//        }
//    }

}


