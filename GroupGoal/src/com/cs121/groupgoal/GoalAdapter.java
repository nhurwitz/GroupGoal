
package com.cs121.groupgoal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class GoalAdapter extends ArrayAdapter<GoalPost> implements Filterable{
	private Context mContext;
	private List<GoalPost> mGoals;
	private ArrayAdapter<GoalPost> array;
	private Activity activity;
	private Filter goalFilter;
	private List<GoalPost> pGoals;
	
	public GoalAdapter(Context context, List<GoalPost> objects) {
	    super(context, R.layout.anywall_post_item, objects);
	
	    this.mContext = context;
	    this.mGoals = objects;
	    this.pGoals = objects;
	    filterPrivate();
	}
	
	private void filterPrivate() {
		Iterator<GoalPost> i = pGoals.iterator();
		while(i.hasNext()) {
			GoalPost g = (GoalPost) i.next();
			if(g.isPrivate()) {
				// #TODO (nhurwitz) also check with invites/friends after feature
				// is implemented.
				if(g.getAttendees().contains(ParseUser.getCurrentUser()
						.getObjectId().toString())) {
					i.remove();
				}
			}
		}
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
	      if(convertView == null){
	          LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
	          convertView = mLayoutInflater.inflate(R.layout.anywall_post_item, null);
	      }
	
	      Log.i("Goal Index/Length:", mGoals.size() + "   " + position);
	      final GoalPost goal = mGoals.get(position);
	      
	      if(pGoals.contains(goal)) {
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
	      }
	      
	
	      return convertView;
	  }
	
	 @Override
	 public Filter getFilter() {
	         if (goalFilter == null) 
	               goalFilter = new GoalFilter();
	
	         return goalFilter;
	 }
	 
	 public void resetData() {
	     mGoals = mGoals;
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
                // We perform filtering operation
                List<GoalPost> filterList = new ArrayList<GoalPost>();

                for (GoalPost a : mGoals) {
                    if (a.getCategory().toString().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                            filterList.add(a);
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