package com.example.d8.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";




    Button editBtn;
    Button cancBtn;
    Button confBtn;

    TextView username;
    TextView Cemail;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        getActivity().setTitle("Profile");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_profile, container, false);


      //Fetch the XML objects for usage
        username = (EditText)v.findViewById(R.id.profile_username);
        Cemail = (EditText)v.findViewById(R.id.profile_email);

        editBtn = (Button)v.findViewById(R.id.profile_unlockBtn);
        editBtn.setOnClickListener(this);
        confBtn = (Button)v.findViewById(R.id.profile_confirm);
        confBtn.setOnClickListener(this);

        cancBtn = (Button)v.findViewById(R.id.profile_cancel);
        cancBtn.setOnClickListener(this);

        cancBtn.setVisibility(View.INVISIBLE);
        confBtn.setVisibility(View.INVISIBLE);

        username.setEnabled(false);
        Cemail.setEnabled(false);

        username.setText(Information.authUser.getName());
        Cemail.setText(Information.authUser.getEmail());

        return v;
    }
    //This function handles click events for every button in the fragment.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_unlockBtn:
                username.setEnabled(true);
                Cemail.setEnabled(true);

                cancBtn.setVisibility(View.VISIBLE);
                confBtn.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.INVISIBLE);
                break;
            case R.id.profile_confirm:
                username.setEnabled(false);
                Cemail.setEnabled(false);

                cancBtn.setVisibility(View.INVISIBLE);
                confBtn.setVisibility(View.INVISIBLE);
                editBtn.setVisibility(View.VISIBLE);

                String a = username.getText().toString();
                String b = Cemail.getText().toString();

                //Updates the profile, this disgusts me
                updateProfile(a, b);




                break;
            case R.id.profile_cancel:
                username.setEnabled(false);
                Cemail.setEnabled(false);

                cancBtn.setVisibility(View.INVISIBLE);
                confBtn.setVisibility(View.INVISIBLE);
                editBtn.setVisibility(View.VISIBLE);

                break;
            default:
                break;


        }

    }
    void updateProfile(@Nullable String name, @Nullable String email) {
        if (name != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            Information.authUser.mUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                System.out.println("Profile Updated!");
                                Information.authUser.setName(name);

                            } else {
                                System.out.println("PAIN IN PROFILE!");
                                Toast.makeText(getContext(), "Your Display Name was NOT Updated!" + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                                username.setText(Information.authUser.getName());

                            }
                        }
                    });

        }
        if( (email != null && !email.isEmpty()) && !email.equals(Information.authUser.getEmail()) && !Information.authUser.isPhone()){
           Information.authUser.mUser.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email Updated!");
                                Toast.makeText(getContext(), "Your Email was updated and changes will be reflected in the next login!",
                                        Toast.LENGTH_LONG).show();

                                Intent myIntent = new Intent(getContext(), MainActivity.class);
                                myIntent.putExtra("finish", true);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(myIntent);



                            }else{
                                System.out.println("PAIN IN EMAIL! " + task.getException());
                                Toast.makeText(getContext(), "Your Email could not be updated!" + task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                                Cemail.setText(Information.authUser.getEmail());

                                Cemail.setTextColor(getResources().getColor(R.color.colorAccent));


                            }
                        }
                    });


        }

        if(Information.authUser.isPhone()){
            Toast.makeText(getContext(), "Phone Users do not have an email!",
                    Toast.LENGTH_LONG).show();
        }
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
