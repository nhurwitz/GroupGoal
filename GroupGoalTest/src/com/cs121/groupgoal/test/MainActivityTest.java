package com.cs121.groupgoal.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.cs121.groupgoal.*;

public class MainActivityTest
extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity mainActivity;
	private ListView groupList;
	
	public MainActivityTest() {
		super(MainActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mainActivity = getActivity();
		groupList =
		        (ListView) mainActivity
		        .findViewById(com.cs121.groupgoal.R.id.posts_listview);
	}
	
	public void testPreconditions() {
	    assertNotNull("Main Activity is Null", mainActivity);
	    assertNotNull("Group List is null", groupList);
	}
	public void testEmpty() {
	   assertEquals(0, groupList.getCount());
	}
}