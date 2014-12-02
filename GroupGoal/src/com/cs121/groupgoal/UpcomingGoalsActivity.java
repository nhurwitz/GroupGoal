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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

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

		ParseUser user= ParseUser.getCurrentUser();
		
		
		Log.d(user.getUsername(),"help?");
		
		
		List<String> mGoals =  (ArrayList<String>) user.get("myGoals");
		
		if(user.get("myGoals")!=null){
			Log.d(user.getUsername(),"allGoals");
		}
		allGoals = (ArrayList<String>) user.get("myGoals"); //arrayList that holds all the goals that the user has joined
		
	    sortLists();
	    
	    
		//code to create listview for upcoming Goals
		ParseQueryAdapter.QueryFactory<GoalPost> factory =
		        new ParseQueryAdapter.QueryFactory<GoalPost>() {
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
		          view = View.inflate(getContext(), R.layout.anywall_post_item, null);
		        }
		        
		        // #TODO (nhurwitz) Replace fields with appropriate ones.
		        
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
		    ListView listView = (ListView) findViewById(R.id.upComing_goals_listview);
			  listView.setAdapter(postsQueryAdapter);

		    // Set up the handler for an item's selection
		    listView.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		        final GoalPost item = postsQueryAdapter.getItem(position);
		        selectedPostObjectId = item.getObjectId();

		        Intent intent = new Intent(UpcomingGoalsActivity.this, ViewGoal.class)
		        	.putExtra("goal_id", item.getObjectId())
		        	.putExtra("goal_owner", item.getOwner().get("fullName").toString());
		        
		        startActivity(intent);
		      }
		    });
		    
		    
		
		    
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
		    	String id = (String) allGoals.get(i);
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
		    
		    /*
		    else{
		    	System.out.println("comparing");

		    	pastGoals.add(allGoals.get(i));			    	
		    }
		    */
		}
	
	}
	
	
	public static ArrayList<String> getUpcomingGoals(){
		return upcomingGoals;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
