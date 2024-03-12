package edu.ucsd.cse110.successorator.app;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.app.ui.cardlist.PendingFragment;
import edu.ucsd.cse110.successorator.app.ui.cardlist.RecurringFragment;
import edu.ucsd.cse110.successorator.app.ui.cardlist.TodayFragment;
import edu.ucsd.cse110.successorator.app.ui.cardlist.TomorrowFragment;

// Assuming CardListAdapter is suitable for displaying Goal objects.
// If not, replace CardListAdapter with your Goal-specific adapter.

public class MainActivity extends AppCompatActivity {
    private MainViewModel activityModel;
    private ActivityMainBinding view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_title);

        this.view = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(view.getRoot());

        ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        activityModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        return true;
    }

    public void swapToToday() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, TodayFragment.newInstance())
                .commit();
    }

    public void swapToTomorrow() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, TomorrowFragment.newInstance())
                .commit();
    }

    public void swapToPending() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, PendingFragment.newInstance())
                .commit();
    }

    public void swapToRecurring() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, RecurringFragment.newInstance())
                .commit();
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        var buttonId = view.getId();

        if (buttonId == R.id.dropdown_button) {
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_views, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();

                // Handle menu item clicks here
                if (itemId == R.id.today_button) {
                    swapToToday();
                } else if (itemId == R.id.tomorrow_button) {
                    swapToTomorrow();
                } else if (itemId == R.id.pending_button) {
                    swapToPending();
                } else if (itemId == R.id.recurring_button) {
                    swapToRecurring();
                } else {
                    return false;
                }

                return true;
            });
        }
        else {
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_focus, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(item -> {
                int itemId = item.getItemId();
                int color = 0;

                // Handle menu item clicks here
                if (itemId == R.id.home_button) {
                    activityModel.filterByContext("Home");
                    color = ContextCompat.getColor(this, R.color.homeDotColor);
                } else if (itemId == R.id.work_button) {
                    activityModel.filterByContext("Work");
                    color = ContextCompat.getColor(this, R.color.workDotColor);
                } else if (itemId == R.id.school_button) {
                    activityModel.filterByContext("School");
                    color = ContextCompat.getColor(this, R.color.schoolDotColor);
                } else if (itemId == R.id.errands_button) {
                    activityModel.filterByContext("Errands");
                    color = ContextCompat.getColor(this, R.color.errandsDotColor);
                } else if (itemId == R.id.cancel_button) {
                    activityModel.cancelFilter();
                } else {
                    return false;
                }

                view.findViewById(buttonId).setBackgroundColor(color);
                return true;
            });
        }

        popupMenu.show();
    }
}


