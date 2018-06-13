package com.example.d8.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


//The Home activity is implemented by the Home fragment, though to be honest this seems like redundant code.
//To implement buttons: go to OnClick overide function
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View fragmentView=null;
    Button btn_add;
    Button btn_rec;
    ListView listView=null;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    //This function handles click events for every button in the fragment, implement your intents here guys!!!
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                Intent goToAdd = new Intent(getActivity(), AddReceiptOptionActivity.class);
                startActivity(goToAdd);
                break;
            case R.id.analyze_btn:
                Intent goToRec = new Intent(getActivity(), AddReceiptFormActivity.class);
                startActivity(goToRec);
            default:
                break;
        }

    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_home, container, false);
        fragmentView=v;

        listView = (ListView)v.findViewById(R.id.receipts_list_view);

        //Adds OnClick Listeners to the lower buttons
        btn_add = (Button) v.findViewById(R.id.add_btn);
        btn_add.setOnClickListener(this);
        btn_rec = (Button) v.findViewById(R.id.analyze_btn);
        btn_rec.setOnClickListener(this);

        //getData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php");
        getJSON("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php");
        initCustomSpinner();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity().getBaseContext(),""+position, Toast.LENGTH_LONG).show();
            }
        });



        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initCustomSpinner() {
        //Set spinner for day number select
        Spinner spinnerDay = (Spinner) fragmentView.findViewById(R.id.spinner_daysnum_select);
        ArrayList<String> resourceDay = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.home_spinner_days_select)));
        HomeFragment.CustomSpinnerAdapter customSpinnerAdapterDay=new HomeFragment.CustomSpinnerAdapter(getContext(),resourceDay);
        spinnerDay.setAdapter(customSpinnerAdapterDay);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Set spinner for category select
        Spinner spinnerCate = (Spinner) fragmentView.findViewById(R.id.spinner_category_select);
        ArrayList<String> resourceCate = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.home_spinner_category)));
        HomeFragment.CustomSpinnerAdapter customSpinnerAdapterCate=new HomeFragment.CustomSpinnerAdapter(getContext(),resourceCate);
        spinnerCate.setAdapter(customSpinnerAdapterCate);

        spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
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
            TextView txt = new TextView(getContext());
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.LEFT);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(getContext());
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            //txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }
    }

    private void getData(final String urlWebService) {

        try{
            URL url = new URL(urlWebService);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
        }catch(Exception e){

        }
    }


    //code from "https://www.simplifiedcoding.net/android-json-parsing-tutorial/"
    //this method is actually fetching the json string
    private void getJSON(final String urlWebService) {
        /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */
        class GetJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                try{
                    loadIntoListView(s);
                }catch(Exception e){

                }

                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "dhfjshjfs", Toast.LENGTH_SHORT).show();
                //Log.i("Json:" , s.toString());
            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {

                try {
                    //creating a URL
                    Log.i("HHHHHHHH","wwwww");
                    URL url = new URL(urlWebService);


                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("GET");
                    con.connect();
                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    Log.i("HHHHHHHH","aaa");
                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {
                        Log.d("HHHHHHHH", json);
                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();
                } catch (Exception e) {
                    Log.i("FAIL222",e.toString());
                    return null;
                }

            }
        }


        //creating asynctask object and executing it
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }


    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] receipts = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            //receipts[i] = obj.getString("receiptID")+"   "+obj.getString("date")+"  "+obj.getString("totalCost");
            receipts[i] = String.format("%-35s%-12s%20s",obj.getString("receiptID"), obj.getString("date"), obj.getString("totalCost"));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, receipts);
        listView.setAdapter(arrayAdapter);
    }
}
