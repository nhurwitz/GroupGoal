package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyFriendsActivity extends Activity {

	ParseUser user = ParseUser.getCurrentUser();
	EditText searchedFriend;
	List<ParseUser> lst;
	List<ParseUser> currentLst = (List<ParseUser>) user.get("friendsList");
	TextView friendsListView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_friends);
		
		
		searchedFriend = (EditText) findViewById(R.id.searchedFriend);
		friendsListView = (TextView) findViewById(R.id.friends_list);
		
		Button AddFriendsButton = (Button) findViewById(R.id.addFriendsButton);
		AddFriendsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("onclick","");
				searchAndAddFriends();
			}
		});
		
		displayFriends();
	}
	
	
	public void searchAndAddFriends(){
		Log.d("searching!",searchedFriend.getText().toString());
		String name = searchedFriend.getText().toString();
		//If no name has been entered
		if(name==""){
			Log.d("No string","error!");
		}
		
		else{
			//Log.d("begin search","");
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("username",name);
			query.findInBackground(new FindCallback<ParseUser>() {
			  public void done(List<ParseUser> objects, ParseException e) {
			    if (e == null) {
			    	Log.d("query complete","");
			    	if(!objects.isEmpty()){
			    		String foundName = 	objects.get(0).getUsername();
			    		
			    		Log.d(String.valueOf(currentLst.contains(objects.get(0))),foundName);
			    		
			    		if(!currentLst.contains(objects.get(0))){
			    			currentLst.add(objects.get(0));
			    			user.put("friendsList", currentLst);
			    			user.saveInBackground();
			    			Log.d("Added Friend",foundName);
			    			displayFriends();
			    		}
			    	}
			    	else{
			    		Log.d("No user exists","");
			    	}
			    	
			    } else {
			    	Log.d("something went wrong finding user","");
			    	}
			  }
			});
		}
}
	
	
	public void displayFriends(){
	
		String friends = "Friends: ";
		
		
		if(currentLst.get(0)!=null){
				Log.d("display friends","inList");
				for (int i=0;i<currentLst.size();i++){
				//String nextFriend =  currentLst.get(i).getUsername();
				//friends = new String (friends+" "+nextFriend);
				Log.d("adding to string", friends);
				}
			}
		
		
		
		friendsListView.setText(friends);
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
	    menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	        public boolean onMenuItemClick(MenuItem item) {
	          startActivity(new Intent(MyFriendsActivity.this, SettingsActivity.class));
	          return true;
	        }
	      });
	      
	      
	      //Add the My Profile Option to the Menu-------------------RD
	        menu.findItem(R.id.action_my_profile).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	        public boolean onMenuItemClick(MenuItem item) {
	      	  Intent amp = new Intent(MyFriendsActivity.this, UserProfileActivity.class);
	      	  //amp.putExtra("user", User Object*)
	          startActivity(amp);
	          return true;
	        }
	      });
	        
	        menu.findItem(R.id.action_notifications).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	 // System.out.println("Inside OnMenuItemClick");
		      	  Intent amp = new Intent(MyFriendsActivity.this, NotificationsActivity.class);
		      	  //amp.putExtra("user", User Object*)
		          startActivity(amp);
		          return true;
		        }
		      });
	        
	        menu.findItem(R.id.action_my_friends).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	  Intent amp = new Intent(MyFriendsActivity.this, MyFriendsActivity.class);
		          startActivity(amp);
		          return true;
		        }
		      });
	        
	        menu.findItem(R.id.action_home).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	 // System.out.println("Inside OnMenuItemClick");
		      	  Intent amp = new Intent(MyFriendsActivity.this, MainActivity.class);
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