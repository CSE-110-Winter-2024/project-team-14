package edu.ucsd.cse110.successorator.app.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.DialogCreateBinding;
import edu.ucsd.cse110.successorator.app.ui.cardlist.TomorrowFragment;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
public class CreateGoalDialogFragment extends DialogFragment{
    private MainViewModel activityModel;
    private DialogCreateBinding view;
    private String context = "Home";
    private String recurrence = "one_time";

    CreateGoalDialogFragment(){

    }

    public static CreateGoalDialogFragment newInstance(){
        var fragment = new CreateGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void assignRecurrence(@NonNull String recurrence) {
        this.recurrence = recurrence;
    }

    public void assignContext(@NonNull String context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = DialogCreateBinding.inflate((getLayoutInflater()));

        view.homeButton.setOnClickListener(v -> assignContext("Home"));
        view.workButton.setOnClickListener(v -> assignContext("Work"));
        view.schoolButton.setOnClickListener(v -> assignContext("School"));
        view.errandsButton.setOnClickListener(v -> assignContext("Errands"));

        view.onetimeButton.setOnClickListener(v -> assignRecurrence("one_time"));
        view.dailyButton.setOnClickListener(v -> assignRecurrence("daily"));
        view.weeklyButton.setOnClickListener(v -> assignRecurrence("weekly"));
        view.monthlyButton.setOnClickListener(v -> assignRecurrence("monthly"));
        view.yearlyButton.setOnClickListener(v -> assignRecurrence("yearly"));

        if (getParentFragment() instanceof TomorrowFragment) {
            activityModel.getCurrentDateTime().observe((dateTime) -> {
                LocalDateTime tomorrow = dateTime.plusDays(1);
                var weeklyFormatter = DateTimeFormatter.ofPattern("'weekly on 'E", Locale.getDefault());
                var monthlyFormatter = DateTimeFormatter.ofPattern("'monthly 'E", Locale.getDefault());
                var yearlyFormatter = DateTimeFormatter.ofPattern("'yearly on 'M/d", Locale.getDefault());

                view.weeklyButton.setText(tomorrow.format(weeklyFormatter));
                view.monthlyButton.setText(tomorrow.format(monthlyFormatter));
                view.yearlyButton.setText(tomorrow.format(yearlyFormatter));
            });
        } else {
            activityModel.getCurrentDateTime().observe((dateTime) -> {
                var weeklyFormatter = DateTimeFormatter.ofPattern("'weekly on 'E", Locale.getDefault());
                var monthylFormatter = DateTimeFormatter.ofPattern("'monthly 'E", Locale.getDefault());
                var yearlyFormatter = DateTimeFormatter.ofPattern("'yearly on 'M/d", Locale.getDefault());

                view.weeklyButton.setText(dateTime.format(weeklyFormatter));
                view.monthlyButton.setText(dateTime.format(monthylFormatter));
                view.yearlyButton.setText(dateTime.format(yearlyFormatter));
            });
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle("Most Important Task")
                .setMessage("Please provide the Most Important Task.")
                .setView(view.getRoot())
                .setPositiveButton("Create", this::onPositiveButtonClick)
                .setNegativeButton("Cancel", this::onNegativeButtonClick)
                .create();
    }

    private void onPositiveButtonClick(DialogInterface dialog, int which){
        var front = view.goalFrontEditText.getText().toString();

        if(front.length() == 0 || front.length() > 30){
            dialog.dismiss();
            return;
        }

        if (context == null) {
            return;
        }

        var goal = new Goal(null, front,false,-1, context, LocalDateTime.now(), recurrence);
        if (getParentFragment() instanceof TomorrowFragment) {
            goal = new Goal(null, front,false,-1, context, LocalDateTime.now().plusDays(1), recurrence);
        }
        activityModel.append(goal);

        dialog.dismiss();
    }

    private void onNegativeButtonClick(DialogInterface dialog, int which){
        dialog.cancel();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        var modelOwner = requireActivity();
        var modelFactory = ViewModelProvider.Factory.from(MainViewModel.initializer);
        var modelProvider = new ViewModelProvider(modelOwner, modelFactory);
        this.activityModel = modelProvider.get(MainViewModel.class);
    }

}