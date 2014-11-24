package com.cs121.groupgoal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
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

	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

	    // Set up a customized query
	    ParseQueryAdapter.QueryFactory<Comment> factory =
	    		
	        new ParseQueryAdapter.QueryFactory<Comment>() {
	    	
	          public ParseQuery<Comment> create() {
	           
	            ParseQuery<Comment> query = ParseQuery.getQuery("Comments");
	            query.include("user");
	            System.out.println("inside factory");
	          
	            return query;
	          }
	        };
	     // Set up the query adapter
	        commentsQueryAdapter = new ParseQueryAdapter<Comment>(this, factory) {
	        
	        	@Override
	          public View getItemView(Comment post, View view, ViewGroup parent) {

	            if (view == null) {
	              view = View.inflate(getContext(), R.layout.activity_comment_view, null);
	            }
	            
	            
	            //String[] goalOwnerFirstLast = post.getUser().toString().split("\\^");
	            TextView contentView = (TextView) view.findViewById(R.id.content_view);
	            TextView usernameView = (TextView) view.findViewById(R.id.username_view);
	            contentView.setText(post.getText());
	            String[] fullName = post.getUser().get("fullName").toString().split("\\^");
	            usernameView.setText(fullName[0] + " " + fullName[1]);
	            
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
	        Log.d("a", "6");
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setDisplayShowHomeEnabled(false);


	    Intent intent = getIntent();


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
	    // Set up a progress dialog
	    final ProgressDialog dialog = new ProgressDialog(CommentActivity.this);
	    dialog.setMessage(getString(R.string.progress_post));
	    dialog.show();
	    
	    commentsQueryAdapter.setTextKey("text");
	   
	    //Create a comment
	    Comment c = new Comment();

	    c.setText(text);
	    //c.setGoal(g)
	    c.setUser(ParseUser.getCurrentUser());

	    ParseACL acl = new ParseACL();
	    // Give public read access

	    acl.setPublicReadAccess(true);

	    c.setACL(acl);
	   // ListView postsListView = (ListView) findViewById(R.id.comments_listview);
	    commentsQueryAdapter.notifyDataSetChanged();
	    // Save the comment
	    c.saveInBackground(new SaveCallback() {
	      @Override
	      public void done(ParseException e) {
	       dialog.dismiss();
	        finish();
	      }
	    });
	
		Toast.makeText(getApplicationContext(), "Comment Successfully Created", 
				Toast.LENGTH_LONG).show();
		
	    Intent i = new Intent(CommentActivity.this, CommentActivity.class);
		startActivity(i);
	  }
	  
	@SuppressLint("NewApi")
	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
		  switch (item.getItemId()) {
		    // Respond to the action bar's Up/Home button
		    case android.R.id.home:
		        Intent upIntent = NavUtils.getParentActivityIntent(this);
		        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
		            // This activity is NOT part of this app's task, so create a new task
		            // when navigating up, with a synthesized back stack.
		            TaskStackBuilder.create(this)
		                    // Add all of this activity's parents to the back stack
		                    .addNextIntentWithParentStack(upIntent)
		                    // Navigate up to the closest parent
		                    .startActivities();
		        } else {
		            // This activity is part of this app's task, so simply
		            // navigate up to the logical parent activity.
		            NavUtils.navigateUpTo(this, upIntent);
		        }
		        return true;
		    }
		    return super.onOptionsItemSelected(item);
	  }

	
}
