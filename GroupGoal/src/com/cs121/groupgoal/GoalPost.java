package com.cs121.groupgoal;

import java.util.Date;
import java.util.List;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a post. Creates a Parse object and stores its info
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
	  return getString("name"); //returns name of Goal
  }

  public void setName(String name) {
	  put ("name", name); //sets the name of the Goal and stores in backend
  }
  
  public String getDescription() {
	  return getString("description"); //returns the goal description
  }  

  public void setDescription(String value) {
    put("description", value); //stores the goal description
  }

  public ParseUser getOwner() {
    return getParseUser("user"); //returns the userID of the user who is posting the goal
  }

  public void setOwner(ParseUser value) {
    put("user", value); //stores the userID of the owner of the goal
  }
  
  public String getOwnerId() {
	  return getString("ownerid"); //returns the user ID of goal owner
  }
  
  public void setOwnerId(String id) {
	  put("ownerid", id); //stores the user ID of the goal owner
  }

  public ParseGeoPoint getLocation() {
    return getParseGeoPoint("location"); //returns the geoPoint of where the goal was created
  }

  public void setLocation(ParseGeoPoint value) {
    put("location", value); //stores the geoPoint of where the goal was created
  }
  
  public String getEventLocation() {
	  return getString("event_location"); //gets the location of the posted event
  }
  
  public void setEventLocation(String eventLocation) {
	  put("event_location", eventLocation); //stores the location
  }
  
  public boolean isPrivate() {
	  return getBoolean("private"); //returns true if the goal was marked as private
  }
  
  public void setPrivate(boolean isPrivate) {
	  put("private", isPrivate); //sets the boolean for whether the goal is private or not
  }
  
  public Category getCategory() {
	  return Category.valueOf(get("category").toString()); //returns the category of the goal
  }
  
  public void setCategory(Category category) {
	  put("category", category.toString()); //stores the category of the goal
  }
  
  public Date getDate() {
	  return getDate("date"); //returns the date of the posted goal
  }
  
  public void setDate(Date date) {
	  put("date", date); //stores the assigned date of the goal
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
	  return a; //returns a list of userID's of the people who joined the goal (including the goal owner)
  }
  
  public void setAttendees(List<String> attendees) {
	  put("attendees", attendees); //creates a list that will hold the userIDs of the attendees
  }
  
  public void addAttendee(String user) {
	  List<String> attendees = getList("attendees");
	  if(!attendees.contains(user)) {
		  attendees.add(user);
		  put("attendees", attendees); //adds userID to the list when they join the goal
	  }
  }
  
  public void removeAttendee(String user) {
	  List<String> attendees = getList("attendees");
	  if(attendees.contains(user)) {
		  attendees.remove(user);
		  put("attendees", attendees); //removes userID when they unjoin a goal
	  }
  }
  
  public void setCommentsList(List<String> comments){
	  put("commentsList", comments); //stores a list of strings that are the comments people posted to the goal
  }


  public static ParseQuery<GoalPost> getQuery() {
    return ParseQuery.getQuery(GoalPost.class);
  }
}
