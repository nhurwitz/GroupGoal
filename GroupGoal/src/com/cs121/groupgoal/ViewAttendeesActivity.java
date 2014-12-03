package com.cs121.groupgoal;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.ParseQuery;
import com.parse.ParseUser;
public class ViewAttendeesActivity extends Activity {

	GoalPost goal;
	ListView attendeeView;
	HashMap<String, String> userProfiles;
	HashMap<String, String> reverseLookup;
	ParseUser attendee;
	
	private String parseId;
	private String ownerName;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_attendees);
	    getActionBar().setDisplayShowTitleEnabled(false);
		
		Intent intent = getIntent();
		parseId = intent.getStringExtra("goal_id");
		ownerName = intent.getStringExtra("goal_owner");
		userProfiles = new HashMap<String,String>();
		reverseLookup = new HashMap<String, String>();
		
		ParseQuery<GoalPost> postQuery = ParseQuery.getQuery(GoalPost.class);
	    
	    try {
	    	goal = postQuery.get(parseId);
	    } catch (com.parse.ParseException e) {
			Log.e("Goal Error", e.getMessage());
		}
	    
	    ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
	    
	    List<String> attendeeIds = goal.getAttendees();
	    for(String id : attendeeIds) {
	    	ParseUser user;
	    	try {
	    		user = userQuery.get(id);
		    	userProfiles.put(id, adaptFullName(user.get("fullName").toString()));
		    	reverseLookup.put(adaptFullName(user.get("fullName").toString()), id);
	    	} catch (com.parse.ParseException e) {
				Log.e("Goal Error", e.getMessage());
			}
	    }
	    
	    ArrayAdapter<String> attendeesAdapter = 
	    		new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
	    
	    attendeesAdapter.addAll(userProfiles.values());
	    
		attendeeView = (ListView) findViewById(R.id.attendee_list_view);
		attendeeView.setAdapter(attendeesAdapter);
		attendeeView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?>adapter,View v, int position, long id) {
				String user = (String) adapter.getItemAtPosition(position);
				String userId = reverseLookup.get(user);
				
				Intent intent = new Intent(ViewAttendeesActivity.this,
						UserProfileActivity.class)
					.putExtra("userId", userId);
			
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(ViewAttendeesActivity.this, ViewGoal.class)
			.putExtra("goal_id", parseId)
			.putExtra("goal_owner", ownerName);
	
		startActivity(intent);
		return true;
	}
	
	private String adaptFullName(String fullName) {
		String[] goalOwnerFirstLast = fullName.split("\\^");
		return (goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
	}


}
