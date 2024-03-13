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
            LocalDateTime today = LocalDateTime.now();

            for (Goal goal: goals) {
                // check recurring here too later
                if (goal.getDateAdded().toLocalDate().isEqual(today.toLocalDate())) {
                    todayGoals.add(goal);
                }
            }

            // add recurring functionality

            adapter.clear();
            adapter.addAll(new ArrayList<>(todayGoals));
            adapter.notifyDataSetChanged();

            if (goals.size() == 0) {
                view.noGoalsText.setVisibility(View.VISIBLE);
            }
            else {
                view.noGoalsText.setVisibility(View.INVISIBLE);
            }
        });
        view.cardList.setAdapter(adapter);

        activityModel.getCurrentDateTime().observe((dateTime) -> {
            var formatter = DateTimeFormatter.ofPattern("'Today, 'E M/d", Locale.getDefault());
            view.dateTextView.setText(dateTime.format(formatter));
        });

        // 4. V -> M (BIND VIEW CLICKS TO MODEL UPDATES)
        view.addButton.setOnClickListener(v -> {
            CreateGoalDialogFragment dialog = CreateGoalDialogFragment.newInstance();
            // Assuming getParentFragmentManager() is valid. If you face issues, try getSupportFragmentManager() instead.
            dialog.show(getChildFragmentManager(), "CreateGoalDialog");
        });

        view.cardList.setOnItemClickListener((parent, view, position, id) -> {
            Goal clickedGoal = adapter.getItem(position);
            if (clickedGoal == null) return;
            activityModel.updateGoal(clickedGoal);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        activityModel.setCurrentDateTime(LocalDateTime.now());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();
        if (itemId == R.id.action_bar_menu_move_views) {
            var tomorrowJustPast2Am = activityModel.getCurrentDateTime().getValue()
                    .truncatedTo(ChronoUnit.DAYS)
                    .plusDays(1)
                    .withHour(2)
                    .withMinute(1);
            activityModel.setCurrentDateTime(tomorrowJustPast2Am);
        }
        return super.onOptionsItemSelected(item);
    }

//    private boolean isReccuringToday(Goal goal, LocalDateTime today) {
//        switch (goal.getReccurence()) {
//            case "one-time":
//                return false;
//            case "daily":
//                return true;
//            case "weekly":
//                return goal.getDateAdded().getDayOfWeek() == today.getDayOfWeek();
//            case "monthly":
//                return goal.getDateAdded().getDayOfMonth() == today.getDayOfMonth();
//            case "yearly":
//                return goal.getDateAdded().getDayOfYear() == today.getDayOfYear();
//            default:
//                return false;
//        }
//    }

}
