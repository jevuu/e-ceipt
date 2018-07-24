package com.example.d8.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AnalyzeListviewAdapter extends BaseAdapter {
    private Context context;
    private List<String> categories;
    private List<Double> values;

    TextView analyzeLabel;
    TextView analyzeValue;

    public AnalyzeListviewAdapter(Context context, List<String> categories, List<Double> values) {
        this.context    = context;
        this.categories = categories;
        this.values     = values;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public String getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate( R.layout.listview_analyze, parent, false );
        String labelName;
        double labelValue;

        analyzeLabel = (TextView) v.findViewById(R.id.analyze_label);
        analyzeValue = (TextView) v.findViewById(R.id.analyze_value);

        if(position != 0) {
            labelName = categories.get(position);
            labelValue = values.get(position);

            analyzeLabel.setText(labelName);
            analyzeValue.setText("$" + String.format("%.2f", labelValue));
        }

        return v;
    }
}
