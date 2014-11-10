package com.cs121.groupgoal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cs121.groupgoal.GoalPost.Category;
import com.cs121.groupgoal.R;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */
@SuppressLint("SimpleDateFormat")
public class PostActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener, 
	TimePickerDialog.OnTimeSetListener {
	
  // UI references.
  private EditText goalTitleView;
  private EditText goalDescriptionView;
  private EditText goalLocationView;
  
  private TextView goalDateView;
  private TextView goalTimeView;
  
  private Button createButton;
  private ParseGeoPoint geoPoint;
  
  private CheckBox isPrivateCheckbox;
  private Spinner categoryDropdown;

  private Date date;
  private SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM-dd-yyyy");
  private SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("hh:mm a");
  private SimpleDateFormat DATE_AND_TIME_FORMATTER = new SimpleDateFormat("MM-dd-yyyy hh:mm a");

  @SuppressLint("DefaultLocale")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_post);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setDisplayShowHomeEnabled(false);


    Intent intent = getIntent();
    
    date = new Date();
    
    Location location = intent.getParcelableExtra(Application.INTENT_EXTRA_LOCATION);
    geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
    
    categoryDropdown = (Spinner)findViewById(R.id.goal_category_spinner);
    String[] items = new String[GoalPost.Category.values().length+1];
    items[0] = "Category";
    for(int i = 1; i < items.length; i++) {
    	items[i] = GoalPost.Category.values()[i-1].toString().toLowerCase();
    }
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
    categoryDropdown.setAdapter(adapter);

    goalTitleView = (EditText) findViewById(R.id.goal_title);
    goalDescriptionView = (EditText) findViewById(R.id.goal_description);
    goalLocationView = (EditText) findViewById(R.id.goal_location);
    goalDateView = (TextView) findViewById(R.id.goal_date);
    goalTimeView = (TextView) findViewById(R.id.goal_time);
    isPrivateCheckbox = (CheckBox) findViewById(R.id.goal_checkbox_private);
    createButton = (Button) findViewById(R.id.submit_goal_button);
    
    createButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String goalTitle = goalTitleView.getText().toString();
			String goalDescription = goalDescriptionView.getText().toString();
			String goalLocation = goalLocationView.getText().toString();
			String goalDate = goalDateView.getText().toString();
			String goalTime = goalTimeView.getText().toString();
			boolean goalPrivate = isPrivateCheckbox.isChecked();
			GoalPost.Category goalCategory = GoalPost.Category.valueOf(
					categoryDropdown.getSelectedItem().toString().toUpperCase());
			
			Date scheduledDate = parseDate(goalDate + " " + goalTime);
			
			if(titleValid() && descriptionValid() && dateAndTimeValid()) {
		
				GoalPost goal = new GoalPost();
				goal.setName(goalTitle);
				goal.setDescription(goalDescription);
				goal.setOwner(ParseUser.getCurrentUser());
				goal.setDate(scheduledDate);
				goal.setEventLocation(goalLocation);
				goal.setLocation(geoPoint);
				goal.setPrivate(goalPrivate);
				goal.setCategory(goalCategory);
				
				
				goal.saveInBackground(new SaveCallback() {
			      @Override
			      public void done(ParseException e) {
			        finish();
			      }
			    });
				
				Toast.makeText(getApplicationContext(), "Goal Successfully Created", 
						Toast.LENGTH_LONG).show();
				
				Intent i = new Intent(PostActivity.this, MainActivity.class);
				startActivity(i);
			}
		}
	});
  }

  
  public void showDatePickerDialog(View v) {
      FragmentTransaction ft = getFragmentManager().beginTransaction();
      DialogFragment newFragment = new DatePickerDialogFragment(PostActivity.this);
      newFragment.show(ft, "date_dialog");
  }

  public void showTimePickerDialog(View v) {
      FragmentTransaction ft = getFragmentManager().beginTransaction();
      DialogFragment newFragment = new TimePickerDialogFragment(PostActivity.this);
      newFragment.show(ft, "time_dialog");
  }

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		 Calendar cal = Calendar.getInstance();
	     cal.set(Calendar.HOUR, hourOfDay);
	     cal.set(Calendar.MINUTE, minute);
	     goalTimeView.setText(TIME_FORMATTER.format(cal.getTime()));
	     Log.i("The goal is scheduled for ", TIME_FORMATTER.format(cal.getTime()));	
	}
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		 Calendar cal = new GregorianCalendar(year, monthOfYear, dayOfMonth);
	     goalDateView.setText(DATE_FORMATTER.format(cal.getTime()));
	     Log.e("The goal is scheduled for ", DATE_FORMATTER.format(cal.getTime()));		
	}
	
	private Date parseDate(String dateAndTime) {
		  Date date = null;
	      try {
			date = DATE_AND_TIME_FORMATTER.parse(dateAndTime);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			Log.e("Parse Expiration Date Error", e.getMessage());
		}
	      return date;
	}
	
	private boolean titleValid() {
		if(goalTitleView.getText().toString().length() == 0) {
			goalTitleView.setError("Title is a required field.");
			return false;
		} else {
			goalTitleView.setError(null);
			return true;
		}
	}
	
	private boolean descriptionValid() {
		if(goalDescriptionView.getText().toString().length() == 0) {
			goalDescriptionView.setError("Description is a required field.");
			return false;
		} else {
			goalDescriptionView.setError(null);
			return true;
		}
	}
	
	private boolean dateAndTimeValid() {
		if(goalDateView.getText().toString().length() == 0 ||
				goalTimeView.getText().toString().length() == 0) {
			goalDateView.setError("Date and Time are required fields.");
			return false;
		} else {
			goalDateView.setError(null);
			goalTimeView.setError(null);
			return true;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
