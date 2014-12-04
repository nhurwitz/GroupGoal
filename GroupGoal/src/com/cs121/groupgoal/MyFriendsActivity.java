package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyFriendsActivity extends Activity {

	ParseUser user = ParseUser.getCurrentUser();
	EditText searchedFriend;
	List<ParseUser> lst;
	List<ParseUser> currentLst = (List<ParseUser>) user.get("friendsList");
	List<String> currentFriends = (List<String>) user.get("friendsList");
	TextView testListView;
	ListView friendsView;
	
	HashMap<String, String> userProfiles;
	HashMap<String, String> reverseLookup;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_friends);
		
		userProfiles = new HashMap<String,String>();
		reverseLookup = new HashMap<String, String>();
		
		searchedFriend = (EditText) findViewById(R.id.searchedFriend);
		friendsView = (ListView) findViewById(R.id.friends_list_view);
		testListView = (TextView) findViewById(R.id.textView3);
		
		Button AddFriendsButton = (Button) findViewById(R.id.addFriendsButton);
		AddFriendsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				searchAndAddFriends();
			}
		});
		
		displayFriends();
	}
	
	
	public void searchAndAddFriends(){
		String name = searchedFriend.getText().toString();
		//If no name has been entered
		if(name!=""){
			Log.d("searching for..",name);
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("username",name);
			Log.d("check 2",name);
			query.findInBackground(new FindCallback<ParseUser>() {	
			  public void done(List<ParseUser> objects, ParseException e) {
				  Log.d("query complete","");
				  if (e == null) {
			    	Log.d("e is null","");
			    	if(!objects.isEmpty()){
			    		
			    		ParseUser friend = objects.get(0);
			    		String foundName = 	friend.getObjectId(); 		
			    		Log.d("found him!",foundName);
			    		//add the friend to the array
			    		if(!currentFriends.contains(foundName)){
			    			currentFriends.add(foundName);
			    			user.put("friendsList", currentFriends);
			    			user.saveInBackground();
			    			
			    			//add to friend's friends list
			    			
			    			@SuppressWarnings("unchecked")
							ArrayList<String> fsList = (ArrayList<String>) friend.get("friendsList");
			    			if(fsList == null) {
			    				fsList = new ArrayList<String>();
			    			}
			    			fsList.add(user.getObjectId());
			    			friend.put("friendsList", fsList);
			    			
			    			searchedFriend.setText("");
			    			searchedFriend.setHint("Add another Friend!");
			    			displayFriends();
			    		}
			    		
			    		

			    	}
			    	
			    } 
			  }
			});
		}
	}
	

	public void displayFriends(){
		
		//with list view
		ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
		
		String theId = "";
		Log.d("for loop","");
		for(String id : currentFriends) {
	    	try {
	    		ParseUser user = userQuery.get(id);
		    	userProfiles.put(id, adaptFullName(user.get("fullName").toString()));
		    	theId = id;
		    	reverseLookup.put(adaptFullName(user.get("fullName").toString()), id);
	    	} catch (com.parse.ParseException e) {
				Log.e("Friend Error", e.getMessage());
			}
		
		}
		
		Log.d(userProfiles.get(theId),"THIS IS THE USER!!!");
		
		ArrayAdapter<String> attendeesAdapter = 
	    		new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		
		  attendeesAdapter.addAll(userProfiles.values());
		    //Log.d(userProfiles.,"");
			friendsView.setAdapter(attendeesAdapter);


		
	  
		

		friendsView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?>adapter,View v, int position, long id) {
				String user = (String) adapter.getItemAtPosition(position);
				String userId = reverseLookup.get(user);
				
				Intent intent = new Intent(MyFriendsActivity.this,
						UserProfileActivity.class)
					.putExtra("userId", userId);
			
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
	    getActionBar().setDisplayShowTitleEnabled(false);
	
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
	        
	        menu.findItem(R.id.action_post).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	            public boolean onMenuItemClick(MenuItem item) {
	  	              Intent intent = new Intent(MyFriendsActivity.this, PostActivity.class);
	  	              
	  	              startActivity(intent);
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
		if (id == R.id.action_my_friends) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private String adaptFullName(String fullName) {
		String[] goalOwnerFirstLast = fullName.split("\\^");
		Log.d("the name: ",(goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]));
		return (goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
	}
	
	
	
}
