package com.cs121.groupgoal;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Data model for a comment. In the final version of this app we decided to not use the Comment Object, but code
 * is left here for future use. 
 */
@ParseClassName("Comments")
public class Comment extends ParseObject {
	  public String getText() {
	    return getString("text"); //returns the text of the comment
	  }

	  public void setText(String value) {
	    put("text", value); //stores the text of the comment into the Parse backend
	  }

	  public ParseUser getUser() {
	    return getParseUser("user"); //returns the user ID of the user who posted the comment
	  }

	  public void setUser(ParseUser value) {
	    put("user", value); //stores the user ID of the user who posted the comment
	  }

	  public static ParseQuery<Comment> getQuery() {
	    return ParseQuery.getQuery("Comments"); //returns a parseQuery for this Parse object
	  }
	}

