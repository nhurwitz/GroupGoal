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
  
  public enum Category {
	  ACADEMIC,
	  SOCIAL,
	  FOOD,
	  ADVENTURE
  }
	
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
  
  public Category getCategory() {
	  return Category.valueOf(get("category").toString());
  }
  
  public void setCategory(Category category) {
	  put("category", category.toString());
  }
  
  public Date getDate() {
	  return getDate("date");
  }
  
  public void setDate(Date date) {
	  put("date", date);
  }
  
  public void setTargetGroupSize(int size) {
	  put("size", size);
  }
  
  public Number getTargetGroupSize() {
	  return getNumber("size");
  }
  
  public Number getCurrentGroupSize() {
	  return getList("attendees") == null ? 0 : getList("attendees").size();
  }
  
  public List<String> getAttendees() {
	  List<String> a = getList("attendees");
	  return a;
  }
  
  public void setAttendees(List<String> attendees) {
	  put("attendees", attendees);
  }
  
  public void addAttendee(String user) {
	  List<String> attendees = getList("attendees");
	  if(!attendees.contains(user)) {
		  attendees.add(user);
		  put("attendees", attendees);
	  }
  }
  
  public void removeAttendee(String user) {
	  List<String> attendees = getList("attendees");
	  if(attendees.contains(user)) {
		  attendees.remove(user);
		  put("attendees", attendees);
	  }
  }
  
  public void setCommentsList(List<String> comments){
	  put("commentsList", comments);
  }


  public static ParseQuery<GoalPost> getQuery() {
    return ParseQuery.getQuery(GoalPost.class);
  }
}
