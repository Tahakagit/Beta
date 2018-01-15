package com.example.franc.misteryapp;

import android.provider.Settings;

import io.realm.RealmObject;

/**
 * Created by franc on 13/01/2018.
 */

public class Player extends RealmObject {

    private int health = 100;
    private String name;

    public Player() {
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
