package edu.ucsd.cse110.successorator.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.app.ui.dialog.CreateGoalDialogFragment;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

// Assuming CardListAdapter is suitable for displaying Goal objects.
// If not, replace CardListAdapter with your Goal-specific adapter.
import edu.ucsd.cse110.successorator.app.ui.cardlist.CardListAdapter;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel model;
    private CardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the title for the activity; replace R.string.app_title with your actual title resource ID
        setTitle(R.string.app_title);

        var modelOwner = this;
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);

        // Initialize the InMemoryDataSource, GoalRepository, and MainViewModel
//        var dataSource = InMemoryDataSource.fromDefault();
//        var goalRepository = new GoalRepository(dataSource);
//        this.model = new MainViewModel(goalRepository);
        this.model = modelProvider.get(MainViewModel.class);

        // Initialize the binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the adapter with an empty list
        this.adapter = new CardListAdapter(this, List.of());


//         Observe changes in the ordered goals from the ViewModel and update the adapter accordingly
        //doesnt require live data so we do not need this, goals
        model.getOrderedGoals().observe(goals -> {
            if (goals == null) return;

            adapter.clear();
            adapter.addAll(new ArrayList<>(goals));
            adapter.notifyDataSetChanged();
        });

        binding.cardList.setAdapter(adapter);

        binding.addButton.setOnClickListener(v -> {
            CreateGoalDialogFragment dialog = CreateGoalDialogFragment.newInstance();
            dialog.show(getSupportFragmentManager(), "CreateGoalDialog");
        });

        binding.cardList.setAdapter(adapter);

        binding.cardList.setOnItemClickListener((parent, view, position, id) -> {
            Goal clickedGoal = adapter.getItem(position);
            if (clickedGoal == null) return;

            model.updateGoal(clickedGoal);
            adapter.notifyDataSetChanged();
        });

    }

}
