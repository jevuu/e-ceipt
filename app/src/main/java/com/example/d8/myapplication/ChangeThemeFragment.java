package com.example.d8.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangeThemeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangeThemeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeThemeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

//    LinearLayout li;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChangeThemeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangeThemeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangeThemeFragment newInstance(String param1, String param2) {
        ChangeThemeFragment fragment = new ChangeThemeFragment();
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

        View v=inflater.inflate(R.layout.fragment_change_theme, container, false);


        ImageView imageViewDay = (ImageView) v.findViewById(R.id.imageViewDay);
        imageViewDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...Your Code
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                getActivity().recreate();

                SharedPreferences settings = getActivity().getSharedPreferences("Settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=settings.edit();
                editor.putInt("Theme_DayNight",AppCompatDelegate.MODE_NIGHT_NO);
                editor.commit();

                getActivity().recreate();

            }
        });

        ImageView imageViewNight = (ImageView) v.findViewById(R.id.imageViewNight);
        imageViewNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //...Your Code
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

//                li = (LinearLayout)v.findViewById(R.id.home_receipt_list_layout);
//                li.setBackgroundColor(Color.rgb(226, 11, 11));

                getActivity().recreate();


                SharedPreferences settings = getActivity().getSharedPreferences("Settings",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=settings.edit();
                editor.putInt("Theme_DayNight",AppCompatDelegate.MODE_NIGHT_YES);
                editor.commit();

                getActivity().recreate();

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
}
