package edu.ucsd.cse110.successorator.app.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

// Source: https://developer.android.com/develop/ui/views/components/pickers#java
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface DatePickerListener {
        void onDateSelected(String date);
        void setDateTime(LocalDateTime date);
    }

    private DatePickerListener listener;

    public void setDatePickerListener(DatePickerListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        String selectedDate = DateFormat.getDateInstance().format(calendar.getTime());
        LocalDateTime date = LocalDateTime.of(year, month + 1, day, 0, 0);

        // Pass the selected date to the listener
        if (listener != null) {
            listener.onDateSelected(selectedDate);
            listener.setDateTime(date);
        }
    }
}
