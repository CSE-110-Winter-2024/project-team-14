package edu.ucsd.cse110.successorator;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import edu.ucsd.cse110.successorator.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.databinding.FragmentCardListBinding;
import edu.ucsd.cse110.successorator.ui.cardlist.CardListAdapter;
import edu.ucsd.cse110.successorator.ui.cardlist.CardListFragment;

public class MainActivity extends AppCompatActivity {
    private MainViewModel activityModel;

    private ActivityMainBinding view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
//        view.placeholderText.setText("No goals for the day!");
        setContentView(view.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, CardListFragment.newInstance())
                .commit();
    }
}
