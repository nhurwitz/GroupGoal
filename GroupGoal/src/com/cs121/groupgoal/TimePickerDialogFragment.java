package com.cs121.groupgoal;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Used for the pop-out time picker that is displayed when a user is posting a new goal
 * and entering relevant information.
 *
 */
public class TimePickerDialogFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    public TimePickerDialogFragment() {
       
    }

    public TimePickerDialogFragment(TimePickerDialog.OnTimeSetListener callback) {
        mTimeSetListener = (TimePickerDialog.OnTimeSetListener) callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();

        return new TimePickerDialog(getActivity(),
                mTimeSetListener, cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(getActivity()));
    }
}