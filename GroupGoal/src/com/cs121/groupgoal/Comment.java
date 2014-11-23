package com.cs121.groupgoal;

import com.parse.ParseClassName;
//import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a comment.
 */
@ParseClassName("Comments")
public class Comment extends ParseObject {
	  public String getText() {
	    return getString("text");
	  }

	  public void setText(String value) {
	    put("text", value);
	  }
	  /*
	  public void setGoal(GoalPost g){
		  put ("goal", g);
	  }
	  
	  public GoalPost getGoal(){
		  return (GoalPost) getParseObject("goal");
	  }*/

	  public ParseUser getUser() {
	    return getParseUser("user");
	  }

	  public void setUser(String value) {
	    put("user", value);
	  }

	  public static ParseQuery<Comment> getQuery() {
	    return ParseQuery.getQuery("Comments");
	  }
	}

