package com.example.franc.misteryapp;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by franc on 14/01/2018.
 */

public class WeaponSet extends RealmObject {

    String weaponName;
    int weaponDamage;
    @PrimaryKey
    private String weaponID;
    private int viewType;

    public String getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }

    public int getWeaponDamage() {
        return weaponDamage;
    }

    public void setWeaponDamage(int weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    public String getWeapondID() {
        return weaponID;
    }

    public void setWeapondID(String timestamp) {
        this.weaponID = timestamp;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
