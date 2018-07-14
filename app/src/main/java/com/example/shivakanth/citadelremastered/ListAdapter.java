package com.example.shivakanth.citadelremastered;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import data.Data;

public class ListAdapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<DataS> attributes;

    public ListAdapter(Activity context, ArrayList<DataS> d){
        super(context,R.layout.detail_layout,d);
        this.context = context;
        this.attributes = d;
    }

    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.detail_layout,null , true);
        TextView h = rowView.findViewById(R.id.att);
        TextView s = rowView.findViewById(R.id.val);
        h.setText(attributes.get(position).head);
        s.setText(attributes.get(position).sub);
        return rowView;

    }
}

class DataS
{
    String head, sub;
}