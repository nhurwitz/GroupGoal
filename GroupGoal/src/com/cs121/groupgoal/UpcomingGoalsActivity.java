package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This activity lists the logged-in user's upcoming goals--or the ones they
 * have joined and have not yet passed.
 * 
 */
public class UpcomingGoalsActivity extends Activity {

	private static ArrayList<String> upcomingGoals = new ArrayList<String>();
	ArrayList<String> allGoals = new ArrayList<String>();
	private ParseQueryAdapter<GoalPost> postsQueryAdapter;
	private String selectedPostObjectId;

	GoalPost goal;
	Date goalDate;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upcoming_goals);
		getActionBar().setDisplayShowTitleEnabled(false);

		// get the user and extract the list of goals
		ParseUser user = ParseUser.getCurrentUser();
		allGoals = (ArrayList<String>) user.get("myGoals"); // arrayList that
															// holds all the
															// goals that the
															// user has joined

		sortLists();

		// code to create listview for upcoming Goals
		ParseQueryAdapter.QueryFactory<GoalPost> factory = new ParseQueryAdapter.QueryFactory<GoalPost>() {
			public ParseQuery<GoalPost> create() {
				ParseQuery<GoalPost> query = ParseQuery.getQuery("Posts");
				query.whereContainedIn("objectId", upcomingGoals);
				query.include("user");
				query.orderByAscending("date");
				return query;
			}
		};

		// Set up the query adapter
		postsQueryAdapter = new ParseQueryAdapter<GoalPost>(this, factory) {
			@Override
			public View getItemView(GoalPost post, View view, ViewGroup parent) {
				if (view == null) {
					view = View.inflate(getContext(),
							R.layout.anywall_post_item, null);
				}

				// fill in appropriate variables to construct the list item
				String[] goalOwnerFirstLast = post.getOwner().get("fullName")
						.toString().split("\\^");
				int target = (Integer) post.getTargetGroupSize();
				int current = (Integer) post.getCurrentGroupSize();

				TextView contentView = (TextView) view
						.findViewById(R.id.content_view);
				TextView usernameView = (TextView) view
						.findViewById(R.id.username_view);
				TextView sizeView = (TextView) view
						.findViewById(R.id.goal_list_attending);
				contentView.setText(post.getName());
				usernameView.setText(goalOwnerFirstLast[0] + " "
						+ goalOwnerFirstLast[1]);

				// set number of attendees and check if complete
				if (target == 0 || current / target >= 1) {
					sizeView.setText("COMPLETE");
					sizeView.setTextColor(Color.GREEN);
				} else {
					sizeView.setText(current + "/" + target);
					sizeView.setTextColor(Color.BLACK);
				}
				return view;
			}
		};

		// Attach the query adapter to the view
		ListView listView = (ListView) findViewById(R.id.upComing_goals_listview);
		listView.setAdapter(postsQueryAdapter);

		// Set up the handler for an item's selection
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final GoalPost item = postsQueryAdapter.getItem(position);
				selectedPostObjectId = item.getObjectId();

				Intent intent = new Intent(UpcomingGoalsActivity.this,
						ViewGoal.class).putExtra("goal_id", item.getObjectId())
						.putExtra("goal_owner",
								item.getOwner().get("fullName").toString());

				startActivity(intent);
			}
		});

	}

	// function to display the three lists of the user's goals
	public void sortLists() {
		// first sort goals between upcoming and past goals

		int i;
		Date todayDate = new Date();
		System.out.println("empty??" + allGoals.isEmpty());
		System.out.println("size?? " + allGoals.size());
		for (i = 0; i < allGoals.size(); i++) {

			ParseQuery<GoalPost> postQuery = ParseQuery
					.getQuery(GoalPost.class);

			try {
				String id = (String) allGoals.get(i);
				System.out.println("ID:::" + id);
				goal = postQuery.get(id);
				goalDate = goal.getDate();
			} catch (com.parse.ParseException e) {
				Log.e("Goal Error", e.getMessage());
			}

			// means the goal has not passed yet or is happening right now
			if (todayDate.compareTo(goalDate) <= 0) {
				upcomingGoals.add(allGoals.get(i));
			}
		}

	}

	// returns upcoming goals list
	public static ArrayList<String> getUpcomingGoals() {
		return upcomingGoals;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		menu.findItem(R.id.action_logout).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						// Call the Parse log out method
						ParseUser.logOut();
						// Start and intent for the dispatch activity
						Intent intent = new Intent(UpcomingGoalsActivity.this,
								DispatchActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						return true;
					}
				});
		 menu.findItem(R.id.action_post).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	          public boolean onMenuItemClick(MenuItem item) {
		              Intent intent = new Intent(UpcomingGoalsActivity.this, PostActivity.class);
		              
		              startActivity(intent);
		              return true;
		          }
	       });

		menu.findItem(R.id.action_my_profile).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						Intent amp = new Intent(UpcomingGoalsActivity.this,
								UserProfileActivity.class);
						startActivity(amp);
						return true;
					}
				});

		menu.findItem(R.id.action_my_friends).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						Intent amp = new Intent(UpcomingGoalsActivity.this,
								MyFriendsActivity.class);
						startActivity(amp);
						return true;
					}
				});

		menu.findItem(R.id.action_upcoming_goals).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						return true;
					}
				});

		menu.findItem(R.id.action_notifications).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						Intent amp = new Intent(UpcomingGoalsActivity.this,
								NotificationsActivity.class);
						startActivity(amp);
						return true;
					}
				});

		menu.findItem(R.id.action_home).setOnMenuItemClickListener(
				new OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						Intent amp = new Intent(UpcomingGoalsActivity.this,
								MainActivity.class);
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
