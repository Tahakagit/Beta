package com.example.franc.misteryapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import io.realm.RealmResults;

public class NavigationActivity extends AppCompatActivity {

    // trova la location del player
    // esegue una query delle location con filtro location player stella
    // passa il result a MyNavigationAdapter
    String playerLocation;
    RealmHelper helper = new RealmHelper();
    WorldManagementHelper worldHelper = new WorldManagementHelper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_exploring);
        worldHelper.startUniverse(1, 1, 1);



        startFab();

        startRecyclerView(getLocation());
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


    // ritorna realmresult di location con stella in comune
    public RealmResults<LocationRealmObject> getLocation(){
        helper.getPlayerLocation();
        playerLocation = helper.getPlayerLocation();
        RealmResults<LocationRealmObject> listOfLocations = helper.getLocationsAtStar(playerLocation);
        return  listOfLocations;
    }

    public void startRecyclerView(RealmResults<LocationRealmObject> locations){
        MyNavigationAdapter navigationAdapter = new MyNavigationAdapter(locations);
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

            }
        });

    }


}
