package edu.ucsd.cse110.successorator.app.ui.cardlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.app.databinding.CardListItemBinding;

public class CardListAdapter extends ArrayAdapter<Goal> {
    public CardListAdapter(Context context, List<Goal> goals) {
        super(context, 0, new ArrayList<>(goals));
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the goal for this position
        var goal = getItem(position);
        assert goal != null;

        // check if a view is being reused
        CardListItemBinding binding;
        if (convertView != null) {
            // if so, bind to it
            binding = CardListItemBinding.bind(convertView);
        } else {
            // otherwise inflate a new view from our layout xml
            var layoutInflater = LayoutInflater.from(getContext());
            binding = CardListItemBinding.inflate(layoutInflater, parent, false);
        }

        // populate the view with the goal's data
        binding.taskText.setText(goal.taskText());

        return binding.getRoot();
    }

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
