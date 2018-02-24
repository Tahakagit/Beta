package com.example.franc.misteryapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class NavigationActivity extends AppCompatActivity implements MenuFragmentGoTo.SendToDialogActivity{

    /**
     *
     *  Show player current starsystem's locations and ships
     *
     * */


    static String playerLocation;
    static RealmHelper helper;
    static MyNavigationAdapter navigationAdapter;
    static MyNavigationEnemyAdapter navigationEnemyAdapter;
    static WorldManagementHelper worldHelper;
    static RealmResults<LocationRealmObject> listOfLocations;
    static Context context;
    boolean fabClicked = false;

    static SpawnEnemyService enemyService;

    static Player  player = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_exploring);
        helper = new RealmHelper();
/*
        startUniverse();
*/

        Realm mRealm = Realm.getDefaultInstance();

        context = this;
/*
        isPlayer();
*/


        listOfLocations = helper.getPlacesAtPLayerPosition();
        listOfLocations.addChangeListener(new RealmChangeListener<RealmResults<LocationRealmObject>>() {
            @Override
            public void onChange(RealmResults<LocationRealmObject> locationRealmObjects) {
                navigationAdapter.notifyDataSetChanged();
            }
        });
/*
        startEnemyLifeService();
*/
/*
        startPlayerMenu();
*/
        FragmentManager fragmentManager = getSupportFragmentManager();

        FloatingActionButton fab = findViewById(R.id.fab);
        PlayerSuperControlCommandOfDestiny controlCommandOfDestiny = new PlayerSuperControlCommandOfDestiny(helper, context, fragmentManager);
        controlCommandOfDestiny.startFab(fab);
        LinearLayout ll = findViewById(R.id.bottom_sheet);
        ViewPager vPager = findViewById(R.id.viewpager_player_menu);
        controlCommandOfDestiny.startPlayerMenu(ll, vPager);
        startLocationsRecyclerView();
        startEnemiesRecyclerView();
/*
        startFab();
*/
        startNavDrawer();

        mRealm.close();
    }

/*
    public void startUniverse(){
        this.helper.resetUniverse();
        // creare routine di inserimento delle location
        int z = 1;

        for (int i = 0; i < 4; i++) {

            String sectorName = "SECTOR - " + randomIdentifier();
            for (int u = 0 ; u < 2 ; u++){
                String starName = "STAR - " + randomIdentifier();
                for (int p = 0; p < 4; p++){
                    String locationName = "LOCATION - " + randomIdentifier();
                    LocationRealmObject location = new LocationRealmObject();
                    location.setLocationName(locationName);
                    location.setLocationStar(starName);
                    location.setLocationSector(sectorName);
                    location.setLocationId(z);
                    this.helper.addItem(location);
                    z++;
                }
            }

        }

    }
*/
/*
    // GENERA STRINGHE CASUALI
    public String randomIdentifier() {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
        final Set<String> identifiers = new HashSet<String>();

        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }
*/

    /**
     * SET PLAYER NEW LOCATION AND UPDATES UI
     * IFACE CALLED FROM DIALOGFRAMENTGOTO
     * @param star String new location to navigate to
     */
    public void navigateTo(String star) {
        RealmHelper helper = new RealmHelper();
        helper.setPlayerLocation(star);
        NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
        NavigationActivity.navigationAdapter.UpdateAdapter(helper.getPlacesAtPLayerPosition());
/*
        finish();
*/
    }

    //NAVIGATION DRAWER
    public void startNavDrawer(){
        final DrawerLayout mDrawerLayout;
        final Intent creaConto = new Intent(this, BattleActivity.class);

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

/*
    */
/**
     * START ENEMY SPAWN SERVICE
     *//*

    public void startEnemyLifeService(){
        Context ctx = getApplicationContext();
        Calendar cal = Calendar.getInstance();
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.add(Calendar.SECOND, 2); // first time

        long interval = 2 * 1000; // 5 minutes in milliseconds
        Intent serviceIntent = new Intent(ctx, SpawnEnemyService.class);
        PendingIntent servicePendingIntent =
                PendingIntent.getService(ctx,
                        SpawnEnemyService.SERVICE_ID, // integer constant used to identify the service
                        serviceIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);  // FLAG to avoid creating a second service if there's already one running
        am.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                cal.getTimeInMillis(),
                interval,
                servicePendingIntent
        );

    }
*/








    // START LOCATIONS RECYCLERVIEW
    public void startLocationsRecyclerView(){
        navigationAdapter = new MyNavigationAdapter(listOfLocations);
        RecyclerView list;
        list = (RecyclerView)findViewById(R.id.navigation_rv_locations);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(navigationAdapter);

    }

    // START ENEMIES RECYCLERVIEW
    public void startEnemiesRecyclerView(){
        navigationEnemyAdapter = new MyNavigationEnemyAdapter(helper.getEnemiesAtPLayerPosition());
        RecyclerView list;
        list = (RecyclerView)findViewById(R.id.navigation_rv_enemies);
        list.setLayoutManager(new GridLayoutManager(this, 4));
        list.setAdapter(navigationEnemyAdapter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "OnStop called on " + this.getLocalClassName(), Toast.LENGTH_LONG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
/*
        mRealm.close();
*/

    }
}
