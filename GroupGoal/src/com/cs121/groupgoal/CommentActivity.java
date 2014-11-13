package com.cs121.groupgoal;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.TextView;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CommentActivity extends Activity {
	  private EditText postEditText;
	  private Button postButton;
	  //private ParseGeoPoint geoPoint;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		Log.d("a", "oncreate");
	    //Intent intent = getIntent();
		Log.d("a", "bye");
	    getActionBar().setDisplayHomeAsUpEnabled(true);
	    getActionBar().setDisplayShowHomeEnabled(false);


	    Intent intent = getIntent();
	    //Location location = intent.getParcelableExtra(Application.INTENT_EXTRA_LOCATION);
		Log.d("a", "lol");

	    //geoPoint = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
		Log.d("a", "bob");

		postEditText = (EditText) findViewById(R.id.edit_comment);
		Log.d("a", "kj");
/*
	    postEditText.addTextChangedListener(new TextWatcher() {
	      @Override
	      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	      }

	      @Override
	      public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	      }

	      @Override
	      public void afterTextChanged(Editable s) {
	        //updatePostButtonState();
	      }
	    });*/
	    
	  
	    postButton = (Button) findViewById(R.id.submit_comment_button);
	    postButton.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View v) {
	    	  Log.d("d", "in onClick");
	        post();
	      }
	    });

	    //updatePostButtonState();

	  }

	  private void post () {
		  //TRIED TO DEBUG
		  
		  Log.d("c", "in first post");		  
	    String text = postEditText.getText().toString();//.trim();
		 // String text = "hi";
	    // Set up a progress dialog
	   // final ProgressDialog dialog = new ProgressDialog(CommentActivity.this);
	    //dialog.setMessage(getString(R.string.progress_post));
	    //dialog.show();
	    
	    Log.d("e", "so far");

	    //Create a comment
	    Comment c = new Comment();
	    // Set the location to the current user's location
	    //c.setLocation(geoPoint);
	    Log.d("e", "farther");
	    c.setText(text);
	    Log.d("e", "even farther");
	    c.setUser(ParseUser.getCurrentUser());
	    Log.d("e", "farthest");
	    ParseACL acl = new ParseACL();
	    // Give public read access
	    Log.d("e", "pal");
	    acl.setPublicReadAccess(true);
	    Log.d("e", "so pal");
	    c.setACL(acl);
	    
	    // Save the comment
	    c.saveInBackground(new SaveCallback() {
	      @Override
	      public void done(ParseException e) {
	     //   dialog.dismiss();
	        finish();
	      }
	    });
	    Intent i = new Intent(CommentActivity.this, MainActivity.class);
		startActivity(i);
	  }
	  
	//  private String getPostEditTextText () {
		//    return postEditText.getText().toString().trim();
		  //}
	  
	//  private void updatePostButtonState () {
		    //int length = getPostEditTextText().length();
		   // postButton.setEnabled(true);
		  //}
	
}
