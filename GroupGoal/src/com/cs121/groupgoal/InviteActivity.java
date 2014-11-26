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

public class InviteActivity extends Activity {
	
	private static class ViewHolder {
		TextView friendName;
	    CheckBox inviteBox;
	}
	
	private ViewHolder viewHolder;
	
	private List<String> toInvite;
	
	private ListView friendsList;
	private Button sendInvite;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
		
		Intent intent = getIntent();
		String parseId = intent.getStringExtra("goal_id");
		
		GoalPost goal;
		try {
			goal = ParseQuery.getQuery(GoalPost.class).get(parseId);
			@SuppressWarnings("unchecked")
			List<String> userIds = (List<String>) goal.get("friendsList");
			FriendAdapter fa = new FriendAdapter(this, userIds);
			
			friendsList = (ListView) findViewById(R.id.friends_list);
			sendInvite = (Button) findViewById(R.id.invite_friends_button);
			
			friendsList.setAdapter(fa);
			sendInvite.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					
					
				}
				
			});
		} catch (com.parse.ParseException e) {
		   Log.e("Goal Error", e.getMessage());
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
	
	private class FriendAdapter extends ArrayAdapter<String> {
		
		private Context mContext;
		private List<String> userIds;

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
		      ParseUser user;
		      
		      ParseQuery<ParseUser> postQuery = ParseQuery.getQuery(ParseUser.class);
			    
		      try {
		      	  user = postQuery.get(id);
		      	  String[] firstLast = user.get("fullName").toString().split("\\^");
		      	  viewHolder.friendName.setText(firstLast[0] + " " + firstLast[1]);
		      } catch (com.parse.ParseException e) {
				  Log.e("Goal Error", e.getMessage());
			  }
			     
			
			
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
