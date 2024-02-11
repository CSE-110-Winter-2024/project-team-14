package edu.ucsd.cse110.successorator.app;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.app.ui.cardlist.CardListAdapter;
import edu.ucsd.cse110.successorator.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding view;
    private MainViewModel model;
    private CardListAdapter adapter;

//    String fruitList[] = {"Apple", "Banana", "Apricot", "Orange", "Melon"};
//    ListView listView;

    protected void onCreate(@Nullable Bundle savedInstanceState, @NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        // initialize the model
        var dataSource = InMemoryDataSource.fromDefault();
        this.model = new MainViewModel(new GoalRepository(dataSource));

        // initialize the Adapter (with an empty list first)
        this.adapter = new CardListAdapter(this, List.of());
        this.model.getOrderedCards().observe(cards -> {
            if (cards == null) return;
            adapter.clear();
            System.out.println(cards);
            adapter.addAll(new ArrayList<>(cards));
            adapter.notifyDataSetChanged();
        });

        // initialize the view
        this.view = ActivityMainBinding.inflate(getLayoutInflater());

        // set the adapter on the ListView
        view.cardList.setAdapter(adapter);

        // observe model -> call view

        // observe view -> call model

        setContentView(view.getRoot());

//        listView = (ListView) findViewById(R.id.card_list);
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.card_list_item, R.id.task_text, fruitList);
//        listView.setAdapter(arrayAdapter);
    }
}
