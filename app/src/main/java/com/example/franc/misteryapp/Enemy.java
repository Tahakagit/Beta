package com.example.franc.misteryapp;

/**
 * Created by franc on 14/01/2018.
 */

public class Enemy{

    private String name = null;
    private int health = 20;
    private boolean isAdded = false;
    private boolean isSelected = false;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void getDamage(int damage){
        this.health = this.health - damage;
    }
}
