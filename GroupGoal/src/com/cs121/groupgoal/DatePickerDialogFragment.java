package com.cs121.groupgoal;

import java.util.Calendar;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;

/**
 * This activity is used when a user posts a goal and needs to choose a date from the pop-out calendar
 *
 */

public class DatePickerDialogFragment extends DialogFragment { //used to pick dates when posting a Goal

    private OnDateSetListener mDateSetListener;

    public DatePickerDialogFragment() {
       
    }

    public DatePickerDialogFragment(OnDateSetListener callback) {
        mDateSetListener = (OnDateSetListener) callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();

        return new DatePickerDialog(getActivity(),
                mDateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

}
