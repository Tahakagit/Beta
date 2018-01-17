package com.example.franc.misteryapp;

import android.os.Handler;

import java.nio.charset.Charset;
import java.util.Random;

/**
 * Created by franc on 14/01/2018.
 */

public class Enemy{

    private String name = null;
    private int health = 20;
    private boolean isAdded = false;

    public Enemy(){

        name = generateName();
    }
    public String generateName() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
