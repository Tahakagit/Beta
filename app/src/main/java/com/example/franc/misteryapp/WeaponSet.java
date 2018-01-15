package com.example.franc.misteryapp;

import io.realm.RealmObject;

/**
 * Created by franc on 14/01/2018.
 */

public class WeaponSet extends RealmObject {

    String weaponName;
    int weaponDamage;

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
}
