package com.example.d8.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initCustomSpinner();



    }

//    private void initSpinner(){
//        //Set spinner for day number select
//        Spinner spinnerDay = (Spinner) findViewById(R.id.spinner_daysnum_select);
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapterDay = ArrayAdapter.createFromResource(this,
//                R.array.home_spinner_days_select, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapterDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinnerDay.setAdapter(adapterDay);
//
//
//
//        //Set spinner for category select
//        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinner_category_select);
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapterCategory = ArrayAdapter.createFromResource(this,
//                R.array.home_spinner_category, android.R.layout.simple_spinner_item);
//        // Specify the layout to use when the list of choices appears
//        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinnerCategory.setAdapter(adapterCategory);
//    }

    private void initCustomSpinner() {
        //Set spinner for day number select
        Spinner spinnerDay = (Spinner) findViewById(R.id.spinner_daysnum_select);
        ArrayList<String> resourceDay = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.home_spinner_days_select)));
        CustomSpinnerAdapter customSpinnerAdapterDay=new CustomSpinnerAdapter(HomeActivity.this,resourceDay);
        spinnerDay.setAdapter(customSpinnerAdapterDay);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Set spinner for category select
        Spinner spinnerCate = (Spinner) findViewById(R.id.spinner_category_select);
        ArrayList<String> resourceCate = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.home_spinner_category)));
        CustomSpinnerAdapter customSpinnerAdapterCate=new CustomSpinnerAdapter(HomeActivity.this,resourceCate);
        spinnerCate.setAdapter(customSpinnerAdapterCate);

        spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter{
        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context,ArrayList<String> asr) {
            this.asr=asr;
            activity = context;
        }

        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(HomeActivity.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(HomeActivity.this);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            //txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }
    }





}
