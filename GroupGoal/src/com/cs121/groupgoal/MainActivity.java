
package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQuery.CachePolicy;

public class MainActivity extends FragmentActivity {

  private GoalAdapter mAdapter;
  private ListView postsListView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    RemoteDataTask task = new RemoteDataTask();
    task.execute();
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
    getActionBar().setDisplayShowHomeEnabled(false);

    menu.findItem(R.id.action_settings).setOnMenuItemClickListener(new OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        return true;
      }
    });
    
    
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
          
      //------------------------------------------------------
      
      menu.findItem(R.id.action_post).setOnMenuItemClickListener(new OnMenuItemClickListener() {
          public boolean onMenuItemClick(MenuItem item) {
	              Intent intent = new Intent(MainActivity.this, PostActivity.class);
	              
	              startActivity(intent);
	              return true;
	          }
        });
          
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
          mAdapter = new GoalAdapter(MainActivity.this, new ArrayList<GoalPost>());


          postsListView.setAdapter(mAdapter);
          postsListView.setVisibility(View.VISIBLE);
      } 
   }
}



