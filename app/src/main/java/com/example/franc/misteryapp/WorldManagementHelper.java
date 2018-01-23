package com.example.franc.misteryapp;

import io.realm.Realm;

/**
 * Created by franc on 23/01/2018.
 */

public class WorldManagementHelper {
    private Realm mRealm;
    RealmHelper helper;

    public void startUniverse(int sector, int star, int location){
        helper = new RealmHelper();
        // creare routine di inserimento delle location
        for (int i = 0; i < location+1; i++) {
            LocationRealmObject place = new LocationRealmObject();
            place.setLocationName("Pippo");
            place.setLocationStar("XDE-23");
            place.setLocationName("Stazione Spaziale");
            helper.addItem(place);

        }

    }
}
