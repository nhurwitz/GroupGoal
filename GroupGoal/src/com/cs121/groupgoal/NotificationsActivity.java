package com.cs121.groupgoal;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * This class contains the code necessary to receive notifications when you are invited to a goal or added as
 * a friend by someone else.
 * Does not currently work because of backend limitations, but when those limitations are removed,
 * code is here for use. 
 *
 */
public class NotificationsActivity extends Activity {
	
	private static class ViewHolder {
		TextView goalName;
	}
	
	private ViewHolder viewHolder;
	private GoalPost goal;
	
	private List<String> notifications;
	private ListView notificationsList;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		
		notifications = (List<String>) ParseUser.getCurrentUser().get("invitedGoals");
		if(notifications == null || notifications.isEmpty()) {
			TextView textView = (TextView) findViewById(R.id.notifications_text);
			textView.setText("No Notifications");
		} else {
			notificationsList = (ListView) findViewById(R.id.notification_listview);
			notificationsList.setAdapter(new NotificationsAdapter(this, notifications));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
	    getActionBar().setDisplayShowTitleEnabled(false);
	    
	    menu.findItem(R.id.action_logout).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	    	public boolean onMenuItemClick(MenuItem item) {
	        	// Call the Parse log out method
	              ParseUser.logOut();
	              // Start and intent for the dispatch activity
	              Intent intent = new Intent(NotificationsActivity.this, DispatchActivity.class);
	              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	              startActivity(intent);
		          return true;
	          }
	      });
	    menu.findItem(R.id.action_post).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	          public boolean onMenuItemClick(MenuItem item) {
		              Intent intent = new Intent(NotificationsActivity.this, PostActivity.class);
		              
		              startActivity(intent);
		              return true;
		          }
	       });

	        menu.findItem(R.id.action_my_profile).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	        	public boolean onMenuItemClick(MenuItem item) {
			      	  Intent amp = new Intent(NotificationsActivity.this, UserProfileActivity.class);
			          startActivity(amp);
			          return true;
			        }
	      });
	        
	        menu.findItem(R.id.action_my_friends).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	  Intent amp = new Intent(NotificationsActivity.this, MyFriendsActivity.class);
		          startActivity(amp);
		          return true;
		        }
		      });
	        
	        menu.findItem(R.id.action_upcoming_goals).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	Intent amp = new Intent(NotificationsActivity.this, UpcomingGoalsActivity.class);
		          startActivity(amp);
		          return true;
		        }
		      });
	        
	        menu.findItem(R.id.action_notifications).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	  Intent amp = new Intent(NotificationsActivity.this, NotificationsActivity.class);
		          startActivity(amp);
		          return true;
		        }
		      });
	        
	        menu.findItem(R.id.action_home).setOnMenuItemClickListener(new OnMenuItemClickListener() {
		        public boolean onMenuItemClick(MenuItem item) {
		      	  Intent amp = new Intent(NotificationsActivity.this, MainActivity.class);
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
	
	private class NotificationsAdapter extends ArrayAdapter<String> {
		
		private Context mContext;
		private List<String> goalUserIds;
		private GoalPost queriedGoal;
		private ParseUser queriedUser;

		public NotificationsAdapter(Context context, List<String> goals) {
			super(context, R.layout.notification_item, goals);
			
		    this.mContext = context;
		    this.goalUserIds = goals;
		}
		
		@Override
		public int getCount() {
			return goalUserIds.size();
		}
			
		public View getView(int position, View convertView, ViewGroup parent) {
			  
		      if(convertView == null){
		    	  viewHolder = new ViewHolder();
		          LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
		          convertView = mLayoutInflater.inflate(R.layout.friend_item, null);
		          viewHolder.goalName = (TextView) convertView.findViewById(R.id.notification_list_item);
			      convertView.setTag(viewHolder);
		      } else {
		    	  viewHolder = (ViewHolder) convertView.getTag();
		      }
		     
		      final String[] id = goalUserIds.get(position).split("\\^");
			  final String goalId = id[0];
			  final String userId = id[1];
		      try {
		    	  queriedGoal = ParseQuery.getQuery(GoalPost.class).get(goalId);
		      } catch (com.parse.ParseException e) {
				  Log.e("Goal Error", e.getMessage());
			  }
		      
		      try {
		    	  queriedUser = ParseQuery.getQuery(ParseUser.class).get(userId);
		      } catch (com.parse.ParseException e) {
				  Log.e("User Error", e.getMessage());
			  }
		      String name = queriedUser.get("fullName").toString().split("\\^")[0] + " " + 
		    		  queriedUser.get("fullName").toString().split("\\^")[1];
		      String text = name + " invited you to " + goal.getName();
		      viewHolder.goalName.setText(text);
		      convertView.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	Intent intent = new Intent(NotificationsActivity.this, ViewGoal.class)
		            		.putExtra("goal_id", goalId);
		            	startActivity(intent);
		            }
		      });

		      return convertView;
		  }
		
	}
}
