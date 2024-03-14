package edu.ucsd.cse110.successorator.app;

import android.os.Bundle;

import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.time.temporal.ChronoUnit;

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
        // if current fragment is not different, don't swap
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                instanceof TodayFragment) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .replace(R.id.fragment_container, TodayFragment.newInstance())
                .commit();
    }

    public void swapToTomorrow() {
        // if current fragment is not different, don't swap
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                instanceof TomorrowFragment) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                .replace(R.id.fragment_container, TomorrowFragment.newInstance())
                .commit();
    }

    public void swapToPending() {
        // if current fragment is not different, don't swap
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                instanceof PendingFragment) {
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
                .replace(R.id.fragment_container, PendingFragment.newInstance())
                .commit();
    }

    public void swapToRecurring() {
        // if current fragment is not different, don't swap
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                instanceof RecurringFragment) {
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
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
                String focusText = "";
                TextView contextIndicator = findViewById(R.id.contextIndicator);

                // Handle menu item clicks here
                // Also handles notifying user that they are in Focus mode.
                if (itemId == R.id.home_button) {
                    activityModel.filterByContext("Home");
                    focusText = "Focus Mode: Home";
                    color = ContextCompat.getColor(this, R.color.homeDotColor);
                } else if (itemId == R.id.work_button) {
                    activityModel.filterByContext("Work");
                    focusText = "Focus Mode: Work";
                    color = ContextCompat.getColor(this, R.color.workDotColor);
                } else if (itemId == R.id.school_button) {
                    activityModel.filterByContext("School");
                    focusText = "Focus Mode: School";
                    color = ContextCompat.getColor(this, R.color.schoolDotColor);
                } else if (itemId == R.id.errands_button) {
                    activityModel.filterByContext("Errands");
                    focusText = "Focus Mode: Errands";
                    color = ContextCompat.getColor(this, R.color.errandsDotColor);
                } else if (itemId == R.id.cancel_button) {
                    activityModel.cancelFilter();
                } else {
                    view.findViewById(R.id.contextIndicator).setVisibility(View.INVISIBLE);
                    return false;
                }

                view.findViewById(buttonId).setBackgroundColor(color);

                contextIndicator.setTextColor(color);
                contextIndicator.setText(focusText);
                contextIndicator.setVisibility(View.VISIBLE);
                fadeInAnimation(contextIndicator);
                return true;
            });
        }

        popupMenu.show();
    }

    private void fadeInAnimation(View view) {
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        view.startAnimation(fadeIn);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        var itemId = item.getItemId();
        if (itemId == R.id.action_bar_menu_move_views) {
            activityModel.buttonCount++;
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


