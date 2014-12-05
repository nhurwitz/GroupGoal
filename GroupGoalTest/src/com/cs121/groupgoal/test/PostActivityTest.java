package com.cs121.groupgoal.test;

import java.util.ArrayList;
import java.util.List;

import com.cs121.groupgoal.GoalPost;
import com.cs121.groupgoal.MainActivity;
import com.cs121.groupgoal.PostActivity;
import com.cs121.groupgoal.GoalPost;
import com.parse.ParseUser;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.ListView;
import junit.framework.TestCase;

public class PostActivityTest extends ActivityUnitTestCase<PostActivity> {

	private PostActivity postActivity;
	private Intent mPostIntent;
	
	public PostActivityTest() {
		super(PostActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		postActivity = getActivity();
		mPostIntent = new Intent (getInstrumentation()
				.getTargetContext(), PostActivity.class);
		startActivity(mPostIntent, null, null);
		final Button createButton = (Button) getActivity().
				findViewById(com.cs121.groupgoal.R.id.submit_goal_button);

	}
	
	//NEED MOCK OBJECT CLASSES FOR THESE TESTS TO WORK PROPERLY (CAN'T
	//DIRECTLY INTERACT WITH PARSE)
	/*
	public void testNextActivityWasLaunchedWithIntent(){
		startActivity(mPostIntent, null, null);
		final Button createButton = (Button) getActivity().
				findViewById(com.cs121.groupgoal.R.id.submit_goal_button);
		createButton.performClick();
		
		final Intent launchIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", launchIntent);
		assertTrue(isFinishCalled());
		
		//final String goalName = launchIntent.getStringExtra();
		
		
		
	}
	public void testGoalPost(){
		final GoalPost goal = new GoalPost();
		goal.setName("Test Goal");
		assertEquals(goal.getName(), "Test Goal");
		goal.setDescription("This is a test description");
		assertEquals(goal.getDescription(), "This is a test description");
		goal.setOwner(ParseUser.getCurrentUser());
		goal.setPrivate(true);
		assertEquals(goal.isPrivate(), true);
		goal.setTargetGroupSize(5);
		assertEquals(goal.getTargetGroupSize(), 5);
		GoalPost.Category a = GoalPost.Category.ACADEMIC;
		goal.setCategory(a);
		assertEquals(goal.getCategory(), GoalPost.Category.ACADEMIC);
		goal.setAttendees(new ArrayList<String>());
		goal.addAttendee("userA");
		assertEquals(goal.getAttendees(), (new ArrayList<String>()).add("userA"));
		goal.addAttendee("userA");
		assertEquals(goal.getAttendees(), (new ArrayList<String>()).add("userA"));
		goal.addAttendee("userB");
		//list to test against
		List<String> attendees = new ArrayList<String>();
		attendees.add("userA");
		attendees.add("userB");
		assertEquals(goal.getAttendees(), attendees);
	}*/
}
