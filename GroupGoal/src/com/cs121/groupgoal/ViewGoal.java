package com.cs121.groupgoal;

import com.cs121.groupgoal.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewGoal extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_view_goal);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String goalName = extras.getString("goal_name");
		String goalDescription = extras.getString("goal_description");
		String goalLocation = extras.getString("goal_location");
		String goalOwner = extras.getString("goal_owner");
		String goalDateAndTime = extras.getString("goal_date_time");
		
		TextView textView = (TextView) findViewById(R.id.goal_details_title);
		textView.setText(goalName);
		
		textView = (TextView) findViewById(R.id.goal_details_description);
		textView.setText(goalDescription);
		
		textView = (TextView) findViewById(R.id.goal_details_location);
		textView.setText(goalLocation);
		
		textView = (TextView) findViewById(R.id.goal_details_date);
		textView.setText(goalDateAndTime);
		
		//textView = (TextView) findViewById(R.id.goal_details_owner);
		//String[] goalOwnerFirstLast = goalOwner.split("\\^");
		//textView.setText(goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
		
		
		
		Button attendButton = (Button) findViewById(R.id.join_goal_button);
		
		attendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ViewGoal.this, MainActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
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
}
