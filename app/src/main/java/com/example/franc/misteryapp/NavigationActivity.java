package com.example.franc.misteryapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
        setContentView(R.layout.activity_navigation);
        worldHelper.startUniverse(1, 1, 1);


        startRecyclerView(getLocation());
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
        RecyclerView.LayoutManager mLayoutManager;
        list = (RecyclerView)findViewById(R.id.navigation_rv);
        list.setAdapter(navigationAdapter);
    }

}
