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

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.DialogCreateBinding;
import edu.ucsd.cse110.successorator.app.databinding.DialogCreateBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

public class CreateGoalDialogFragment extends DialogFragment{
    private MainViewModel activityModel;
    private DialogCreateBinding view;
    private String context;

    private String typeOfGoal = "one_time";

    CreateGoalDialogFragment(){

    }

    public static CreateGoalDialogFragment newInstance(){
        var fragment = new CreateGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void assignContext(@NonNull String context) {
        this.context = context;
    }

    public void assignType(@NonNull String typeOfGoal) {this.typeOfGoal = typeOfGoal;}

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = DialogCreateBinding.inflate((getLayoutInflater()));

        view.homeButton.setOnClickListener(v -> {
            assignContext("Home");
        });
        view.workButton.setOnClickListener(v -> {
            assignContext("Work");
        });
        view.schoolButton.setOnClickListener(v -> {
            assignContext("School");
        });
        view.errandsButton.setOnClickListener(v -> {
            assignContext("Errands");
        });

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
            context = "Home";
        }
        var goal = new Goal(null, front,false,-1, context, typeOfGoal, LocalDateTime.now().toString());
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