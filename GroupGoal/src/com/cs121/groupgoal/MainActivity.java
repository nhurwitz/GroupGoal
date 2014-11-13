
package com.cs121.groupgoal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
public class MainActivity extends FragmentActivity implements LocationListener,
    GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener {

  /*
   * Define a request code to send to Google Play services This code is returned in
   * Activity.onActivityResult
   */
  private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

  /*
   * Constants for location update parameters
   */
  // Milliseconds per second
  private static final int MILLISECONDS_PER_SECOND = 1000;

  // The update interval
  private static final int UPDATE_INTERVAL_IN_SECONDS = 5;

  // A fast interval ceiling
  private static final int FAST_CEILING_IN_SECONDS = 1;

  // Update interval in milliseconds
  private static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
      * UPDATE_INTERVAL_IN_SECONDS;

  // A fast ceiling of update intervals, used when the app is visible
  private static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
      * FAST_CEILING_IN_SECONDS;

  /*
   * Constants for handling location results
   */
  // Conversion from feet to meters
  private static final float METERS_PER_FEET = 0.3048f;

  // Conversion from kilometers to meters
  private static final int METERS_PER_KILOMETER = 1000;

  // Initial offset for calculating the map bounds
  private static final double OFFSET_CALCULATION_INIT_DIFF = 1.0;

  // Accuracy for calculating the map bounds
  private static final float OFFSET_CALCULATION_ACCURACY = 0.01f;

  // Maximum results returned from a Parse query
  private static final int MAX_POST_SEARCH_RESULTS = 20;

  // Maximum post search radius for map in kilometers
  private static final int MAX_POST_SEARCH_DISTANCE = 100;
  
  private static final String GOAL_OJBECT_ID = "goalObjectId";

  // Fields for the map radius in feet
  private float radius;
  private float lastRadius;

  // Fields for helping process map and location changes
  private final Map<String, Marker> mapMarkers = new HashMap<String, Marker>();
  private boolean hasSetUpInitialLocation;
  private String selectedPostObjectId;
  private Location lastLocation;
  private Location currentLocation;

  // A request to connect to Location Services
  private LocationRequest locationRequest;

  // Stores the current instantiation of the location client in this object
  private LocationClient locationClient;

  // Adapter for the Parse query
  private ParseQueryAdapter<GoalPost> postsQueryAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    radius = Application.getSearchDistance();
    lastRadius = radius;
    setContentView(R.layout.activity_main);

    // Create a new global location parameters object
    locationRequest = LocationRequest.create();

    // Set the update interval
    locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

    // Use high accuracy
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    // Set the interval ceiling to one minute
    locationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

    // Create a new location client, using the enclosing class to handle callbacks.
    locationClient = new LocationClient(this, this, this);

    // Set up a customized query
    ParseQueryAdapter.QueryFactory<GoalPost> factory =
        new ParseQueryAdapter.QueryFactory<GoalPost>() {
          public ParseQuery<GoalPost> create() {
            ParseQuery<GoalPost> query = GoalPost.getQuery();
            query.include("user");
            query.orderByDescending("createdAt");
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

    // Disable automatic loading when the adapter is attached to a view.
    postsQueryAdapter.setAutoload(false);

    // Disable pagination, we'll manage the query limit ourselves
    postsQueryAdapter.setPaginationEnabled(false);

    // Attach the query adapter to the view
    ListView postsListView = (ListView) findViewById(R.id.posts_listview);
    postsListView.setAdapter(postsQueryAdapter);

    // Set up the handler for an item's selection
    postsListView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final GoalPost item = postsQueryAdapter.getItem(position);
        selectedPostObjectId = item.getObjectId();

        Intent intent = new Intent(MainActivity.this, ViewGoal.class)
        	.putExtra("goal_id", item.getObjectId())
        	.putExtra("goal_owner", item.getOwner().get("fullName").toString());
        
        startActivity(intent);
      }
    });
  }

  /*
   * Called when the Activity is no longer visible at all. Stop updates and disconnect.
   */
  @Override
  public void onStop() {
    // If the client is connected
    if (locationClient.isConnected()) {
      stopPeriodicUpdates();
    }

    // After disconnect() is called, the client is considered "dead".
    locationClient.disconnect();

    super.onStop();
  }

  /*
   * Called when the Activity is restarted, even before it becomes visible.
   */
  @Override
  public void onStart() {
    super.onStart();

    // Connect to the location services client
    locationClient.connect();
  }

  /*
   * Called when the Activity is resumed. Updates the view.
   */
  @Override
  protected void onResume() {
    super.onResume();

    Application.getConfigHelper().fetchConfigIfNeeded();

    // Get the latest search distance preference
    radius = Application.getSearchDistance();
    // Save the current radius
    lastRadius = radius;
    // Query for the latest data to update the views.
    doListQuery();
  }

  /*
   * Handle results returned to this Activity by other Activities started with
   * startActivityForResult(). In particular, the method onConnectionFailed() in
   * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to start
   * an Activity that handles Google Play services problems. The result of this call returns here,
   * to onActivityResult.
   */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    // Choose what to do based on the request code
    switch (requestCode) {

      // If the request code matches the code sent in onConnectionFailed
      case CONNECTION_FAILURE_RESOLUTION_REQUEST:

        switch (resultCode) {
          // If Google Play services resolved the problem
          case Activity.RESULT_OK:

            if (Application.APPDEBUG) {
              // Log the result
              Log.d(Application.APPTAG, "Connected to Google Play services");
            }

            break;

          // If any other result was returned by Google Play services
          default:
            if (Application.APPDEBUG) {
              // Log the result
              Log.d(Application.APPTAG, "Could not connect to Google Play services");
            }
            break;
        }

        // If any other request code was received
      default:
        if (Application.APPDEBUG) {
          // Report that this Activity received an unknown requestCode
          Log.d(Application.APPTAG, "Unknown request code received for the activity");
        }
        break;
    }
  }

  /*
   * Verify that Google Play services is available before making a request.
   * 
   * @return true if Google Play services is available, otherwise false
   */
  private boolean servicesConnected() {
    // Check that Google Play services is available
    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

    // If Google Play services is available
    if (ConnectionResult.SUCCESS == resultCode) {
      if (Application.APPDEBUG) {
        // In debug mode, log the status
        Log.d(Application.APPTAG, "Google play services available");
      }
      // Continue
      return true;
      // Google Play services was not available for some reason
    } else {
      // Display an error dialog
      Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
      if (dialog != null) {
        ErrorDialogFragment errorFragment = new ErrorDialogFragment();
        errorFragment.setDialog(dialog);
        errorFragment.show(getSupportFragmentManager(), Application.APPTAG);
      }
      return false;
    }
  }

  /*
   * Called by Location Services when the request to connect the client finishes successfully. At
   * this point, you can request the current location or start periodic updates
   */
  public void onConnected(Bundle bundle) {
    if (Application.APPDEBUG) {
      Log.d("Connected to location services", Application.APPTAG);
    }
    currentLocation = getLocation();
    startPeriodicUpdates();
  }

  /*
   * Called by Location Services if the connection to the location client drops because of an error.
   */
  public void onDisconnected() {
    if (Application.APPDEBUG) {
      Log.d("Disconnected from location services", Application.APPTAG);
    }
  }

  /*
   * Called by Location Services if the attempt to Location Services fails.
   */
  public void onConnectionFailed(ConnectionResult connectionResult) {
    // Google Play services can resolve some errors it detects. If the error has a resolution, try
    // sending an Intent to start a Google Play services activity that can resolve error.
    if (connectionResult.hasResolution()) {
      try {

        // Start an Activity that tries to resolve the error
        connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

      } catch (IntentSender.SendIntentException e) {

        if (Application.APPDEBUG) {
          // Thrown if Google Play services canceled the original PendingIntent
          Log.d(Application.APPTAG, "An error occurred when connecting to location services.", e);
        }
      }
    } else {
      // If no resolution is available, display a dialog to the user with the error.
      showErrorDialog(connectionResult.getErrorCode());
    }
  }

  /*
   * Report location updates to the UI.
   */
  public void onLocationChanged(Location location) {
    currentLocation = location;
    if (lastLocation != null
        && geoPointFromLocation(location)
        .distanceInKilometersTo(geoPointFromLocation(lastLocation)) < 0.01) {
      // If the location hasn't changed by more than 10 meters, ignore it.
      return;
    }
    lastLocation = location;
    if (!hasSetUpInitialLocation) {
      // Zoom to the current location.
      hasSetUpInitialLocation = true;
    }
    doListQuery();
  }

  /*
   * In response to a request to start updates, send a request to Location Services
   */
  private void startPeriodicUpdates() {
    locationClient.requestLocationUpdates(locationRequest, this);
  }

  /*
   * In response to a request to stop updates, send a request to Location Services
   */
  private void stopPeriodicUpdates() {
    locationClient.removeLocationUpdates(this);
  }

  /*
   * Get the current location
   */
  private Location getLocation() {
    // If Google Play Services is available
    if (servicesConnected()) {
      // Get the current location
      return locationClient.getLastLocation();
    } else {
      return null;
    }
  }

  /*
   * Set up a query to update the list view
   */
  private void doListQuery() {
    Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
    // If location info is available, load the data
    if (myLoc != null) {
      // Refreshes the list view with new data based
      // usually on updated location data.
      postsQueryAdapter.loadObjects();
    }
  }

  /*
   * Helper method to get the Parse GEO point representation of a location
   */
  private ParseGeoPoint geoPointFromLocation(Location loc) {
    return new ParseGeoPoint(loc.getLatitude(), loc.getLongitude());
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
        	  Location myLoc = (currentLocation == null) ? lastLocation : currentLocation;
	          	if (myLoc == null) {
	          	  	 Toast.makeText(MainActivity.this,
	          	            "Please try again after your location appears on the map.",
	          	            Toast.LENGTH_LONG).show();
	          	}
	          	  
	              Intent intent = new Intent(MainActivity.this, PostActivity.class)
	              	.putExtra(Application.INTENT_EXTRA_LOCATION, myLoc);
	              
	              startActivity(intent);
	              return true;
	          }
        });
          
      //------------------------------------------------------
     
    return true;
  }

  /*
   * Show a dialog returned by Google Play services for the connection error code
   */
  private void showErrorDialog(int errorCode) {
    // Get the error dialog from Google Play services
    Dialog errorDialog =
        GooglePlayServicesUtil.getErrorDialog(errorCode, this,
            CONNECTION_FAILURE_RESOLUTION_REQUEST);

    // If Google Play services can provide an error dialog
    if (errorDialog != null) {

      // Create a new DialogFragment in which to show the error dialog
      ErrorDialogFragment errorFragment = new ErrorDialogFragment();

      // Set the dialog in the DialogFragment
      errorFragment.setDialog(errorDialog);

      // Show the error dialog in the DialogFragment
      errorFragment.show(getSupportFragmentManager(), Application.APPTAG);
    }
  }

  /*
   * Define a DialogFragment to display the error dialog generated in showErrorDialog.
   */
  public static class ErrorDialogFragment extends DialogFragment {
    // Global field to contain the error dialog
    private Dialog mDialog;

    /**
     * Default constructor. Sets the dialog field to null
     */
    public ErrorDialogFragment() {
      super();
      mDialog = null;
    }

    /*
     * Set the dialog to display
     * 
     * @param dialog An error dialog
     */
    public void setDialog(Dialog dialog) {
      mDialog = dialog;
    }

    /*
     * This method must return a Dialog to the DialogFragment.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
      return mDialog;
    }
  }
}
