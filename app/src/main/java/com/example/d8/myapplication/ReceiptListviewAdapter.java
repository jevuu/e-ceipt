package com.example.d8.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ReceiptListviewAdapter extends BaseAdapter {
    private Context context;
    private List<Receipt> receipts;



    public ReceiptListviewAdapter(Context context, List<Receipt> receipts) {
        this.context = context;
        this.receipts = receipts;
    }


    @Override
    public int getCount() {
        return receipts.size();
    }

    @Override
    public Receipt getItem(int position) {
        return receipts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate( R.layout.listview_receipt, parent, false );
        final String companyName = getItem(position).getBusinessName();
        final String receiptDate = getItem(position).getDate();
        final String totalCost = String.format("%.2f",getItem(position).getTotalCost());


        TextView company_name = (TextView)v.findViewById(R.id.business_name);
        TextView receipt_date = (TextView)v.findViewById(R.id.receipt_date);
        TextView receipt_totalcost = (TextView)v.findViewById(R.id.receipt_total_cost);

        company_name.setText(companyName);
        receipt_date.setText(receiptDate);
        receipt_totalcost.setText(totalCost);

        return v;
    }
}
