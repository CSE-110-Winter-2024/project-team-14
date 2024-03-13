package edu.ucsd.cse110.successorator.app.ui.cardlist;

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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.R;
import edu.ucsd.cse110.successorator.app.databinding.FragmentPendingBinding;
import edu.ucsd.cse110.successorator.app.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.app.ui.dialog.CreatePendingGoalDialogFragment;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class PendingFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentPendingBinding view;
    private CardListAdapter adapter;
    private Goal clickedGoal;

    public PendingFragment() {
        // Required empty public constructor
    }

    public static PendingFragment newInstance() {
        PendingFragment fragment = new PendingFragment();
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
        this.view = FragmentPendingBinding.inflate(inflater, container, false);

        setupMvp();

        // Set "Pending" text to the dateTextView
        view.dateTextView.setText("Pending");

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

            List<Goal> pendingGoals = new ArrayList<>();

            for (Goal goal : goals) {
                if (goal.isPending()) {
                    pendingGoals.add(goal);
                }
            }

            adapter.clear();
            adapter.addAll(new ArrayList<>(pendingGoals));
            adapter.notifyDataSetChanged();

            if (goals.size() == 0) {
                view.noGoalsText.setVisibility(View.VISIBLE);
            }
            else {
                view.noGoalsText.setVisibility(View.INVISIBLE);
            }
        });
        view.cardList.setAdapter(adapter);

        // 4. V -> M (BIND VIEW CLICKS TO MODEL UPDATES)
        view.addButton.setOnClickListener(v -> {
            CreatePendingGoalDialogFragment dialog = CreatePendingGoalDialogFragment.newInstance();
            // Assuming getParentFragmentManager() is valid. If you face issues, try getSupportFragmentManager() instead.
            dialog.show(getChildFragmentManager(), "CreatePendingGoalDialog");
        });

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
                    activityModel.switchPending(clickedGoal);
                    return true;
                } else if (itemId == R.id.moveTomorrow_button) {
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
}
