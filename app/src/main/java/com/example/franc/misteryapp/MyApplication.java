package com.example.franc.misteryapp;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by franc on 08/11/2017.
 */

public class MyApplication extends Application {
    static String playerLocation;
    Enemy enemy = new Enemy();


    RealmHelper helper = new RealmHelper();

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());


        startUniverse();

        isPlayer();
        Enemy enemyControl = new Enemy();

        enemyControl.startSpawnEnemies();
/*
        new SpawnEnemy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
*/


    }

    // PLAYER INIT
    public boolean isPlayer(){
        /**
         * Check if player already exists if not create it and set
         * location
         *
         */
        Player player = helper.getPlayer();
        helper.resetLocation();
        /*
        helper.resetLocation();
*/
        helper.restorePlayerHealth();
/*
        if (helper.getPlayer() == null){
            Player player = new Player();
            helper.addItem(player);
        }else {
            helper.resetLocation();
            helper.restoreHealth(helper.getPlayer());
        }
*/
/*
        playerLocation = player.getLocation();
*/
/*
        if (helper.getPlayerLocation() == null)
            helper.setPlayerLocation(helper.getRandomLocation());
*/

        return true;
    }

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


    // GET RANDOM LOCATION
    String getRandomLocation(){
        Realm mRealm = Realm.getDefaultInstance();

        Random r = new Random();
        String randomStarSystem;
        try {
            int getSize = mRealm.where(LocationRealmObject.class).findAll().size();
            int Low = 10;
            int High = getSize;
            int Result = r.nextInt(High-Low) + Low;
            RealmResults<LocationRealmObject> location = mRealm.where(LocationRealmObject.class).equalTo("locationId", Result).findAll();
            randomStarSystem = location.get(0).getLocationStar();
        } finally {
            mRealm.close();
        }
        return randomStarSystem;
    }


}
