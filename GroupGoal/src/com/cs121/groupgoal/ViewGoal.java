package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.ParseException;

import com.cs121.groupgoal.R;
import com.cs121.groupgoal.GoalPost.Category;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * This activity displays all the relevant information of the goal,
 * and gives the user the option to join or unjoin the goal.
 *
 */
public class ViewGoal extends Activity {
	
	GoalPost goal;
	
	String goalName = "";
	String goalDescription = "";
	String ownerName = "";
	ParseGeoPoint locationMade = null;
	String goalLocation = "";
	ParseUser goalOwner = null;
	Date goalDateAndTime = null;
	List<String> attendees = null;
	
	ParseUser user = ParseUser.getCurrentUser();
	String parseID;
	
	boolean attending = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_view_goal);
	    getActionBar().setDisplayShowTitleEnabled(false);

		Bundle bundle = getIntent().getExtras();
		String parseId = bundle.getString("goal_id");
		parseID = parseId; 
			 
	    ParseQuery<GoalPost> postQuery = ParseQuery.getQuery(GoalPost.class);
	    
	    try {
	    	goal = postQuery.get(parseId);
	    } catch (com.parse.ParseException e) {
			Log.e("Goal Error", e.getMessage());
		}

	    if(bundle.size() > 1) {
			ownerName = bundle.getString("goal_owner");
		} else {
			ownerName = goal.getOwnerId();
			try {
				ownerName = ParseQuery.getQuery(ParseUser.class)
						.get(ownerName).get("fullName").toString();
			} catch (com.parse.ParseException e) {
				Log.e("Goal Error", e.getMessage());
			}
		}

	    if(goal != null) {
	    	attendees = goal.getAttendees();
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
		if(attendees.contains(ParseUser.getCurrentUser().getObjectId())) {
			attending = true;
			setAttendingBox(attending);
		}
		
		attendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(!attending) {
					attendees.add(ParseUser.getCurrentUser().getObjectId());
					attending = true;
					setAttendingBox(attending);
					String goalId = goal.getObjectId();
					
					user.addAllUnique("myGoals", Arrays.asList(goalId));
					user.saveInBackground();
				} else {
					attendees.remove(ParseUser.getCurrentUser().getObjectId());
					attending = false;
					setAttendingBox(attending);
				}
				goal.setAttendees(attendees);
				goal.saveInBackground();
			}
		});
		
	}
	
	public void setAttendingBox(boolean attending) { //switches text of button from join to joined depending on state
		Button attendButton = (Button) findViewById(R.id.join_goal_button);
		if(attending) {
			attendButton.setText("Joined!");
		} else {
			attendButton.setText("Join?");
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent intent = new Intent(ViewGoal.this, MainActivity.class);
				startActivity(intent);
				return true;
		}
		return true;
	}
	
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.view_goal, menu);
	    getActionBar().setDisplayShowTitleEnabled(false);

	    //Add the My Profile Option to the Menu
	      menu.findItem(R.id.action_invite).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	      public boolean onMenuItemClick(MenuItem item) {
	    	  Intent intent = new Intent(ViewGoal.this, InviteActivity.class)
				.putExtra("goal_id", goal.getObjectId())
				.putExtra("goal_owner", ownerName);
	        startActivity(intent);
	        return true;
	      }
	    });
	      
	      
	      menu.findItem(R.id.action_attendees).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		      public boolean onMenuItemClick(MenuItem item) {
		    	  Intent intent = new Intent(ViewGoal.this, ViewAttendeesActivity.class)
					.putExtra("goal_id", goal.getObjectId())
					.putExtra("goal_owner", ownerName);
		        startActivity(intent);
		        return true;
		      }
		    });     
	      
	      menu.findItem(R.id.action_comment).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		      public boolean onMenuItemClick(MenuItem item) {
		    	  Intent intent = new Intent(ViewGoal.this, CommentActivity.class)
					.putExtra("goalObjectId", goal.getObjectId())
					.putExtra("goal_owner", ownerName);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
		        return true;
		      }
		    });     
	          
	    return true;
	  }
}
