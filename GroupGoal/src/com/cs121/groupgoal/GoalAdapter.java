
package com.cs121.groupgoal;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class GoalAdapter extends ArrayAdapter<GoalPost> implements Filterable{
	private Context mContext;
	private List<GoalPost> mGoals;
	ArrayAdapter<GoalPost> array;
	Activity activity;
	private Filter goalFilter;
	
	public GoalAdapter(Context context, List<GoalPost> objects) {
	      super(context, R.layout.anywall_post_item, objects);
	
	        this.mContext = context;
	        this.mGoals = objects;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
	      if(convertView == null){
	          LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
	          convertView = mLayoutInflater.inflate(R.layout.anywall_post_item, null);
	      }
	
	
	      final GoalPost goal = mGoals.get(position);
	      String[] goalOwnerFirstLast = goal.getOwner().get("fullName").toString().split("\\^");
	      int target = (Integer) goal.getTargetGroupSize();
	      int current = (Integer) goal.getCurrentGroupSize();
	
	      TextView goalView = (TextView) convertView.findViewById(R.id.content_view);
	      TextView nameView = (TextView) convertView.findViewById(R.id.username_view);
	      TextView attendingView = (TextView) convertView.findViewById(R.id.goal_list_attending);
	
	      goalView.setText(goal.getName().toString());
	      nameView.setText(goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
	      attendingView.setText(current + "/" + target);
	
	
	      convertView.setOnClickListener(new OnClickListener() {
	
	            @Override
	            public void onClick(View view) {
	
	                Intent intent = new Intent(getContext(), ViewGoal.class)
		                .putExtra("goal_id", goal.getObjectId())
		            	.putExtra("goal_owner", goal.getOwner().get("fullName").toString());
	
	                getContext().startActivity(intent);
	            }
	      });
	
	      return convertView;
	  }
	
//	 @Override
//	 public Filter getFilter() {
//	         if (goalFilter == null) 
//	               goalFilter = new GoalFilter();
//	
//	         return goalFilter;
//	 }
}