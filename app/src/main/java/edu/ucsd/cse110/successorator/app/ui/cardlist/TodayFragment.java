package edu.ucsd.cse110.successorator.app.ui.cardlist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.R;
import edu.ucsd.cse110.successorator.app.databinding.FragmentTodayBinding;
import edu.ucsd.cse110.successorator.app.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class TodayFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentTodayBinding view;
    private CardListAdapter adapter;

    private Goal clickedGoal;

    public TodayFragment() {
        // Required empty public constructor
    }

    public static TodayFragment newInstance() {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Model
        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);

        // Initialize the Adapter (with an empty list for now)
        this.adapter = new CardListAdapter(requireContext(), List.of());
        activityModel.getOrderedGoals().observe(goals -> {
            if (goals == null) return;
            adapter.clear();
            adapter.addAll(new ArrayList<>(goals)); // remember the mutable copy here!
            adapter.notifyDataSetChanged();
        });
    }



    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        this.view = FragmentTodayBinding.inflate(inflater, container, false);

        setupMvp();

        // Set the adapter on the ListView
        view.cardList.setAdapter(adapter);

        return view.getRoot();
    }

    private void setupMvp() {
        // 3. M -> V (MAKE VIEWS MATCH MODEL)
        // Observe changes in the ordered goals from the ViewModel and update the adapter accordingly
        // doesnt require live data so we do not need this, goals
        activityModel.getOrderedGoals().observe(goals -> {
            if (goals == null) {
                view.noGoalsText.setVisibility(View.VISIBLE);
                return;
            }

            List<Goal> todayGoals = new ArrayList<>();
            LocalDateTime today = LocalDateTime.now().plusDays(activityModel.buttonCount);

            for (Goal goal: goals) {
                if(goal.taskText().equals("DUMMY")){

                }
                else if((goal.getDateAdded().isBefore(today)) &&(goal.getRecurrence().equals("one_time"))){
                    todayGoals.add(goal);
                }
                else if ((goal.getDateAdded().toLocalDate().isEqual(today.toLocalDate()) ||
                        isReccuringToday(goal, today)) && (!goal.isPending()) ||
                        (goal.getDateAdded().isBefore(today) && (!goal.completed()) && (isReccuringToday(goal, today)))) {
                    //Goal temp = new Goal(goal.id(), goal.taskText(), goal.completed(), goal.sortOrder(), goal.context(), goal.getDateAdded(), "one_time", false);
                    todayGoals.add(goal);
                }
            }

            adapter.clear();
            adapter.addAll(new ArrayList<>(todayGoals));
            adapter.notifyDataSetChanged();

            if (todayGoals.size() == 0) {
                view.noGoalsText.setVisibility(View.VISIBLE);
            }
            else {
                view.noGoalsText.setVisibility(View.INVISIBLE);
            }
        });
        view.cardList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        activityModel.getCurrentDateTime().observe((dateTime) -> {
            LocalDateTime today = LocalDateTime.now().plusDays(activityModel.buttonCount);
            var formatter = DateTimeFormatter.ofPattern("'Today, 'E M/d", Locale.getDefault());
            view.dateTextView.setText(today.format(formatter));
            //adapter.notifyDataSetChanged();
        });

        // 4. V -> M (BIND VIEW CLICKS TO MODEL UPDATES)
        view.addButton.setOnClickListener(v -> {
            CreateGoalDialogFragment dialog = CreateGoalDialogFragment.newInstance();
            // Assuming getParentFragmentManager() is valid. If you face issues, try getSupportFragmentManager() instead.
            dialog.show(getChildFragmentManager(), "CreateGoalDialog");
        });

//        view.cardList.setOnItemClickListener((parent, view, position, id) -> {
//            Goal clickedGoal = adapter.getItem(position);
//            if (clickedGoal == null) return;
//            activityModel.updateGoal(clickedGoal);
//        });

        view.cardList.setOnItemLongClickListener((parent, view, position, id) -> {
            clickedGoal = adapter.getItem(position);
            if (clickedGoal == null) return false;
            showPopupMenu(view);
            //adapter.notifyDataSetChanged(); //added
            return true;
        });


    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.long_press_goal, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                // Handle menu item clicks here
                int itemId = item.getItemId();
                if (itemId == R.id.moveToday_button) {
                    // Need to implement moving to today
                    activityModel.switchPending(clickedGoal);
                    return true;
                } else if (itemId == R.id.moveTomorrow_button) {
                    // Need to implement moving to tomorrow
                    activityModel.switchPending(clickedGoal);
                    activityModel.setDate(clickedGoal, LocalDateTime.now().plusDays(1));
                    return true;
                } else if (itemId == R.id.finish_button) {
                    activityModel.updateGoal(clickedGoal);
                    return true;
                } else if (itemId == R.id.delete_button) {
                    activityModel.remove(clickedGoal.id());
                    return true;
                } else {
                    return false;
                }
            }
        });

        popupMenu.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        activityModel.setCurrentDateTime(LocalDateTime.now());
    }

    private boolean isReccuringToday(Goal goal, LocalDateTime today) {
        switch (goal.getRecurrence()) {
            case "one_time":
                return false;
            case "daily":
                return goal.getDateAdded().isBefore(LocalDateTime.now());
            case "weekly":
                return goal.getDateAdded().getDayOfWeek() == today.getDayOfWeek();
            case "monthly":
                return isSameWeekAndDayOfMonth(goal.getDateAdded(), today);
            case "yearly":
                return goal.getDateAdded().getDayOfYear() == today.getDayOfYear();
            default:
                return false;
        }
    }

    private boolean isSameWeekAndDayOfMonth(LocalDateTime dateAdded, LocalDateTime today) {
        return getWeekOfMonth(dateAdded) == getWeekOfMonth(today) &&
                dateAdded.getDayOfWeek() == today.getDayOfWeek();
    }

    private int getWeekOfMonth(LocalDateTime date) {
        return (date.getDayOfMonth() - 1) / 7 + 1;
    }

}


