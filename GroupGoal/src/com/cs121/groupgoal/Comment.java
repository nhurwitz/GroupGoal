package com.cs121.groupgoal;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a comment.
 */

public class Comment extends ParseObject {
	  public String getText() {
	    return getString("text");
	  }

	  public void setText(String value) {
	    put("text", value);
	  }

	  public ParseUser getUser() {
	    return getParseUser("user");
	  }

	  public void setUser(ParseUser value) {
	    put("user", value);
	  }
	  public void setLocation(ParseGeoPoint value) {
		    put("location", value);
		  }
	  public static ParseQuery<Comment> getQuery() {
	    return ParseQuery.getQuery(Comment.class);
	  }
	}
