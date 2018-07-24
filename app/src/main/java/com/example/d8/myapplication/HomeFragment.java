package com.example.d8.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.IDNA;
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
import java.util.Date;


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

    String USERID = Information.authUser.getUserId();
    String USERRECEIPTFILENAME = USERID+Information.RECEIPTSLOCALFILENAME;
    String USERNAME = Information.authUser.getName();

    View fragmentView=null;
    Button btn_add;
    Button btn_rec;
    ListView listView=null;
    TextView receiptsTotalCost;


    private String daysSpinnerSelect = "All receipts";
    private String cateSpinnerSelect = "All receipts";
    ArrayList<Receipt> receiptsSelect = new ArrayList<Receipt>();


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
                Intent goToRec = new Intent(getActivity(), AnalyzeActivity.class);
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

        getActivity().setTitle("Home");
        DataController.SyncronizeData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php", getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_home, container, false);
        fragmentView=v;

//        listView = (ListView)v.findViewById(R.id.receipts_list_view);

        //Adds OnClick Listeners to the lower buttons
        btn_add = (Button) v.findViewById(R.id.add_btn);
        btn_add.setOnClickListener(this);
        btn_rec = (Button) v.findViewById(R.id.analyze_btn);
        btn_rec.setOnClickListener(this);

        //getData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php");
        //getJSON("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php");
        //DataController.SyncronizeData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php", this);

        initCustomSpinner();

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //Toast.makeText(getActivity().getBaseContext(),""+position, Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getActivity(),ReceiptDetailActivity.class);
//                intent.putExtra("RECEIPTINDEX", Integer.toString(position));
//                startActivity(intent);
//            }
//        });


        listView = (ListView)v.findViewById(R.id.receipts_list_view);
        receiptsTotalCost = (TextView)v.findViewById(R.id.total_cost) ;

        //DataController.SyncronizeData("http://myvmlab.senecacollege.ca:6207/getUserReceipts.php", this);

        try{
            String json = DataController.readJsonFile(USERRECEIPTFILENAME, v.getContext());
            if(json.equals("null")){
                DataController.storeJsonToLocal("[]",USERRECEIPTFILENAME,getActivity());
            }
            Log.i("JSONHOME", json);

            if(!Information.receipts.isEmpty()){
                Information.receipts.clear();
            }
            DataController.loadReceiptsObj(json);

        }catch(JSONException e){
            Log.e("JSONERROR", e.toString());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(),""+position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(),ReceiptDetailActivity.class);
                intent.putExtra("RECEIPTINDEX", Integer.toString(position));
                startActivity(intent);
            }
        });

        //Initialize categories to user
        Information.categories.clear();
        Information.categories.add("All categories");

        for(int i=0; i<Information.receipts.size(); i++){
            String cateTemp = Information.receipts.get(i).getCategory();
            Log.i("CATETEST", cateTemp);
            for(int j=0; j<Information.categories.size(); j++){
                if(Information.categories.get(j).equals(cateTemp)){
                    break;
                }
                if(j==Information.categories.size()-1){
                    Information.categories.add(cateTemp);
                }
            }
        }

        initCustomSpinner();

        //test
        //DataController.deleteLocalFile(getActivity());

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
                daysSpinnerSelect = item;

                Log.i("ITEMSAaaaaa", item);
                double totalCost = 0.0;

                ArrayList<Receipt> receiptsSelect = new ArrayList<Receipt>();
                receiptsSelect = loadReceiptObjToListviewByDaysAndCate(Information.receipts,daysSpinnerSelect,cateSpinnerSelect);
                loadReceiptObjToListView(receiptsSelect);
//                if(item.equals("All receipts")){
//                    loadReceiptObjToListView(Information.receipts);
//
//                    for(Receipt receipt:Information.receipts){
//                        totalCost += receipt.getTotalCost();
//                    }
//                }else{
//                    String daysNumString = item.replaceAll("\\D+","");
//
//                    Integer daysNum = Integer.parseInt(daysNumString);
//
//                    //Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + daysNumString, Toast.LENGTH_LONG).show();
//                    ArrayList<Receipt> receipts = DataController.getReceiptsInDays(daysNum);
//
//                    loadReceiptObjToListView(receipts);
//
//                    for(Receipt receipt:receipts){
//                        totalCost += receipt.getTotalCost();
//                    }
//                    Log.i("TOTALCOST22222", Double.toString(totalCost));
//                }

                for(Receipt receipt:receiptsSelect){
                    totalCost += receipt.getTotalCost();
                }

                Log.i("TOTALCOST22222", String.format("%.2f", totalCost));
                receiptsTotalCost.setText(String.format("%.2f", totalCost));


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Set spinner for category select
        Spinner spinnerCate = (Spinner) fragmentView.findViewById(R.id.spinner_category_select);
        //ArrayList<String> resourceCate = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.home_spinner_category)));
        ArrayList<String> resourceCate = Information.categories;
        HomeFragment.CustomSpinnerAdapter customSpinnerAdapterCate=new HomeFragment.CustomSpinnerAdapter(getContext(),resourceCate);
        spinnerCate.setAdapter(customSpinnerAdapterCate);

        spinnerCate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                cateSpinnerSelect = item;

                double totalCost = 0.0;
                ArrayList<Receipt> receiptsSelect = new ArrayList<Receipt>();
                receiptsSelect = loadReceiptObjToListviewByDaysAndCate(Information.receipts,daysSpinnerSelect,cateSpinnerSelect);
                loadReceiptObjToListView(receiptsSelect);
                for(Receipt receipt:receiptsSelect){
                    totalCost += receipt.getTotalCost();
                }

                Log.i("TOTALCOST22222", String.format("%.2f", totalCost));
                receiptsTotalCost.setText(String.format("%.2f", totalCost));

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

    //Load the receipts data to listview(from object to listview)
    void loadReceiptObjToListView(ArrayList<Receipt> _receipts){
        if(!_receipts.isEmpty()){
            ReceiptListviewAdapter adapter = new ReceiptListviewAdapter(getActivity(),_receipts);
            listView.setAdapter(adapter);
        }else{
            String[] emptyString = {};
            ArrayAdapter<String> arrayAdapterEmpty = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, emptyString);
            listView.setAdapter(arrayAdapterEmpty);
            Log.i("NORECEIPT!","true");
        }
    }


    ArrayList<Receipt> loadReceiptObjToListviewByDaysAndCate(ArrayList<Receipt> _receipts, String daysSelect, String cateSelect){
        ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        if(daysSelect.equals("All receipts") && cateSelect.equals("All categories")){
            receipts = Information.receipts;
        }else if(daysSelect.equals("All receipts") && !cateSelect.equals("All categories")){
            receipts = DataController.getReceiptsInCategory(cateSelect);
        }else if(!daysSelect.equals("All receipts") && cateSelect.equals("All categories")){
            String daysNumString = daysSelect.replaceAll("\\D+","");
            Integer daysNum = Integer.parseInt(daysNumString);
            receipts = DataController.getReceiptsInDays(daysNum);
        }else if(!daysSelect.equals("All receipts") && !cateSelect.equals("All categories")){
            String daysNumString = daysSelect.replaceAll("\\D+","");
            Integer daysNum = Integer.parseInt(daysNumString);
            receipts = DataController.getReceiptsInDaysAndCategory(daysNum, cateSelect);
        }

        return receipts;

    }
}