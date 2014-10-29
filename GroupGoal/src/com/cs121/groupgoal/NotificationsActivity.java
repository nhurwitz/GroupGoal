package com.cs121.groupgoal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class NotificationsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notifications, menu);
		
		  menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		          startActivity(new Intent(NotificationsActivity.this, SettingsActivity.class));
		          return true;
		        }
		      });
		      
		      
		      //Add the My Profile Option to the Menu-------------------RD
		        menu.findItem(R.id.action_my_profile).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	  System.out.println("Inside OnMenuItemClick");
		      	  Intent amp = new Intent(NotificationsActivity.this, UserProfileActivity.class);
		      	  //amp.putExtra("user", User Object*)
		          startActivity(amp);
		          return true;
		        }
		      });
		        
		        menu.findItem(R.id.action_notifications).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			        public boolean onMenuItemClick(MenuItem item) {
			      	 // System.out.println("Inside OnMenuItemClick");
			      	  Intent amp = new Intent(NotificationsActivity.this, NotificationsActivity.class);
			      	  //amp.putExtra("user", User Object*)
			          startActivity(amp);
			          return true;
			        }
			      });
		return true;
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
