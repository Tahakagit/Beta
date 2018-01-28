package com.example.franc.misteryapp;

import io.realm.RealmObject;

/**
 * Created by franc on 23/01/2018.
 */



public class LocationRealmObject extends RealmObject {

    private String locationName = null;
    private String locationStar = null;
    private String locationSector = null;
    private int locationId = 0;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationStar() {
        return locationStar;
    }

    public void setLocationStar(String locationStar) {
        this.locationStar = locationStar;
    }

    public String getLocationSector() {
        return locationSector;
    }

    public void setLocationSector(String locationSector) {
        this.locationSector = locationSector;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }
}
