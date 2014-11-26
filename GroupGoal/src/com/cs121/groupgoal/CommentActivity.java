package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class CommentActivity extends Activity {
	  private EditText postEditText;
	  private Button postButton;
	  GoalPost currentGoal;
	  ArrayList<String> allComments;
	  private String commentText;
	  String[] commentsMade;
	  String[] commentsMadeR;
	  private String goalID;
	  ParseUser user= ParseUser.getCurrentUser();
	  	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		
		
		Intent intent = getIntent();
		goalID = intent.getStringExtra("goalObjectId");
		String currentGoalId = intent.getStringExtra("goalObjectId"); //gets the objectID of the goal we were viewing
	
		ParseQuery<GoalPost> postQuery = ParseQuery.getQuery(GoalPost.class); //fetch the GoalPost object associated with the obejctId	    
	    try {
	    	currentGoal = postQuery.get(currentGoalId);
	    } catch (com.parse.ParseException e) {
			Log.e("Goal Error", e.getMessage());
		}
   
	    allComments = (ArrayList<String>) currentGoal.get("commentsList"); //this array holds the strings of comments posted
	    
	    commentsMade = new String[allComments.size()];
	    commentsMade = allComments.toArray(commentsMade);
	    Collections.reverse(Arrays.asList(commentsMade)); //want to reverse the array that stores the comments so that the 
	    commentsMadeR = commentsMade;					  // most recent comments appear at the top of the page
	   
	    System.out.println("string array size "+commentsMade.length);
	    
	    //make listview from string array
	    ArrayAdapter adapter = new ArrayAdapter<String>(this, 
	    	      R.layout.comments_post_item, commentsMadeR);
	    	      
	    	      ListView listView = (ListView) findViewById(R.id.comments_list_view);
	    	      listView.setAdapter(adapter);

		postEditText = (EditText) findViewById(R.id.edit_comment);
	  
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

	    currentGoal.add("commentsList", nameAndComment);
	    currentGoal.saveInBackground();
	    
		Toast.makeText(getApplicationContext(), "Comment successfully posted", 
				Toast.LENGTH_LONG).show();
		
	    Intent i = new Intent(CommentActivity.this, CommentActivity.class);
	    i.putExtra("goalObjectId", goalID);
		startActivity(i);
	  }
	
}