package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.ParseException;

import com.cs121.groupgoal.R;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewGoal extends Activity {
	
	GoalPost goal;
	
	String goalName = "";
	String goalDescription = "";
	ParseGeoPoint locationMade = null;
	String goalLocation = "";
	ParseUser goalOwner = null;
	Date goalDateAndTime = null;
	List<ParseUser> attendees = null;
	
	boolean attending = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_view_goal);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setDisplayShowHomeEnabled(false);
	    
		Intent intent = getIntent();
		String parseId = intent.getStringExtra("goal_id");
		String ownerName = intent.getStringExtra("goal_owner");
	 
	    ParseQuery<GoalPost> postQuery = ParseQuery.getQuery(GoalPost.class);
	    
	    try {
	    	goal = postQuery.get(parseId);
	    } catch (com.parse.ParseException e) {
			Log.e("Goal Error", e.getMessage());
		}
		
	    
	    
	    if(goal != null) {
	    	attendees = (goal.getAttendees() == null) ? new ArrayList<ParseUser>() : goal.getAttendees();
	    	goalName = goal.getName().toString();
	    	goalDescription = goal.getDescription().toString();
	    	locationMade = goal.getLocation();
	    	goalLocation = goal.getEventLocation().toString();
	    	goalDateAndTime = goal.getDate();
	    }	
		
		TextView textView = (TextView) findViewById(R.id.goal_details_title);
		textView.setText(goalName);
		
		textView = (TextView) findViewById(R.id.goal_details_description);
		textView.setText(goalDescription);
		
		textView = (TextView) findViewById(R.id.goal_details_location);
		textView.setText(goalLocation);
		
		textView = (TextView) findViewById(R.id.goal_details_date);
		textView.setText(goalDateAndTime.toString());
		
		textView = (TextView) findViewById(R.id.goal_details_owner);
		String[] goalOwnerFirstLast = ownerName.split("\\^");
		textView.setText(goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
		
		Button attendButton = (Button) findViewById(R.id.join_goal_button);
		if(attendees.contains(ParseUser.getCurrentUser())) {
			attending = true;
			setAttendingBox(attending);
		}
		
		attendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!attending) {
					attendees.add(ParseUser.getCurrentUser());
					attending = true;
					setAttendingBox(attending);
				} else {
					attendees.remove(ParseUser.getCurrentUser());
					attending = false;
					setAttendingBox(attending);
				}
				goal.setAttendees(attendees);
			}
		});
		Button viewComments = (Button) findViewById(R.id.view_comments_button);
		
		viewComments.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ViewGoal.this, CommentActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
			}
			
			
		});
		Button viewAttendees = (Button) findViewById(R.id.view_attendees_button);
		
		viewAttendees.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ViewGoal.this, ViewAttendeesActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
			}
			
			
		});

	}
	
	public void setAttendingBox(boolean attending) {
		Button attendButton = (Button) findViewById(R.id.join_goal_button);
		if(attending) {
			attendButton.setText("Joined!");
		} else {
			attendButton.setText("Join?");
		}
	}
}
