package com.cs121.groupgoal;

import java.util.Date;
import java.util.List;

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
  
  public String getEventLocation() {
	  return getString("event_location");
  }
  
  public void setEventLocation(String eventLocation) {
	  put("event_location", eventLocation);
  }
  
  public boolean isPrivate() {
	  return getBoolean("private");
  }
  
  public void setPrivate(boolean isPrivate) {
	  put("private", isPrivate);
  }
  
  public Date getDate() {
	  return getDate("date");
  }
  
  public void setDate(Date date) {
	  put("date", date);
  }
  
  public List<Object> getAttendees() {
	  return getList("attendees");
  }
  
  public void setAttendees(List<ParseUser> attendees) {
	  put("attendees", attendees);
  }
  
  public void addAttendee(ParseUser user) {
	  List<Object> attendees = getList("attendees");
	  if(!attendees.contains(user)) {
		  attendees.add(user);
		  put("attendees", attendees);
	  }
  }
  
  public void removeAttendee(ParseUser user) {
	  List<Object> attendees = getList("attendees");
	  if(attendees.contains(user)) {
		  attendees.remove(user);
		  put("attendees", attendees);
	  }
  }

  public static ParseQuery<GoalPost> getQuery() {
    return ParseQuery.getQuery(GoalPost.class);
  }
}
