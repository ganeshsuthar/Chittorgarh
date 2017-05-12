package com.m.chittorgarh.Adaptors;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.m.chittorgarh.Halper.AnimatedExpandableListView;
import com.m.chittorgarh.Halper.FormattedTextView;
import com.m.chittorgarh.Model.NavData;
import com.m.chittorgarh.Model.NevSubData;
import com.m.chittorgarh.R;
import com.m.chittorgarh.View.Activities.Home;

import java.util.ArrayList;

public class NavAdaptor extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<NavData> groups;
    ArrayList<NevSubData> chList;
    public NavAdaptor(Context context, ArrayList<NavData> groups) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.groups = groups;
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<NevSubData> chList = groups.get(groupPosition).getSubTitile();
        return chList.get(childPosition);
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final NevSubData child = (NevSubData) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            convertView.setTag(child);
        }
        FormattedTextView textView = (FormattedTextView) convertView.findViewById(R.id.sub);
        textView.setText(child.getName());
        return convertView;
    }
    @Override
    public int getRealChildrenCount(int groupPosition) {
         chList = groups.get(groupPosition).getSubTitile();
        return chList.size();
    }
    @Override
    public int getGroupCount() {
        return groups.size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition , boolean isExpanded, View convertView, ViewGroup parent) {
        NavData group = (NavData) getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_item, parent, false);
        }
        FormattedTextView title = (FormattedTextView) convertView.findViewById(R.id.textTitle);
        ImageView imageView= (ImageView)convertView.findViewById(R.id.indicator);
        title.setText(group.getTitle());

        if (getRealChildrenCount(groupPosition)==0){
              imageView.setVisibility(View.GONE);
        }else {
            imageView.setVisibility(View.VISIBLE);
            if (isExpanded){
                imageView.setImageResource(R.drawable.ic_expand_less);
            }else {
                imageView.setImageResource(R.drawable.ic_expand_more);
            }
        }
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
    @Override
    public boolean hasStableIds() {
        return true;
    }
    private void sendData(Context context, Fragment fragment, String data){
        Bundle bundle = new Bundle();
        bundle.putString("data",data);
        fragment.setArguments(bundle);
        ((Home)context).changeFragment(fragment,"product");
    }
}
