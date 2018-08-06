package com.example.d8.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,ProfileFragment.OnFragmentInteractionListener,
        SettingFragment.OnFragmentInteractionListener,AboutFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,ChangeThemeFragment.OnFragmentInteractionListener,
        ChangeBGColorFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment=null;
        Class fragmentClass = null;
        fragmentClass = HomeFragment.class;
        try
        {
            fragment = (Fragment)fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragmentContent, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Get data from shared preferences
        SharedPreferences settings=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        if(settings!=null) {
            int themeDayNight=settings.getInt("Theme_DayNight", AppCompatDelegate.MODE_NIGHT_NO);//Default day
            AppCompatDelegate.setDefaultNightMode(themeDayNight);

            //Set window's background color from saved settings.
            int bc = settings.getInt("Background_Color", Color.CYAN);//Default white color
            if(themeDayNight==AppCompatDelegate.MODE_NIGHT_YES)
                bc=Color.GRAY;
            navigationView.setBackgroundColor(bc);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.menu, menu);

            fetchProfile();

            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    //Sets the Email, username, and picture of the current user in the nav bar.
    // 6/10/2018
    public void fetchProfile(){

        TextView t = (TextView) findViewById(R.id.nav_head_Name);
        t.setText(Information.authUser.getName());

        ImageView im =  findViewById(R.id.nav_head_image);
        im.setImageResource(R.drawable.receiptsnap_logo);

        TextView te = (TextView) findViewById(R.id.nav_head_email);
        te.setText(Information.authUser.getEmail());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent goOption = new Intent(this, AddReceiptOptionActivity.class);
            startActivity(goOption);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment=null;
        Class fragmentClass = null;
        fetchProfile();

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;
        } else if (id == R.id.nav_setting) {
            fragmentClass = SettingFragment.class;
        } else if (id == R.id.nav_about) {
            fragmentClass = AboutFragment.class;
        } else if (id == R.id.nav_logout) {

            logout();
            return true;

        }

        try
        {
            fragment = (Fragment)fragmentClass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragmentContent, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(){
        //When signing out, this prevents the user 'backing' into the app.
        //finish() destroys the home activity as well.
        Information.authUser.signOut();
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("finish", true);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(myIntent);
        finish();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}












