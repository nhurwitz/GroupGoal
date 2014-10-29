package com.cs121.groupgoal;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TextView;

//adding some new comment shit

//some new comments
public class UserProfileActivity extends Activity {

	ParseUser user;
	TextView userNameText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		Bundle b = getIntent().getExtras();
		if(b!=null){
			user = (ParseUser) b.get("user");
			}
		
		
		if(user!=null){
			userNameText = (TextView)findViewById(R.id.user_name_text);
			userNameText.setText(user.getString("name"));
		}
		else{
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		
	    menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	        public boolean onMenuItemClick(MenuItem item) {
	          startActivity(new Intent(UserProfileActivity.this, SettingsActivity.class));
	          return true;
	        }
	      });
	      
	      
	      //Add the My Profile Option to the Menu-------------------RD
	        menu.findItem(R.id.action_my_profile).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	        public boolean onMenuItemClick(MenuItem item) {
	      	  System.out.println("Inside OnMenuItemClick");
	      	  Intent amp = new Intent(UserProfileActivity.this, UserProfileActivity.class);
	      	  //amp.putExtra("user", User Object*)
	          startActivity(amp);
	          return true;
	        }
	      });
	        
	        menu.findItem(R.id.action_notifications).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	 // System.out.println("Inside OnMenuItemClick");
		      	  Intent amp = new Intent(UserProfileActivity.this, NotificationsActivity.class);
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
		
		//---------------------------------RD
		else if (id == R.id.action_my_profile) {
			System.out.println("CLICKED!");
			return true;
		}
		//--------------------------
		
		return super.onOptionsItemSelected(item);
	}
}
