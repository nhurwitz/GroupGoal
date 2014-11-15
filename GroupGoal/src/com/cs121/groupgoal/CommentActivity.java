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

	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

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
	    

	    //Create a comment
	    Comment c = new Comment();

	    c.setText(text);

	    c.setUser(ParseUser.getCurrentUser());

	    ParseACL acl = new ParseACL();
	    // Give public read access

	    acl.setPublicReadAccess(true);

	    c.setACL(acl);
	    
	    // Save the comment
	    c.saveInBackground(new SaveCallback() {
	      @Override
	      public void done(ParseException e) {
	       dialog.dismiss();
	        finish();
	      }
	    });
	    Intent i = new Intent(CommentActivity.this, MainActivity.class);
		startActivity(i);
	  }

	
}
