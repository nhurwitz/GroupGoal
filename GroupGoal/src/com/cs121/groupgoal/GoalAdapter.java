
package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * This activity customizes an adapter for Goal posts.
 *
 */
public class GoalAdapter extends ArrayAdapter<GoalPost> implements Filterable{
	private Context mContext;
	private List<GoalPost> mGoals;
	private Filter goalFilter;
	private Filter categoryFilter;
	private List<GoalPost> finalGoals;
	
	private static class ViewHolder {
		TextView goalView;
	    TextView nameView;
	    TextView attendingView;
	}
	
	public GoalAdapter(Context context, List<GoalPost> objects) {
	    super(context, R.layout.anywall_post_item, objects);
	
	    this.mContext = context;
	    this.mGoals = objects;
	    this.finalGoals = objects;
	}
	
	@Override
	public int getCount() {
		return mGoals.size();
	}
		
	public View getView(int position, View convertView, ViewGroup parent) {
		  ViewHolder viewHolder;
		  
	      if(convertView == null){
	    	  viewHolder = new ViewHolder();
	          LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
	          convertView = mLayoutInflater.inflate(R.layout.anywall_post_item, null);
	          viewHolder.goalView = (TextView) convertView.findViewById(R.id.content_view);
		      viewHolder.nameView = (TextView) convertView.findViewById(R.id.username_view);
		      viewHolder.attendingView = (TextView) convertView.findViewById(R.id.goal_list_attending);
		      convertView.setTag(viewHolder);
	      } else {
	    	  viewHolder = (ViewHolder) convertView.getTag();
	      }
	     
	      final GoalPost goal = mGoals.get(position);
	      
	      if(mGoals.contains(goal)) {
	    	  String[] goalOwnerFirstLast = goal.getOwner().get("fullName").toString().split("\\^");
		      int target = (Integer) goal.getTargetGroupSize();
		      int current = (Integer) goal.getCurrentGroupSize();
		
		     
		
		      viewHolder.goalView.setText(goal.getName().toString());
		      viewHolder.nameView.setText(goalOwnerFirstLast[0] + " " + goalOwnerFirstLast[1]);
		      if(target == 0 || current/target >= 1) {
		    	  viewHolder.attendingView.setText("COMPLETE");
		    	  viewHolder.attendingView.setTextColor(Color.GREEN);
		      } else {
		    	  viewHolder.attendingView.setText(current + "/" + target);	
		    	  viewHolder.attendingView.setTextColor(Color.BLACK);
		      }
		     
		
		
		      convertView.setOnClickListener(new OnClickListener() {
		
		            @Override
		            public void onClick(View view) {
		
		                Intent intent = new Intent(getContext(), ViewGoal.class)
			                .putExtra("goal_id", goal.getObjectId())
			            	.putExtra("goal_owner", goal.getOwner().get("fullName").toString());
		
		                getContext().startActivity(intent);
		            }
		      });
	      }
	      
	
	      return convertView;
	  }
	
	 @Override
	 public Filter getFilter() {
	         if (goalFilter == null) 
	               goalFilter = new GoalFilter();
	
	         return goalFilter;
	 }
	 
	 public Filter getCategoryFilter() {
		 if(categoryFilter == null) {
			 categoryFilter = new CategoryFilter();
		 }
		 
		 return categoryFilter;
	 }
	 
	 public void resetData() {
	     mGoals = finalGoals;
	 }
	 	
	private class GoalFilter extends Filter {
		
	  @SuppressLint("DefaultLocale")
	  @Override
	  protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        // We implement here the filter logic
        if (constraint == null || constraint.length() == 0) {
                results.values = mGoals;
                results.count = mGoals.size();
        }
        else {
        		String con = constraint.toString().toUpperCase();
                // We perform filtering operation
                List<GoalPost> filterList = new ArrayList<GoalPost>();
                
                for (GoalPost a : mGoals) {
                    int subIndex = 0;
                	String name = a.getName().toString().toUpperCase();
                	for(int i = 0; i < name.length(); i++) {
                		if(subIndex == con.length()) {
                			break;
                		} else if(con.charAt(subIndex) == name.charAt(i)) {
                			subIndex++;
                		}
                	}
                	if(subIndex == con.length()) {
                		filterList.add(a);
                	}
                }

                results.values = filterList;
                results.count = filterList.size();

        }
        
        return results;
    }
	  
    @SuppressWarnings("unchecked")
    @Override
    protected void publishResults(CharSequence constraint,
	            FilterResults results) {
	    // Now we have to inform the adapter about the new list filtered
	    if (results.count == 0)
	        notifyDataSetInvalidated();
	    else {
	    	mGoals = (List<GoalPost>) results.values;
	        notifyDataSetChanged();
	    }
    }
  }
	private class CategoryFilter extends Filter {
		
		  @SuppressLint("DefaultLocale")
		  @Override
		  protected FilterResults performFiltering(CharSequence constraint) {
	        FilterResults results = new FilterResults();
	        // We implement here the filter logic
	        if (constraint == null || constraint.length() == 0) {
	                results.values = mGoals;
	                results.count = mGoals.size();
	        }
	        else {
	        		String con = constraint.toString().toUpperCase();
	                // We perform filtering operation
	                List<GoalPost> filterList = new ArrayList<GoalPost>();
	                
	                for (GoalPost a : mGoals) {
	                	if(a.getCategory().toString().toUpperCase().equals(con)) {
	                		filterList.add(a);
	                	}
	                }

	                results.values = filterList;
	                results.count = filterList.size();

	        }
	        
	        return results;
	    }
		  
	    @SuppressWarnings("unchecked")
	    @Override
	    protected void publishResults(CharSequence constraint,
		            FilterResults results) {
		    // Now we have to inform the adapter about the new list filtered
		    if (results.count == 0)
		        notifyDataSetInvalidated();
		    else {
		    	mGoals = (List<GoalPost>) results.values;
		        notifyDataSetChanged();
		    }
	    }
	  }
}