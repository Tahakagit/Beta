package com.example.franc.misteryapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class NavigationActivity extends AppCompatActivity {

    // trova la location del player
    // esegue una query delle location con filtro location player stella
    // passa il result a MyNavigationAdapter
    static String playerLocation;
    BroadcastReceiver  act2InitReceiver;
    RealmHelper helper = new RealmHelper();
    static MyNavigationAdapter navigationAdapter;
    WorldManagementHelper worldHelper = new WorldManagementHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_exploring);
        worldHelper.startUniverse();


        playerLocation = helper.getPlayerLocation();
        if (playerLocation == null)
            helper.setPlayerLocation(helper.getFirstStar());


        listOfLocations = helper.getPlacesAtPLayerPosition();

        listOfLocations.addChangeListener(new RealmChangeListener<RealmResults<LocationRealmObject>>() {
            @Override
            public void onChange(RealmResults<LocationRealmObject> locationRealmObjects) {
                navigationAdapter.notifyDataSetChanged();
            }
        });


        startRecyclerView();



        startFab();

        startNavDrawer();
    }

    //NAVIGATION DRAWER
    public void startNavDrawer(){
        final DrawerLayout mDrawerLayout;
        final Intent creaConto = new Intent(this, MainActivity.class);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            switch (menuItem.getItemId())
                            {
                                case R.id.action_category_1:
                                    startActivity(creaConto);
                                    break;
                                case R.id.action_category_2:
                                    //tabLayout.getTabAt(1).select();
                                    break;
                                case R.id.action_category_3:
                                    //tabLayout.getTabAt(2).select();
                            }

                            mDrawerLayout.closeDrawers();
                            return true;
                        }
                    });
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
/*
        listOfLocations = helper.getPlacesAtPLayerPosition();

        navigationAdapter = new MyNavigationAdapter(listOfLocations);
        navigationAdapter.notifyDataSetChanged();
*/
    }

    static RealmResults<LocationRealmObject> listOfLocations;

    // ritorna realmresult di location con stella in comune
    public void getLocation(){
        helper.getPlayerLocation();
        playerLocation = helper.getPlayerLocation();
        listOfLocations = helper.getPlacesAtPLayerPosition();
        listOfLocations.addChangeListener(new RealmChangeListener<RealmResults<LocationRealmObject>>() {
            @Override
            public void onChange(RealmResults<LocationRealmObject> locationRealmObjects) {
                navigationAdapter.notifyDataSetChanged();
            }
        });

    }

/*
    @Override
    public void sendLocation(String location) {
        startRecyclerView(helper.getLocationsAtStar(location));
    }
*/

    public void startRecyclerView(){
        navigationAdapter = new MyNavigationAdapter(listOfLocations);
        RecyclerView list;
        list = (RecyclerView)findViewById(R.id.navigation_rv);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(navigationAdapter);

    }

    public void startFab(){
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                final FloatingActionButton fab1 = findViewById(R.id.fab_1);

                final Animation show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.show_fab_1);

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
                layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
                layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
                fab1.setLayoutParams(layoutParams);

                fab1.startAnimation(show_fab_1);
                fab1.setClickable(true);
                fab1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent intent = new Intent(NavigationActivity.this, DialogActivity.class);
                        startActivity(intent);

                    }
                });

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(act2InitReceiver);

    }
}
