package edu.ucsd.cse110.successorator.app;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;
import java.util.stream.Collectors;

import edu.ucsd.cse110.successorator.app.databinding.ActivityMainBinding;
import edu.ucsd.cse110.successorator.app.ui.cardlist.PendingFragment;
import edu.ucsd.cse110.successorator.app.ui.cardlist.RecurringFragment;
import edu.ucsd.cse110.successorator.app.ui.cardlist.TodayFragment;
import edu.ucsd.cse110.successorator.app.ui.cardlist.TomorrowFragment;
import edu.ucsd.cse110.successorator.lib.domain.Goal;

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
        if (view.getId() == R.id.dropdown_button) {
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_views, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    // Handle menu item clicks here
                    int itemId = item.getItemId();
                    if (itemId == R.id.today_button) {
                        swapToToday();
                        return true;
                    } else if (itemId == R.id.tomorrow_button) {
                        swapToTomorrow();
                        return true;
                    } else if (itemId == R.id.pending_button) {
                        swapToPending();
                        return true;
                    } else if (itemId == R.id.recurring_button) {
                        swapToRecurring();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        else {
            popupMenu.getMenuInflater().inflate(R.menu.popup_menu_focus, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    // Handle menu item clicks here
                    int itemId = item.getItemId();
                    if (itemId == R.id.home_button) {
                        activityModel.filterByContext("Home");
                        return true;
                    } else if (itemId == R.id.work_button) {
                        activityModel.filterByContext("Work");
                        return true;
                    } else if (itemId == R.id.school_button) {
                        activityModel.filterByContext("School");
                        return true;
                    } else if (itemId == R.id.errands_button) {
                        activityModel.filterByContext("Errands");
                        return true;
                    } else if (itemId == R.id.cancel_button) {
                        activityModel.cancelFilter();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
        popupMenu.show();
    }
}