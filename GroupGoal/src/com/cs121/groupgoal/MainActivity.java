
package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cs121.groupgoal.GoalPost.Category;
import com.cs121.groupgoal.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.ParseQuery.CachePolicy;

public class MainActivity extends FragmentActivity {

  private GoalAdapter mAdapter;
  private ListView postsListView;
  private EditText mEditText;
  private Button searchGoalsButton;
  private Button resetGoalsButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    RemoteDataTask task = new RemoteDataTask();
    task.execute();
    
    mEditText = (EditText) findViewById(R.id.goal_search_box);
    searchGoalsButton = (Button) findViewById(R.id.search_goal_button);
    searchGoalsButton.setOnClickListener(new OnClickListener() {

		@Override
    	public void onClick(View v) {
			String filter = mEditText.getText().toString();
			if(filter.length() != 0) {
				mAdapter.getFilter().filter(filter);
				mEditText.setText("");
			}	
		}    	

    });
    
    resetGoalsButton = (Button) findViewById(R.id.reset_goal_button);
    resetGoalsButton.setOnClickListener(new OnClickListener() {

		@Override
		public void onClick(View v) {
			mAdapter = new GoalAdapter(MainActivity.this, new ArrayList<GoalPost>());
			postsListView.setAdapter(mAdapter);
			updateData();			
		}
    	
    });
  }
  
  public void updateData() {
	  ParseQuery<GoalPost> query = ParseQuery.getQuery(GoalPost.class);
	  query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
	  query.include("user");
      query.orderByDescending("createdAt");
      query.findInBackground(new FindCallback<GoalPost>() {
		@Override
		public void done(List<GoalPost> goals, ParseException e) {
			if(goals != null){
	          mAdapter.clear();
	          for (int i = 0; i < goals.size(); i++) {
	              mAdapter.add(goals.get(i));
	          }
			}
		}    	  
      });
  }

  /*
   * Called when the Activity is no longer visible at all. Stop updates and disconnect.
   */
  @Override
  public void onStop() {
    super.onStop();
  }

  /*
   * Called when the Activity is restarted, even before it becomes visible.
   */
  @Override
  public void onStart() {
    super.onStart();
  }

  /*
   * Called when the Activity is resumed. Updates the view.
   */
  @Override
  protected void onResume() {
    super.onResume();

    Application.getConfigHelper().fetchConfigIfNeeded();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    getActionBar().setDisplayShowTitleEnabled(false);

    //Add the My Profile Option to the Menu-------------------RD
      menu.findItem(R.id.action_my_profile).setOnMenuItemClickListener(new OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
    	System.out.println("Inside OnMenuItemClick");
    	Intent amp = new Intent(MainActivity.this, UserProfileActivity.class);
    	//Pass the user to the amp activity
    	//amp.putExtra("user",User Object*);
        startActivity(amp);
        return true;
      }
    });
      
      menu.findItem(R.id.action_notifications).setOnMenuItemClickListener(new OnMenuItemClickListener() {
          public boolean onMenuItemClick(MenuItem item) {
        	Intent amp = new Intent(MainActivity.this, NotificationsActivity.class);
        	//Pass the user to the amp activity
        	//amp.putExtra("user",User Object*);
            startActivity(amp);
            return true;
          }
        });
          
      menu.findItem(R.id.action_my_friends).setOnMenuItemClickListener(new OnMenuItemClickListener() {
	        public boolean onMenuItemClick(MenuItem item) {
	      	  Intent amp = new Intent(MainActivity.this, MyFriendsActivity.class);
	          startActivity(amp);
	          return true;
	        }
	      });
      
      menu.findItem(R.id.action_post).setOnMenuItemClickListener(new OnMenuItemClickListener() {
          public boolean onMenuItemClick(MenuItem item) {
	              Intent intent = new Intent(MainActivity.this, PostActivity.class);
	              
	              startActivity(intent);
	              return true;
	          }
        });
      
      menu.findItem(R.id.action_logout).setOnMenuItemClickListener(new OnMenuItemClickListener() {
          public boolean onMenuItemClick(MenuItem item) {
        	// Call the Parse log out method
              ParseUser.logOut();
              // Start and intent for the dispatch activity
              Intent intent = new Intent(MainActivity.this, DispatchActivity.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(intent);
	          return true;
          }
        });
      
      MenuItem filterSpinner = menu.findItem(R.id.action_filter);
      View iView = filterSpinner.getActionView();
      if(iView instanceof Spinner) {
    	  final Spinner spinner = (Spinner) iView;
    	  ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
    			  this, android.R.layout.simple_spinner_item){
    		  
    		  @Override
    		  public View getView(int position, View convertView, ViewGroup parent) {
    			  View v = super.getView(position, convertView, parent);

                  ((TextView) v).setTextSize(16);
                  ((TextView) v).setTextColor(Color.WHITE);
                  return v;
    		  }
    		  
    		  public View getDropDownView(int position, View convertView,
                      ViewGroup parent) {
                  View v = super.getDropDownView(position, convertView,
                          parent);
  
                  ((TextView) v).setTextColor(Color.WHITE);
                  
                  return v;
              }
    	  };
    	  
    	  spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	  
    	  List<String> categoryStrings = new ArrayList<String>();
    	  categoryStrings.add("Filter");
    	  for(Category c : Category.values()) {
    		  categoryStrings.add(SignUpActivity.capitalize(c.toString()));
    	  }
    	  
    	  spinnerAdapter.addAll(categoryStrings);
    	  spinner.setAdapter(spinnerAdapter);
    	  spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@SuppressLint("DefaultLocale")
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String selected = parent.getItemAtPosition(position).toString();
				if(!selected.equals("Filter")){
					mAdapter.resetData();
					mAdapter.getCategoryFilter().filter(selected.toUpperCase());
				} else {
					mAdapter = new GoalAdapter(MainActivity.this, new ArrayList<GoalPost>());
					postsListView.setAdapter(mAdapter);
					updateData();	
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
    		  
    	  });
      }
      
      
      
          
      //------------------------------------------------------
     
    return true;
  }

  private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

      @Override
      protected void onPreExecute() {
          super.onPreExecute();
      }

      @Override
      protected Void doInBackground(Void... params) {
          updateData();
          return null;
      }

      @Override
      protected void onPostExecute(Void result) {
    	  postsListView = (ListView) findViewById(R.id.posts_listview);
    	  postsListView.setTextFilterEnabled(true);
          mAdapter = new GoalAdapter(MainActivity.this, new ArrayList<GoalPost>());


          postsListView.setAdapter(mAdapter);
          postsListView.setVisibility(View.VISIBLE);
      } 
   }
}



