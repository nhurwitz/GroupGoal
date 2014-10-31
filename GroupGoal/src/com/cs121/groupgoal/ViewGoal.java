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

	//private static final GoalPost post;
	
	//goalpost instance variable mockups
	private String name = "joanna";
	private String description = "this is a long description about a goal";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_view_goal);
		
		
		Button attendButton = (Button) findViewById(R.id.join_goal_button);
		
		attendButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ViewGoal.this, MainActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
			}
		});
	}
}
