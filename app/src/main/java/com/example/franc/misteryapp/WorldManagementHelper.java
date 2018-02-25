package com.example.franc.misteryapp;

import android.util.Log;
import android.widget.Toast;

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

    // GENERATE RANDOM STRING
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

        mRealm = helper.getRealm();
        String newLocation = helper.getRandomLocation();

        Enemy enemy = new Enemy("ship - " + randomIdentifier(), randomIdentifier(), newLocation);
        Log.d("Enemy AI spawn", "Spawning " + enemy.getName() + " at   " + newLocation);
    }
    public void deleteEnemy(AllEnemies enemy){

        helper.delItem(enemy);
    }

}
