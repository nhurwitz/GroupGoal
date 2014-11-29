package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		setContentView(R.layout.activity_invite);
		
		notifications = (List<String>) ParseUser.getCurrentUser().get("invitedGoals");
		if(notifications.isEmpty()) {
			View linearLayout = (View) findViewById(R.id.notifications_layout);
			TextView textView = new TextView(this);
			textView.setText("No Notifications");
			((LinearLayout) linearLayout).addView(textView);
		} else {
			notificationsList = (ListView) findViewById(R.id.notification_listview);
			notificationsList.setAdapter(new NotificationsAdapter(this, notifications));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invite, menu);
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
