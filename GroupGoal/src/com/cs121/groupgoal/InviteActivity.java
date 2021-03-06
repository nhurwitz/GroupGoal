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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class InviteActivity extends Activity {
	
	private static class ViewHolder {
		TextView friendName;
	    CheckBox inviteBox;
	}
	
	private ViewHolder viewHolder;
	private GoalPost goal;
	private String parseId;
	private String ownerName;
	private List<String> toInvite;
	
	private ListView friendsList;
	private Button sendInvite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
		
		Intent intent = getIntent();
		parseId = intent.getStringExtra("goal_id");
		ownerName = intent.getStringExtra("goal_owner");
		
		try {
			goal = ParseQuery.getQuery(GoalPost.class).get(parseId);
		} catch (com.parse.ParseException e) {
		   Log.e("Goal Error", e.getMessage());
		}
		
		@SuppressWarnings("unchecked")
		List<String> userIds = (List<String>) ParseUser.getCurrentUser().get("friendsList");
		if(userIds == null) {
			userIds = new ArrayList<String>();
		}
		FriendAdapter fa = new FriendAdapter(this, userIds);
		
		friendsList = (ListView) findViewById(R.id.invite_listview);
		sendInvite = (Button) findViewById(R.id.invite_friends_button);
		if(userIds.isEmpty())
			sendInvite.setText("No friends to invite");
		
		friendsList.setAdapter(fa);
		sendInvite.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onClick(View v) {
				if(toInvite != null && !toInvite.isEmpty()) {
					for(String id : toInvite) {
						try {
							ParseUser user = ParseQuery.getQuery(ParseUser.class)
									.get(id);
							List<String> ig = (List<String>) user.get("invitedGoals");
							String userId = ParseUser.getCurrentUser().getObjectId();
							boolean contained = false;
							for(String invite : toInvite) {
								if(id == invite.split("\\^")[0]) {
									contained = true;
								}
							}
							
							if(!contained) {
								ig.add(id + "^" + userId);
							}
							
							user.put("invitedGoals",ig);
							user.saveEventually();
						} catch (com.parse.ParseException e) {
							   Log.e("Invite Error", e.getMessage());
						}
					}
				
					Toast.makeText(InviteActivity.this, "Friends successfully invited!", Toast.LENGTH_LONG)
						.show();
				}
				Intent intent = new Intent(InviteActivity.this, ViewGoal.class)
					.putExtra("goal_id", parseId)
					.putExtra("goal_owner", ownerName);
				
				startActivity(intent);
				
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invite, menu);
	    getActionBar().setDisplayShowTitleEnabled(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(InviteActivity.this, ViewGoal.class)
			.putExtra("goal_id", parseId)
			.putExtra("goal_owner", ownerName);
	
		startActivity(intent);
		return true;
	}
	
	private class FriendAdapter extends ArrayAdapter<String> {
		
		private Context mContext;
		private List<String> userIds;
		private ParseUser queriedUser;

		public FriendAdapter(Context context, List<String> friends) {
			super(context, R.layout.friend_item, friends);
			
		    this.mContext = context;
		    this.userIds = friends;
		}
		
		@Override
		public int getCount() {
			return userIds.size();
		}
			
		public View getView(int position, View convertView, ViewGroup parent) {
			  
		      if(convertView == null){
		    	  viewHolder = new ViewHolder();
		          LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
		          convertView = mLayoutInflater.inflate(R.layout.friend_item, null);
		          viewHolder.friendName = (TextView) convertView.findViewById(R.id.friend_list_item);
			      viewHolder.inviteBox = (CheckBox) convertView.findViewById(R.id.invite_friend_checkbox);
			      convertView.setTag(viewHolder);
		      } else {
		    	  viewHolder = (ViewHolder) convertView.getTag();
		      }
		     
		      final String id = userIds.get(position);
			    
		      try {
		    	  queriedUser = ParseQuery.getQuery(ParseUser.class).get(id);
		      } catch (com.parse.ParseException e) {
				  Log.e("Goal Error", e.getMessage());
			  }
		      
		      String[] firstLast = queriedUser.get("fullName").toString().split("\\^");
	      	  viewHolder.friendName.setText(firstLast[0] + " " + firstLast[1]);
			     
			
			
		      convertView.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	if(!viewHolder.inviteBox.isChecked()) {
		            		viewHolder.inviteBox.setChecked(!viewHolder.inviteBox.isChecked());
		            		if(toInvite == null) {
		            			toInvite = new ArrayList<String>();
		            		}
		            		toInvite.add(id);
		            	} else {
		            		viewHolder.inviteBox.setChecked(!viewHolder.inviteBox.isChecked());
		            		toInvite.remove(id);
		            	}		            	
		            }
		      });
		      
		      
		
		      return convertView;
		  }
		
	}
}
