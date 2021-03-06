package com.example.franc.misteryapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Enemy RealmObject
 */

public class AllEnemies extends RealmObject {

    private String name = null;
    private int health = 20;
    private String location = null;
    private boolean isSelected = false;
    @PrimaryKey
    private String id = null;
    private Boolean isAttacked = false;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getAttacked() {
        return isAttacked;
    }

    public void setAttacked(Boolean attacked) {
        isAttacked = attacked;
    }
}
