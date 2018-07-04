package com.example.d8.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Receipt.Item>items;

    public ItemListViewAdapter(Context context, ArrayList<Receipt.Item> items) {
        this.context = context;
        this.items = items;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    public String getItemName(int position) {
        return items.get(position).getItemName();
    }

    public double getItemPrice(int position) {
        return items.get(position).getItemPrice();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate( R.layout.listview_items, parent, false );

        String classname = getItem(position).getClass().toString();
        Log.i("CLASSESSSS:",classname);

        final String itemName = getItemName(position);
        final String itemPrice = Double.toString(getItemPrice(position));

        TextView item_name = (TextView)v.findViewById(R.id.add_receipt_itemname_label);
        TextView item_price = (TextView)v.findViewById(R.id.add_receipt_itemprice_label);

        item_name.setText(itemName);
        item_price.setText(itemPrice);

//        final String itemName = getItem(position).getI();
//        final String receiptDate = getItem(position).getDate();
//        final String totalCost = String.format("%.2f",getItem(position).getTotalCost());

//
//        TextView company_name = (TextView)v.findViewById(R.id.business_name);
//        TextView receipt_date = (TextView)v.findViewById(R.id.receipt_date);
//        TextView receipt_totalcost = (TextView)v.findViewById(R.id.receipt_total_cost);
//
//        company_name.setText(companyName);
//        receipt_date.setText(receiptDate);
//        receipt_totalcost.setText(totalCost);

        return v;
    }
}
