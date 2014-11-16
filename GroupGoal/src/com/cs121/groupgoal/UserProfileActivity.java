package com.cs121.groupgoal;
	
	
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
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
	
	public class UserProfileActivity extends Activity {
		ParseUser user= ParseUser.getCurrentUser();
		TextView userNameTextBox;
		Editable userProfileMessage;
		private EditText searchedFriend;
		
		ArrayList<String> allGoals =  (ArrayList<String>) user.get("myGoals"); //arrayList that holds all the goals that the user has joined
		
		
		ArrayList<String> pastGoals = new ArrayList<String>();
		ArrayList<String> upcomingGoals = new ArrayList<String>();
		
		ArrayList<String> madeGoals = (ArrayList<String>) user.get("createdGoals"); //arrayList that holds all the goals that the user has created

		GoalPost goal;
		Date goalDate;
		
		  private ParseQueryAdapter<GoalPost> mainAdapter;
		  private ParseQueryAdapter<GoalPost> postsQueryAdapter2;

			private ListView listView;
		  private String selectedPostObjectId;
		  private GoalPost tempPost;
			private CustomAdapter urgentTodosAdapter;




		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user_profile);
			getActionBar().setDisplayHomeAsUpEnabled(true);
		    getActionBar().setDisplayShowHomeEnabled(false);
			
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
			
			sortLists();
			
			//code for creating a Parse Query Adapter that will be used to display goals
			 // Set up a customized query
//		    ParseQueryAdapter.QueryFactory<GoalPost> factory2 =
//		        new ParseQueryAdapter.QueryFactory<GoalPost>() {
//		          public ParseQuery<GoalPost> create() {
//						System.out.println("queried");
//
//		            ParseQuery<GoalPost> query = GoalPost.getQuery();
//					query.whereEqualTo("name", "future goal");
//		            query.include("user");
//		            query.orderByDescending("createdAt");
//		            return query;
//		          }
//		        };

		        ParseQueryAdapter.QueryFactory<GoalPost> factory =
		                new ParseQueryAdapter.QueryFactory<GoalPost>() {
		                  public ParseQuery<GoalPost> create() {
		                    ParseQuery<GoalPost> query = GoalPost.getQuery();
		                    query.include("user");
		                    System.out.println("inside main factory again");
		                    query.orderByDescending("createdAt");
		                    return query;
		                  }
		                };
		        
		        System.out.println("after factory");
		    // Set up the query adapter
		   postsQueryAdapter2 = new ParseQueryAdapter<GoalPost>(this, factory) {
		      @Override
		      public View getItemView(GoalPost post, View view, ViewGroup parent) {
		        if (view == null) {
		        	System.out.println("being viewed!");
		          view = View.inflate(getContext(), R.layout.anywall_post_item, null);
		        }
		        
		        // #TODO (nhurwitz) Replace fields with appropriate ones.
		        
		        String[] goalOwnerFirstLast = post.getOwner().get("fullName").toString().split("\\^");
		        int target = (Integer) post.getTargetGroupSize();
		        System.out.println("target:"+target);
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

		    // Disable automatic loading when the adapter is attached to a view.
		    postsQueryAdapter2.setAutoload(false);

		    // Disable pagination, we'll manage the query limit ourselves
		    postsQueryAdapter2.setPaginationEnabled(false);

		    // Attach the query adapter to the view
		    ListView postsListView = (ListView) findViewById(R.id.upComing_goals_listview);
		    postsListView.setAdapter(postsQueryAdapter2);

		    // Set up the handler for an item's selection
		    postsListView.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        final GoalPost item = postsQueryAdapter2.getItem(position);
		        selectedPostObjectId = item.getObjectId();

		        Intent intent = new Intent(UserProfileActivity.this, ViewGoal.class)
		        	.putExtra("goal_id", item.getObjectId())
		        	.putExtra("goal_owner", item.getOwner().get("fullName").toString());
		        
		        startActivity(intent);
		      }
		    });
		  
			
			
			
//			ParseQueryAdapter<GoalPost> adapter =
//					  new ParseQueryAdapter<GoalPost>(this, new ParseQueryAdapter.QueryFactory<GoalPost>() {
//					    public ParseQuery<GoalPost> create() {
//					      // Here we can configure a ParseQuery to our heart's desire.
//					      ParseQuery<GoalPost> query = new ParseQuery<GoalPost>("GoalPost");
//							query.whereEqualTo("name", "future goal");
//							System.out.println("got queried");
//					      return query;
//					    }
//					    
//					    public View getItemView(GoalPost post, View view, ViewGroup parent) {
//					        if (view == null) {
//					          view = View.inflate(getBaseContext(), R.layout.anywall_post_item, null);
//					          System.out.println("got viewed");
//					        }
//					        
//					        // #TODO (nhurwitz) Replace fields with appropriate ones.
//					        
//					        String[] goalOwnerFirstLast = post.getOwner().get("fullName").toString().split("\\^");
//					        int target = (Integer) post.getTargetGroupSize();
//					        System.out.println("target:"+target);
//					        int current = (Integer) post.getCurrentGroupSize();
//					        TextView contentView = (TextView) view.findViewById(R.id.content_view);
//					        TextView usernameView = (TextView) view.findViewById(R.id.username_view);
//					        TextView sizeView = (TextView) view.findViewById(R.id.goal_list_attending);
//					        contentView.setText(post.getName());
//					        usernameView.setText(goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
//					        sizeView.setText(current + "/" + target);
//					        return view;
//					    }
//					  });
//			adapter.setTextKey("title");
//			
//			 ListView listView = (ListView) findViewById(R.id.upComing_goals_listview);
//			 listView.setAdapter(adapter);
			  
			  
			

			
		  postsListView.setOnItemClickListener(new OnItemClickListener() {
		        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		          final GoalPost item = postsQueryAdapter2.getItem(position);
		          selectedPostObjectId = item.getObjectId();

		          Intent intent = new Intent(UserProfileActivity.this, ViewGoal.class)
		          	.putExtra("goal_id", item.getObjectId())
		          	.putExtra("goal_owner", item.getOwner().get("fullName").toString());
		          
		          startActivity(intent);
		        }
		      });
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
		
		
		public void searchAndAddFriends(){ //is called when button is pushed to add friends (use searchedFriend attribute)
			
		}
		
		public void sortLists(){ //function to display the three lists of the user's goals
			//first sort goals between upcoming and past goals

			int i;
			Date todayDate = new Date();
			System.out.println("empty??"+allGoals.isEmpty());
			System.out.println("size?? "+allGoals.size());
			for (i=0; i < allGoals.size(); i++){
				
				ParseQuery<GoalPost> postQuery = ParseQuery.getQuery(GoalPost.class);
			    
			    try {
			    	String id = allGoals.get(i);
			    	System.out.println("ID:::"+id);
			    	goal = postQuery.get(id);
			    	goalDate = goal.getDate();
			    } catch (com.parse.ParseException e) {
					Log.e("Goal Error", e.getMessage());
				}
				
				System.out.println("iteration number: "+i);	
			    
			    if (todayDate.compareTo(goalDate) <= 0){ //means the goal has not passed yet or is happening right now
			    	upcomingGoals.add(allGoals.get(i));			    	
			    }
			    else{
			    	pastGoals.add(allGoals.get(i));			    	
			    }
			}
//			displayUpcomingGoals(upcomingGoals);
//			displayPastGoals(pastGoals);
//			displayCreatedGoals();			
		}
		
		public void displayUpcomingGoals(final ArrayList<String> goals){
			
		  }
			
		
		public void displayPastGoals(ArrayList<String> goals){
			
		}
		public void displayCreatedGoals(){
			
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
		        
		        menu.findItem(R.id.action_home).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			        public boolean onMenuItemClick(MenuItem item) {
			      	 // System.out.println("Inside OnMenuItemClick");
			      	  Intent amp = new Intent(UserProfileActivity.this, MainActivity.class);
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
		
