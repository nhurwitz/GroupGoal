package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a post.
 */
@ParseClassName("Posts")
public class GoalPost extends ParseObject {
	
  public String getName() {
	  return getString("name");
  }

  public void setName(String name) {
	  put ("name", name);
  }
  
  public String getDescription() {
	  return getString("description");
  }  

  public void setDescription(String value) {
    put("description", value);
  }

  public ParseUser getOwner() {
    return getParseUser("user");
  }

  public void setOwner(ParseUser value) {
    put("user", value);
  }

  public ParseGeoPoint getLocation() {
    return getParseGeoPoint("location");
  }

  public void setLocation(ParseGeoPoint value) {
    put("location", value);
  }
  
  public boolean isPrivate() {
	  return getBoolean("private");
  }
  
  public void setPrivate(boolean isPrivate) {
	  put("private", isPrivate);
  }

  public static ParseQuery<GoalPost> getQuery() {
    return ParseQuery.getQuery(GoalPost.class);
  }
}
