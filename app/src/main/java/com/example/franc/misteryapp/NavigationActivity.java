package com.example.franc.misteryapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class NavigationActivity extends AppCompatActivity {

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
    static RealmResults<AllEnemies> listOfEnemies;
    static Context context;

    static SpawnEnemyService enemyService;

    static Player  player = null;
/*
    Realm mRealm = null;
*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_exploring);
        helper = new RealmHelper();
/*
        worldHelper = new WorldManagementHelper(helper);
        worldHelper.startUniverse();
*/

        Realm mRealm = Realm.getDefaultInstance();

        context = this;

        helper.resetEnemies();
        isPlayer();
        playerLocation = helper.getPlayerLocation();
        if (playerLocation == null)
            helper.setPlayerLocation(helper.getFirstStar());


        listOfLocations = helper.getPlacesAtPLayerPosition();
/*
        listOfEnemies = mRealm.where(AllEnemies.class).findAll();
*/
        listOfLocations.addChangeListener(new RealmChangeListener<RealmResults<LocationRealmObject>>() {
            @Override
            public void onChange(RealmResults<LocationRealmObject> locationRealmObjects) {
                navigationAdapter.notifyDataSetChanged();
            }
        });
        startEnemyLifeService();
        startLocationsRecyclerView();
        startEnemiesRecyclerView();
        startFab();
        startNavDrawer();

        mRealm.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    // inizializza il Player
    public boolean isPlayer(){
        Realm mRealm = Realm.getDefaultInstance();
        try {
            player = helper.getPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (player == null){
            Player player = new Player();
            helper.addItem(player);
        }else {
            helper.resetLocation();
            helper.restoreHealth(player);
        }
        mRealm.close();
        return true;
    }

    public void startEnemyLifeService(){
        Context ctx = getApplicationContext();
/** this gives us the time for the first trigger.  */
        Calendar cal = Calendar.getInstance();
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        long interval = 2000; // 5 minutes in milliseconds
        Intent serviceIntent = new Intent(ctx, SpawnEnemyService.class);
// make sure you **don't** use *PendingIntent.getBroadcast*, it wouldn't work
        PendingIntent servicePendingIntent =
                PendingIntent.getService(ctx,
                        SpawnEnemyService.SERVICE_ID, // integer constant used to identify the service
                        serviceIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);  // FLAG to avoid creating a second service if there's already one running
// there are other options like setInexactRepeating, check the docs
        am.setRepeating(
                AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                cal.getTimeInMillis(),
                interval,
                servicePendingIntent
        );

    }

    public void startLocationsRecyclerView(){
        navigationAdapter = new MyNavigationAdapter(listOfLocations);
        RecyclerView list;
        list = (RecyclerView)findViewById(R.id.navigation_rv_locations);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(navigationAdapter);

    }

    public void startEnemiesRecyclerView(){
        Realm mRealm = Realm.getDefaultInstance();

        RealmResults<AllEnemies> allEnemiesAtPlayerPosition = helper.getEnemiesAtPLayerPosition();
        navigationEnemyAdapter = new MyNavigationEnemyAdapter(allEnemiesAtPlayerPosition);
        RecyclerView list;
        list = (RecyclerView)findViewById(R.id.navigation_rv_enemies);
        list.setLayoutManager(new GridLayoutManager(this, 4));
        list.setAdapter(navigationEnemyAdapter);
/*
        listOfEnemies.addChangeListener(new RealmChangeListener<RealmResults<AllEnemies>>() {
            @Override
            public void onChange(RealmResults<AllEnemies> allEnemies) {

                navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
            }
        });
*/
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
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "OnResume called on " + this.getLocalClassName(), Toast.LENGTH_LONG);

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
