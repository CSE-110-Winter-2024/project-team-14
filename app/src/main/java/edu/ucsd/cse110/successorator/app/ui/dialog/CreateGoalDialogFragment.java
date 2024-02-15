package edu.ucsd.cse110.successorator.app.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import edu.ucsd.cse110.successorator.app.MainViewModel;
import edu.ucsd.cse110.successorator.app.databinding.DialogCreateBinding;
import edu.ucsd.cse110.successorator.app.databinding.DialogCreateBinding;
import edu.ucsd.cse110.successorator.lib.domain.Goal;
import edu.ucsd.cse110.successorator.lib.domain.GoalRepository;

public class CreateGoalDialogFragment extends DialogFragment{
    private MainViewModel activityModel;
    private DialogCreateBinding view;
    CreateGoalDialogFragment(){

    }

    public static CreateGoalDialogFragment newInstance(){
        var fragment = new CreateGoalDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState){
        this.view = DialogCreateBinding.inflate((getLayoutInflater()));

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

        var goal = new Goal(null, front,false,-1);
        activityModel.addGoal(goal);
        //view.notify();

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