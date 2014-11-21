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
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_attendees);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		
		Intent intent = getIntent();
		String parseId = intent.getStringExtra("goal_id");
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
	    
		attendeeView = (ListView) findViewById(R.id.attendee_list_view);
		attendeeView.setAdapter(attendeesAdapter);
		attendeeView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?>adapter,View v, int position, long id) {
				String user = (String) adapter.getItemAtPosition(position);
				String userId = reverseLookup.get(user);
				
				Intent intent = new Intent(ViewAttendeesActivity.this,
						UserProfileActivity.class)
					.putExtra("user_id", userId);
			
				startActivity(intent);
			}
		});
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
	
	private String adaptFullName(String fullName) {
		String[] goalOwnerFirstLast = fullName.split("\\^");
		return (goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
	}


}
