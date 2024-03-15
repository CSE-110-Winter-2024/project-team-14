package edu.ucsd.cse110.successorator.app.ui.cardlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import edu.ucsd.cse110.successorator.app.databinding.FragmentRecurringBinding;
import edu.ucsd.cse110.successorator.app.ui.dialog.CreateRecurringGoalDialogFragment;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class RecurringFragment extends Fragment {
    private MainViewModel activityModel;
    private FragmentRecurringBinding view;
    private CardListAdapter adapter;

    public RecurringFragment() {
        // Required empty public constructor
    }

    public static RecurringFragment newInstance() {
        RecurringFragment fragment = new RecurringFragment();
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

//        activityModel.setContext(null);

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
        this.view = FragmentRecurringBinding.inflate(inflater, container, false);

        setupMvp();

        // Set "Recurring" text to the dateTextView
        view.dateTextView.setText("Recurring");

        // Set the adapter on the ListView
        view.cardList.setAdapter(adapter);
        setContext();

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

            List<Goal> recurringGoals = new ArrayList<>();

            for (Goal goal : goals) {
                if (!goal.getRecurrence().equals("one_time")) {
                    recurringGoals.add(goal);
                }
            }

            adapter.clear();
            adapter.addAll(new ArrayList<>(recurringGoals));
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
            CreateRecurringGoalDialogFragment dialog = CreateRecurringGoalDialogFragment.newInstance();
            // Assuming getParentFragmentManager() is valid. If you face issues, try getSupportFragmentManager() instead.
            dialog.show(getChildFragmentManager(), "CreateGoalDialog");
        });

        //  binding.cardList.setAdapter(adapter); //added

        view.cardList.setOnItemClickListener((parent, view, position, id) -> {
            Goal clickedGoal = adapter.getItem(position);
            if (clickedGoal == null) return;
            activityModel.updateGoal(clickedGoal);
            //adapter.notifyDataSetChanged(); //added
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

    private int getContextColor() {
        String context = activityModel.getContext();
        if (context == null) {
            return 0;
        }

        switch (context) {
            case "Work":
                return ContextCompat.getColor(requireContext(), R.color.workDotColor);
            case "School":
                return ContextCompat.getColor(requireContext(), R.color.schoolDotColor);
            case "Home":
                return ContextCompat.getColor(requireContext(), R.color.homeDotColor);
            case "Errands":
                return ContextCompat.getColor(requireContext(), R.color.errandsDotColor);
            default:
                return 0;
        }
    }

    private String getContextText() {
        String context = activityModel.getContext();
        if (context == null) {
            return "";
        }

        return "Focus Mode: " + context;
    }

    private void setContext() {
        int color = getContextColor();
        String text = getContextText();
        view.focusMenuButton.setBackgroundColor(color);
        view.contextIndicator.setTextColor(color);
        view.contextIndicator.setText(text);
        view.contextIndicator.setVisibility(View.VISIBLE);
    }
}
