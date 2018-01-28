package com.example.franc.misteryapp;

import android.util.Log;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;

/**
 * Created by franc on 23/01/2018.
 */

public class WorldManagementHelper {
    private Realm mRealm;
    private RealmHelper helper;

    public WorldManagementHelper(RealmHelper helper) {
        this.helper = helper;
    }

    public void startUniverse(){
        this.helper.resetUniverse();
        // creare routine di inserimento delle location
        int z = 1;

        for (int i = 0; i < 4; i++) {

            String sectorName = "Sector - " + randomIdentifier();
            for (int u = 0 ; u < 2 ; u++){
                String starName = "Star - " + randomIdentifier();
                for (int p = 0; p < 4; p++){
                    String locationName = "Location - " + randomIdentifier();
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
    // genera stringhe casuali
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

    public void spawnEnemy(){

        AllEnemies enemy = new AllEnemies();
        enemy.setName("ship - " + randomIdentifier());
        enemy.setLocation(helper.getRandomLocation());
        helper.addItem(enemy);
        Log.d("tag", "Spawning new enemy at   " + enemy.getLocation());



    }

}
