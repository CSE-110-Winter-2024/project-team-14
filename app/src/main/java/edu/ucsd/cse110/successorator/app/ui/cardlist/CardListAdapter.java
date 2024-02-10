package edu.ucsd.cse110.successorator.app.ui.cardlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.app.databinding.ListItemCardBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

public class CardListAdapter extends ArrayAdapter<Goal> {
    public CardListAdapter(Context context, List<Goal> goals) {
        // this sets a lot of stuff internally which we can access
        // with getContext() and getItem() for example

        // Also note that ArrayAdapter NEEDS a mutable List (ArrayList)
        // or it will crash!
        super(context, 0, new ArrayList<>(goals));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the goal for this position
        var goal = getItem(position);
        assert goal != null;

        //check if a view is being reused
        ListItemCardBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = ListItemCardBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout XML
            var layoutInflater = LayoutInflater.from(getContext());
            binding = ListItemCardBinding.inflate(layoutInflater, parent, false);
        }

        // populate the view with the goal's data
        binding.taskText.setText(goal.taskText());

        return binding.getRoot();
    }

    // the below methods aren't strictly necessary usually.
    // But get in the habit of defining them because they never hurt
    // (as long as you have the IDs for each item) and sometimes you need them.

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        var goal = getItem(position);
        assert goal != null;

        var id = goal.id();
        assert id != null;

        return id;
    }
}
