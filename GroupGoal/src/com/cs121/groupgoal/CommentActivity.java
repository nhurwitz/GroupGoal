package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * This activity handles all the storing and displaying of commments
 * that are posted by the attendees of a specific goal
 *
 */
public class CommentActivity extends Activity {
	
	  private EditText postEditText; 
	  private Button postButton;
	  GoalPost currentGoal;
	  ArrayList<String> allComments;
	  private String commentText; 
	  String[] commentsMade;
	  String[] commentsMadeR;
	  private String goalID;
	  private String ownerName;
	  String currentGoalId;
	  ParseUser user= ParseUser.getCurrentUser();
	  	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		Intent intent = getIntent();
		goalID = intent.getStringExtra("goalObjectId");
		ownerName = intent.getStringExtra("goal_owner");
		currentGoalId = intent.getStringExtra("goalObjectId"); //gets the objectID of the goal we were viewing
	
		update();
	}
	
	private void update() {
		ParseQuery<GoalPost> postQuery = ParseQuery.getQuery(GoalPost.class); //fetch the GoalPost object associated with the objectId	    
	    try {
	    	currentGoal = postQuery.get(currentGoalId);
	    } catch (com.parse.ParseException e) {
			Log.e("Goal Error", e.getMessage());
		}
   
	    allComments = (ArrayList<String>) currentGoal.get("commentsList"); //this array holds the strings of comments posted
	    
	    commentsMade = new String[allComments.size()];
	    commentsMade = allComments.toArray(commentsMade);
	    Collections.reverse(Arrays.asList(commentsMade)); //want to reverse the array that stores the comments so that the 
	    commentsMadeR = commentsMade;					  // most recent comments appear at the top of the list
	    
	    //makes a listview from the reversed string array
	    ArrayAdapter adapter = new ArrayAdapter<String>(this, 
	    	      R.layout.comments_post_item, commentsMadeR);
	    	      
	    	      ListView listView = (ListView) findViewById(R.id.comments_list_view);
	    	      listView.setAdapter(adapter);

		postEditText = (EditText) findViewById(R.id.edit_comment); //gets the text that was inputted by the user
	  
	    postButton = (Button) findViewById(R.id.submit_comment_button);
	    postButton.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View v) {
	    	  post();
	      }
	    });
	}

	  private void post () {	  
		  
	    String text = postEditText.getText().toString();
	    String userFullName = user.getString("fullName").toString();
		String[] firstLast = userFullName.split("\\^");
		String finalName = (firstLast[0] + " " + firstLast[1]); //we add the user's full name to the beginning of the comment so
		String nameAndComment = (finalName + ": " + text);      // we know who made that comment 

	    currentGoal.add("commentsList", nameAndComment); //stores the name and comment into the arrayList of strings called commentsList
	    currentGoal.saveInBackground();
	    
		Toast.makeText(getApplicationContext(), "Comment successfully posted", 
				Toast.LENGTH_LONG).show();
		postEditText.setText("");
		
	    update();
	  }
	  
	  @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.comment, menu);
		    getActionBar().setDisplayShowTitleEnabled(false);
			return true;
		}
	  
	  @Override
		public boolean onOptionsItemSelected(MenuItem item) {
		  Intent intent = new Intent(CommentActivity.this, ViewGoal.class)
			.putExtra("goal_id", goalID)
			.putExtra("goal_owner", ownerName);
		  
		startActivity(intent);
		return true;
	  }
	
}