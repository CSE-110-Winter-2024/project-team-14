package edu.ucsd.cse110.successorator.app.ui.cardlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.app.databinding.CardListItemBinding;

public class CardListAdapter extends ArrayAdapter<Goal> {
    public CardListAdapter(Context context, List<Goal> goals) {

        super(context, 0, new ArrayList<>(goals));

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the goal for this position
        var goal = getItem(position);
        assert goal != null;

        // Check if an existing view is being reused, otherwise inflate the view
        CardListItemBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = CardListItemBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML.
            var layoutInflater = LayoutInflater.from(getContext());
            binding = CardListItemBinding.inflate(layoutInflater, parent, false);
        }

        // Populate the view with the goal's data
        // Assuming Goal class has getDescription() method for the task description
        binding.taskText.setText(goal.taskText());

        return binding.getRoot();
    }

    @Override
    public boolean hasStableIds() {
        // Return true if your items' ids are unique and stable
        return true;
    }

    @Override
    public long getItemId(int position) {
        // Assuming Goal class has getId() method returning a unique ID as long
        Goal goal = getItem(position);
        assert goal != null;

        var id = goal.id();

        assert id != null;
        return id;
    }

}
