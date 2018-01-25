package com.example.franc.misteryapp;

import android.provider.Settings;

import io.realm.RealmObject;

/**
 * Created by franc on 13/01/2018.
 */

public class Player extends RealmObject {

    private int health = 100;
    private String name = null;
    private String location = null;

    public Player() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
