package com.cs121.groupgoal;
	
	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.QueryFactory;
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
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * This activity contains the user's personal information, and allows them
 * to write a personal signature. It also fetches all the past goals that the
 * user has completed and all the goals created by the user and displays them as list views.
 *
 */
	public class UserProfileActivity extends Activity {

		Bundle extras;
		ParseUser user;
		TextView userNameTextBox;
		Editable userProfileMessage;
		ArrayList<String> allGoals,pastGoals,madeGoals;
		private EditText searchedFriend;

		GoalPost goal;
		Date goalDate;
		
		  private String selectedPostObjectId;
		  
		  //adapters to display the listviews of Goals 
		  private ParseQueryAdapter<GoalPost> postsQueryAdapter2;
		  private ParseQueryAdapter<GoalPost> postsQueryAdapter3;

		  @Override
		protected void onCreate(Bundle savedInstanceState) {
			
			extractExtras();
			
			allGoals =  (ArrayList<String>) user.get("myGoals"); //arrayList that holds all the goals that the user has joined			
			pastGoals = new ArrayList<String>();
			madeGoals = (ArrayList<String>) user.get("createdGoals"); //arrayList that holds all the goals that the user has created

		
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user_profile);
			getActionBar().setDisplayShowTitleEnabled(false);
			
			displayProfile();
			
			
			Button EditButton = (Button) findViewById(R.id.edit_user_info_button);//allows user to edit their personal signature	
			EditButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					popUpEditBox();
				}
			});
			
			if(user!=ParseUser.getCurrentUser()){
				EditButton.setVisibility(View.INVISIBLE);
			}
			
			
			sortLists();
							
			//code to create listview for created Goals
		    ParseQueryAdapter.QueryFactory<GoalPost> factory2 =
			        new ParseQueryAdapter.QueryFactory<GoalPost>() {
			          public ParseQuery<GoalPost> create() {
			            ParseQuery<GoalPost> query = ParseQuery.getQuery("Posts");
			            query.whereContainedIn("objectId", madeGoals);
			            query.include("user");
			            query.orderByAscending("date");
			            return query;
			          }
			        };
			        

					    // Set up the query adapter for created goals
					    postsQueryAdapter2 = new ParseQueryAdapter<GoalPost>(this, factory2) {
					      @Override
					      public View getItemView(GoalPost post, View view, ViewGroup parent) {
					        if (view == null) {
					          view = View.inflate(getContext(), R.layout.anywall_post_item, null);
					        }
					        					        
					        String[] goalOwnerFirstLast = post.getOwner().get("fullName").toString().split("\\^");
					        int target = (Integer) post.getTargetGroupSize();
					        int current = (Integer) post.getCurrentGroupSize();
					        TextView contentView = (TextView) view.findViewById(R.id.content_view);
					        TextView usernameView = (TextView) view.findViewById(R.id.username_view);
					        TextView sizeView = (TextView) view.findViewById(R.id.goal_list_attending);
					        contentView.setText(post.getName());
					        usernameView.setText(goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
					        sizeView.setText(current + "/" + target);
					        return view;
					      }
					    };


					    // Attach the query adapter to the view
					    ListView listView2 = (ListView) findViewById(R.id.created_goals_listview);
						listView2.setAdapter(postsQueryAdapter2);

					    // Set up the handler for an item's selection
					    listView2.setOnItemClickListener(new OnItemClickListener() {
					      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					        final GoalPost item = postsQueryAdapter2.getItem(position);
					        selectedPostObjectId = item.getObjectId();

					        Intent intent = new Intent(UserProfileActivity.this, ViewGoal.class)
					        	.putExtra("goal_id", item.getObjectId())
					        	.putExtra("goal_owner", item.getOwner().get("fullName").toString());
					        
					        startActivity(intent);
					      }
					    });
					    
					  //code to create listview for past Goals
					    ParseQueryAdapter.QueryFactory<GoalPost> factory3 =
						        new ParseQueryAdapter.QueryFactory<GoalPost>() {
						          public ParseQuery<GoalPost> create() {
						            ParseQuery<GoalPost> query = ParseQuery.getQuery("Posts");
						            query.whereContainedIn("objectId", pastGoals);
						            query.include("user");
						            query.orderByAscending("date");
						            return query;
						          }
						        };

						    // Set up the query adapter for past goals
						    postsQueryAdapter3 = new ParseQueryAdapter<GoalPost>(this, factory3) {
						      @Override
						      public View getItemView(GoalPost post, View view, ViewGroup parent) {
						        if (view == null) {
						          view = View.inflate(getContext(), R.layout.anywall_post_item, null);
						        }
						        						        
						        String[] goalOwnerFirstLast = post.getOwner().get("fullName").toString().split("\\^");
						        int target = (Integer) post.getTargetGroupSize();
						        int current = (Integer) post.getCurrentGroupSize();
						        TextView contentView = (TextView) view.findViewById(R.id.content_view);
						        TextView usernameView = (TextView) view.findViewById(R.id.username_view);
						        TextView sizeView = (TextView) view.findViewById(R.id.goal_list_attending);
						        contentView.setText(post.getName());
						        usernameView.setText(goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
						        sizeView.setText(current + "/" + target);
						        return view;
						      }
						    };


						    // Attach the query adapter to the view
						    ListView listView3 = (ListView) findViewById(R.id.past_goals_listview);
							  listView3.setAdapter(postsQueryAdapter3);

						    // Set up the handler for an item's selection
						    listView3.setOnItemClickListener(new OnItemClickListener() {
						      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						        final GoalPost item = postsQueryAdapter3.getItem(position);
						        selectedPostObjectId = item.getObjectId();

						        Intent intent = new Intent(UserProfileActivity.this, ViewGoal.class)
						        	.putExtra("goal_id", item.getObjectId())
						        	.putExtra("goal_owner", item.getOwner().get("fullName").toString());
						        
						        startActivity(intent);
						      }
						    });

		}
		
		
		//extract the user name from the intent and set user using a ParseQuery
		private void extractExtras() {
			
			extras = getIntent().getExtras(); 
			
			
			//default to the current user
			user = ParseUser.getCurrentUser();

			//set user to other if passed in as an extra
			if(extras!=null){
				String id = extras.getString("userId");
				Log.d("the id is",id);
				
			try {
				user = ParseQuery.getQuery(ParseUser.class).get(id);
				Log.d("we got the user",id);
			} catch (ParseException e) {
				e.printStackTrace();
				
			}
			}
			
		}
		
		public void displayProfile() { //function to display general user info
			String userFullName = user.getString("fullName").toString();
			String[] firstLast = userFullName.split("\\^");
			
			TextView nameView = (TextView) findViewById(R.id.user_name_box);
			nameView.setTypeface(null, Typeface.BOLD);
		    nameView.setText(firstLast[0] + " " + firstLast[1]);
		       
		    String userDescription = user.getString("userDescription");  
		    TextView profileDescription = (TextView) findViewById (R.id.user_info_text);
			profileDescription.setTypeface(null, Typeface.BOLD_ITALIC);
		    profileDescription.setText(userDescription);
			
		}
		
		public void popUpEditBox(){
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
	
			alert.setTitle("Your Personalized Message");
			alert.setMessage("Message");
			
				// Set an EditText view to get user input 
				final EditText input = new EditText(this);
				alert.setView(input);
	
				alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Editable value = input.getText();
						resetUserSignature(value);
					}	
					});
	
				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});
			alert.show();
		
		}
		
		public void resetUserSignature(Editable e){  //is called when the user updates his/her signature
			String result = e.toString();			 // saves the attribute to Parse and updates the text field
			
			user.put("userDescription", result);
			user.saveInBackground();
			
			String newDescription = user.getString("userDescription");  
		    TextView profileDescription = (TextView) findViewById (R.id.user_info_text);
			profileDescription.setTypeface(null, Typeface.BOLD_ITALIC);
			profileDescription.setText(newDescription);
		}
		
		//function to sort the users joined goals between upcoming and past goals
		public void sortLists(){ 
			int i;
			Date todayDate = new Date();

			for (i=0; i < allGoals.size(); i++){
				
				ParseQuery<GoalPost> postQuery = ParseQuery.getQuery(GoalPost.class);
			    
			    try {
			    	String id = (String) allGoals.get(i);
			    	goal = postQuery.get(id);
			    	goalDate = goal.getDate();
			    } catch (com.parse.ParseException e) {
					Log.e("Goal Error", e.getMessage());
				}

			    if(todayDate.compareTo(goalDate) > 0){ //means the goal already passed
			    	pastGoals.add(allGoals.get(i));			    	
			    }
			}
		}
		
	
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			
		    menu.findItem(R.id.action_logout).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		    	public boolean onMenuItemClick(MenuItem item) {
		        	// Call the Parse log out method
		              ParseUser.logOut();
		              // Start and intent for the dispatch activity
		              Intent intent = new Intent(UserProfileActivity.this, DispatchActivity.class);
		              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		              startActivity(intent);
			          return true;
		          }
		      });

		        menu.findItem(R.id.action_my_profile).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		          return true;
		        }
		      });
		        
		        menu.findItem(R.id.action_my_friends).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			        public boolean onMenuItemClick(MenuItem item) {
			      	  Intent amp = new Intent(UserProfileActivity.this, MyFriendsActivity.class);
			          startActivity(amp);
			          return true;
			        }
			      });
		        
		        menu.findItem(R.id.action_upcoming_goals).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			        public boolean onMenuItemClick(MenuItem item) {
			      	Intent amp = new Intent(UserProfileActivity.this, UpcomingGoalsActivity.class);
			          startActivity(amp);
			          return true;
			        }
			      });
		        
		        menu.findItem(R.id.action_notifications).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			        public boolean onMenuItemClick(MenuItem item) {
			      	  Intent amp = new Intent(UserProfileActivity.this, NotificationsActivity.class);
			          startActivity(amp);
			          return true;
			        }
			      });
		        
		        menu.findItem(R.id.action_home).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			        public boolean onMenuItemClick(MenuItem item) {
			      	  Intent amp = new Intent(UserProfileActivity.this, MainActivity.class);
			          startActivity(amp);
			          return true;
			        }
			      });
		        
			return true;
		}
	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here
			int id = item.getItemId();
			if (id == R.id.action_my_profile) {
				return true;
			}
						
			return super.onOptionsItemSelected(item);
		}
	}
