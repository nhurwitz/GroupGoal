package com.cs121.groupgoal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * This activity was suppose to be a quick start tutorial guide that only appeared
 * when a user signed up for the first time; however it was decided not to be included in
 * the final release of the app, though the code is preserved here for potential future use.
 *
 */
public class StartGuideActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_guide);
		
		Button gotItButton = (Button) findViewById(R.id.GotIt_button);
		
		gotItButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(StartGuideActivity.this, MainActivity.class);
		        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_guide, menu);
		
		/**  IF NEEDED FOR MENU SELECTION
		  menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		          startActivity(new Intent(NotificationsActivity.this, SettingsActivity.class));
		          return true;
		        }
		      });

	**/
		return true;
		
	}
}
