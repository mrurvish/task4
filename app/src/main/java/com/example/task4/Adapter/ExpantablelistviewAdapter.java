package com.example.task4.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.task4.R;

import java.util.HashMap;
import java.util.List;

public class ExpantablelistviewAdapter extends BaseExpandableListAdapter {
    private List<String> chapter;

    ImageView indicator;
    private Context context;
    private HashMap<String, List<String>> topiclist;

    public ExpantablelistviewAdapter(Context context, List<String> chapter, HashMap<String, List<String>> topiclist) {
        this.chapter = chapter;
        this.context = context;
        this.topiclist = topiclist;
    }

    @Override
    public int getGroupCount() {
        return this.chapter.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (topiclist.get(chapter.get(groupPosition)) == null)
            return 0;
        else
            return this.topiclist.get(this.chapter.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.chapter.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.topiclist.get(this.chapter.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String titel = (String) getGroup(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group, null);


        }
        indicator = convertView.findViewById(R.id.groupIndicator);
        indicator.setImageResource(isExpanded ? R.drawable.baseline_keyboard_arrow_up_24 : R.drawable.baseline_keyboard_arrow_down_24);
        TextView textTitle = (TextView) convertView.findViewById(R.id.listTitle);
        textTitle.setText(titel);
        List<String> check = topiclist.get(this.chapter.get(groupPosition));

        if ( check != null) {
            // Hide the indicator for the first group
            indicator.setVisibility(View.VISIBLE);
        } else {
            // Show the indicator for other groups
            indicator.setVisibility(View.GONE);
        }


        return convertView;
}
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String titel = (String) getChild(groupPosition,childPosition);
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.child,null);

        }
        TextView textchild= (TextView) convertView.findViewById(R.id.expandedListItem);
        textchild.setText(titel);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
