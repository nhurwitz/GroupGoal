package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Arrays;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentActivity extends Activity {
	  private EditText postEditText;
	  private Button postButton;
	  // Adapter for the Parse query
	  private ParseQueryAdapter<Comment> commentsQueryAdapter;
	 //private ArrayList<Comment> commentList;
	  GoalPost currentGoal;
	  ArrayList<String> allComments;
	  private String ID;

	  Comment commentMade;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		Intent intent = getIntent();
		String currentGoalId = intent.getStringExtra("goalObjectId"); //gets the objectID of the goal we were viewing
		
		ParseQuery<GoalPost> postQuery = ParseQuery.getQuery(GoalPost.class); //fetch the GoalPost object associated with the obejctId	    
	    try {
	    	currentGoal = postQuery.get(currentGoalId);
	    } catch (com.parse.ParseException e) {
			Log.e("Goal Error", e.getMessage());
		}
	    
	    allComments = (ArrayList<String>) currentGoal.get("commentsList");
		
		
	    // Set up a customized query
	    ParseQueryAdapter.QueryFactory<Comment> factory =
	    		
	        new ParseQueryAdapter.QueryFactory<Comment>() {
	    	
	          public ParseQuery<Comment> create() {
	           
	            ParseQuery<Comment> query = ParseQuery.getQuery("Comments");
	            query.whereContainedIn("objectId", allComments);
		     //   query.include("user");
	            System.out.println("inside factory");
	            query.orderByDescending("createdAt");
	            return query;
	          }
	        };

	        // Set up the query adapter
	        commentsQueryAdapter = new ParseQueryAdapter<Comment>(this, factory) {
	        
	        	@Override
	          public View getItemView(Comment post, View view, ViewGroup parent) {
		              System.out.println("inside view");

	            if (view == null) {
	              view = View.inflate(getContext(), R.layout.comments_post_item, null);
	              System.out.println("inside view2");
	              Log.d("a", "3");
	            }
	            
	            
	            //String[] goalOwnerFirstLast = post.getUser().toString().split("\\^");
	            TextView contentView = (TextView) view.findViewById(R.id.comment_posting);
	           // TextView usernameView = (TextView) view.findViewById(R.id.username_view);
	            contentView.setText(post.getText());
	            //usernameView.setText(post.getUser().toString());
	            //Log.d("a", "4");
	            
	            return view;
	          }
	        };

	        // Disable automatic loading when the adapter is attached to a view.
	      //  commentsQueryAdapter.setAutoload(false);

	        // Disable pagination, we'll manage the query limit ourselves
	       // commentsQueryAdapter.setPaginationEnabled(false);
	        //Log.d("a", "5");
	        
	       
	        // Attach the query adapter to the view
	        ListView postsListView = (ListView) findViewById(R.id.comments_list_view);
	        postsListView.setAdapter(commentsQueryAdapter);
	       // Log.d("a", "6");
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setDisplayShowHomeEnabled(false);

		postEditText = (EditText) findViewById(R.id.edit_comment);
	    
	  
	    postButton = (Button) findViewById(R.id.submit_comment_button);
	    postButton.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View v) {
	        post();
	      }
	    });
	}

	  private void post () {
	  System.out.println("in post");
	    String text = postEditText.getText().toString();
	    // Set up a progress dialog
	   // final ProgressDialog dialog = new ProgressDialog(CommentActivity.this);
	    //dialog.setMessage(getString(R.string.progress_post));
	    //dialog.show();
	    System.out.println("after dialog string"+text);
	    //commentsQueryAdapter.setTextKey("text");
	   
	    //Create a comment
	    final Comment c = new Comment();
	    c.setText(text);
	    //c.setGoal(g)
	    String userId = ParseUser.getCurrentUser().getObjectId();
	    c.setUser(userId);
	    System.out.println("set user");
	    

	    commentsQueryAdapter.notifyDataSetChanged();
	    // Save the comment
	    c.saveInBackground(new SaveCallback() {
    	      @Override
	      public void done(ParseException e) {
	    	  if (e==null){
	    		  ID = c.getObjectId();
	  		      System.out.println("get id inside saving ahh:"+ID);
	  	    	  currentGoal.addAllUnique("commentsList", Arrays.asList(ID)); //adds the comment's ID to the list of ID held by the GoalPost
//	  		    currentGoal.addComment(ID);
	  	    	  currentGoal.saveInBackground(new SaveCallback(){
	  	    		  @Override
	  	    		  public void done(ParseException e){
	  	    			  finish();
	  	    		  }
	  	    	  });
	    	  }
	    	  else{}	
    	      };
	    });
		Toast.makeText(getApplicationContext(), "Comment Successfully Created", 
				Toast.LENGTH_LONG).show();
		
	    Intent i = new Intent(CommentActivity.this, CommentActivity.class);
		startActivity(i);
	  }
	    
	
}
