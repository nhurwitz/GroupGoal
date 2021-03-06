	package com.cs121.groupgoal;
	
	import java.util.ArrayList;
import java.util.List;
	
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
	
	import com.cs121.groupgoal.R;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
	
	/**
	 * Activity which displays a sign up screen to the user.
	 */
	public class SignUpActivity extends Activity {

	  private EditText firstNameEditText;
	  private EditText lastNameEditText;
	  private EditText usernameEditText;
	  private EditText passwordEditText;
	  private EditText passwordAgainEditText;
	
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	
	    setContentView(R.layout.activity_signup);
	    getActionBar().setDisplayShowTitleEnabled(false);
	    
	    // Sets up the signup form.
	    firstNameEditText = (EditText) findViewById(R.id.username_firstname_text);
	    lastNameEditText = (EditText) findViewById(R.id.username_lastname_text);
	    usernameEditText = (EditText) findViewById(R.id.username_edit_text);
	
	    passwordEditText = (EditText) findViewById(R.id.password_edit_text);
	    passwordAgainEditText = (EditText) findViewById(R.id.password_again_edit_text);
	    passwordAgainEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
	      @Override
	      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	        if (actionId == R.id.edittext_action_signup ||
	            actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
	          signup();
	          return true;
	        }
	        return false;
	      }
	    });
	
	    Button mActionButton = (Button) findViewById(R.id.action_button);
	    mActionButton.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View view) {
	        signup();
	      }
	    });
	  }
	
	  private void signup() {
		String userFirstName = capitalize(firstNameEditText.getText().toString().trim());
		String userLastName = capitalize(lastNameEditText.getText().toString().trim());
		
		String userfullname = userFirstName + "^" + userLastName;
	    String username = usernameEditText.getText().toString().trim();
	    String password = passwordEditText.getText().toString().trim();
	    String passwordAgain = passwordAgainEditText.getText().toString().trim();
	
	    // Validate the sign up data
	    boolean validationError = false;
	    StringBuilder validationErrorMessage = new StringBuilder(getString(R.string.error_intro));
	    if (username.length() == 0) {
	      validationError = true;
	      validationErrorMessage.append(getString(R.string.error_blank_username));
	    }
	    if (userfullname.length() < 2) {
		      validationError = true;
		      validationErrorMessage.append(getString(R.string.error_blank_username));
		}
	    if (password.length() == 0) {
	      if (validationError) {
	        validationErrorMessage.append(getString(R.string.error_join));
	      }
	      validationError = true;
	      validationErrorMessage.append(getString(R.string.error_blank_password));
	    }
	    if (!password.equals(passwordAgain)) {
	      if (validationError) {
	        validationErrorMessage.append(getString(R.string.error_join));
	      }
	      validationError = true;
	      validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
	    }
	    validationErrorMessage.append(getString(R.string.error_end));
	
	    // If there is a validation error, display the error
	    if (validationError) {
	      Toast.makeText(SignUpActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
	          .show();
	      return;
	    }
	
	    // Set up a progress dialog
	    final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
	    dialog.setMessage(getString(R.string.progress_signup));
	    dialog.show();
	
	    // Set up a new Parse user
	    ParseUser user = new ParseUser(); //creates a new Parse User
	    user.put("fullName", userfullname); //save the fullname entered by the user, as well as the username and password
	    user.setUsername(username);
	    user.setPassword(password);
	    
	    String message = "Edit Your Personal Message Here";
	    user.put("userDescription", message);
	
	    List<String> emptyList = new ArrayList<String>(); 
	    user.put("friendsList", emptyList); //stores more user attributes into parse
	    user.put("myGoals",  emptyList);
	    user.put("createdGoals",  emptyList);
	    user.put("invitedGoals", emptyList);
	    
	    // Call the Parse signup method
	    user.signUpInBackground(new SignUpCallback() {
	      @Override
	      public void done(ParseException e) {
	        dialog.dismiss();
	        if (e != null) {
	          // Show the error message
	          Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
	        } else {
	          // Start an intent for the dispatch activity
	          Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
	          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	          startActivity(intent);
	        }
	      }
	    });
	  }
	  
	  @SuppressLint("DefaultLocale")
	public static String capitalize(String name) {
		  name = name.toLowerCase();
		  if(name.length() > 1) {
			  return Character.toUpperCase(name.charAt(0)) + name.substring(1);
		  } else {
			  return name.toUpperCase();
		  }
		  
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
