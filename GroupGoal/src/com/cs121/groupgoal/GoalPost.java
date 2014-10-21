package com.cs121.groupgoal;

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
  public String getText() {
    return getString("text");
  }

  public void setText(String value) {
    put("text", value);
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

  public static ParseQuery<GoalPost> getQuery() {
    return ParseQuery.getQuery(GoalPost.class);
  }
}
