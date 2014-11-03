	package com.cs121.groupgoal;
	
	import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
	
	import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
	
	import java.util.ArrayList;
import java.util.List;
	
	//adding some new comment shit
	
	//some new comments
	public class UserProfileActivity extends Activity {
		ParseUser user= ParseUser.getCurrentUser();
		TextView userNameTextBox;
		Editable userProfileMessage;
		private EditText searchedFriend;
		
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user_profile);
			
			displayProfile();
			
			Button EditButton = (Button) findViewById(R.id.edit_user_info_button);		
			EditButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					popUpEditBox();
			      
				}
			});
			
			searchedFriend = (EditText) findViewById(R.id.searchedFriend);
			
			Button AddFriendsButton = (Button) findViewById(R.id.addFriendsButton);
			AddFriendsButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					searchAndAddFriends();
				}
			});
			
			displayLists();
		}
		
		public void displayProfile() { //function to display general user info
			String userFullName = user.getString("fullName");
			
			TextView nameView = (TextView) findViewById(R.id.user_name_box);
			nameView.setTypeface(null, Typeface.BOLD);
		    nameView.setText(userFullName);
		       
		    String userDescription = user.getString("userDescription");  
		    TextView profileDescription = (TextView) findViewById (R.id.user_info_text);
			  profileDescription.setTypeface(null, Typeface.BOLD_ITALIC);
		    	profileDescription.setText(userDescription);
			
		}
		
		public void displayLists(){ //function to display the three lists of the user's goals
			
			
		}
		
		//TO-DO (VICKY): need to update Parse value when user edits profile description
		public void popUpEditBox(){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
	
			alert.setTitle("Your Personalized Message");
			alert.setMessage("Message");
	
			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);
	
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			  final Editable value = input.getText();
			  TextView UserInfoDescription = (TextView) findViewById(R.id.user_info_text);
			  UserInfoDescription.setTypeface(null, Typeface.BOLD_ITALIC);
		      UserInfoDescription.setText("'"+value+"'");
		      userProfileMessage=value;

			  }
			
				});
	
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			  public void onClick(DialogInterface dialog, int whichButton) {
			    // Canceled.
			  }
			});
	
			alert.show();
		
		}
		
		
		public void searchAndAddFriends(){ //should be called when button is pushed to add friends (use searchedFriend attribute)
			
		}
		
		public void displayUpcomingGoals(){
			
		}
		public void displayPastAttendedGoals(){
			
		}
		public void displayPastCreatedGoals(){
			
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
