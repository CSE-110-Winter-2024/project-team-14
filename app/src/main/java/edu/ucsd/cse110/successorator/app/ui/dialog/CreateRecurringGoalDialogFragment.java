package edu.ucsd.cse110.successorator.app.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.R;
import edu.ucsd.cse110.successorator.app.databinding.DialogRecurringBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
public class CreateRecurringGoalDialogFragment extends DialogFragment
        implements DatePickerFragment.DatePickerListener {
    private MainViewModel activityModel;
    private DialogRecurringBinding view;
    private String context = "Home";
    private String recurrence = "daily";
    private LocalDateTime selectedDate;

    CreateRecurringGoalDialogFragment(){

    }

    public static CreateRecurringGoalDialogFragment newInstance(){
        var fragment = new CreateRecurringGoalDialogFragment();
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

    @Override
    public void onDateSelected(String date) {
        view.selectedDateTextView.setVisibility(View.VISIBLE);
        view.selectedDateTextView.setText(date);
    }

    @Override
    public void setDateTime(LocalDateTime date) {
        selectedDate = date;
    }

    // Inside showDatePickerDialog method
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setDatePickerListener(this); // Set the listener
        newFragment.show(getParentFragmentManager(), "datePicker");
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = DialogRecurringBinding.inflate((getLayoutInflater()));

        view.homeButton.setOnClickListener(v -> assignContext("Home"));
        view.workButton.setOnClickListener(v -> assignContext("Work"));
        view.schoolButton.setOnClickListener(v -> assignContext("School"));
        view.errandsButton.setOnClickListener(v -> assignContext("Errands"));

        view.dailyButton.setOnClickListener(v -> assignRecurrence("daily"));
        view.weeklyButton.setOnClickListener(v -> assignRecurrence("weekly"));
        view.monthlyButton.setOnClickListener(v -> assignRecurrence("monthly"));
        view.yearlyButton.setOnClickListener(v -> assignRecurrence("yearly"));

        view.pickDateButton.setOnClickListener(v -> showDatePickerDialog());

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

        var goal = new Goal(null, front,false,-1, context, selectedDate, recurrence, false);
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