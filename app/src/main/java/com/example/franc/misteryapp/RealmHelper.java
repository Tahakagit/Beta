package com.example.franc.misteryapp;

import android.webkit.WebMessagePort;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by franc on 09/01/2018.
 */

public class RealmHelper {

    private Realm mRealm;
    public RealmHelper() {}

    public Player getPlayer(){

        mRealm = Realm.getDefaultInstance();
        Player getPlayer = mRealm.where(Player.class).findAll().first();
        return getPlayer;

    }
    public void resetLocation(){
        String position;
        mRealm = Realm.getDefaultInstance();
        final Player getPlayer = mRealm.where(Player.class).findAll().first();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getPlayer.setLocation(null);

            }
        });

        mRealm.close();

    }
    public String getPlayerLocation(){

        String position;
        mRealm = Realm.getDefaultInstance();
        Player getPlayer = mRealm.where(Player.class).findAll().first();
        position = getPlayer.getLocation();
        mRealm.close();
        return position;

    }

    public void setPlayerLocation(final String firstStar){

        mRealm = Realm.getDefaultInstance();
        final Player player = mRealm.where(Player.class).findAll().first();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                player.setLocation(firstStar);

            }
        });
        mRealm.close();

    }


    public RealmResults<LocationRealmObject> getPlacesAtPLayerPosition(){

        mRealm = Realm.getDefaultInstance();

        RealmResults<LocationRealmObject> listOfPlaces= mRealm.where(LocationRealmObject.class).equalTo("locationStar", getPlayerLocation()).findAll();

        mRealm.close();
        return listOfPlaces;
    }

    public int getPlayerHealth(){

        int health;
        mRealm = Realm.getDefaultInstance();
        Player getPlayer = mRealm.where(Player.class).findAll().first();
        health = getPlayer.getHealth();
        mRealm.close();
        return health;

    }

    public void resetWeapons(){

        try {
            mRealm = Realm.getDefaultInstance();

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<WeaponSet> getWeapons = mRealm.where(WeaponSet.class).findAll();
                    getWeapons.deleteAllFromRealm();

                }
            });

        } finally {
            mRealm.close();

        }

    }

    public void resetUniverse(){

        try {
            mRealm = Realm.getDefaultInstance();

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<LocationRealmObject> getWeapons = mRealm.where(LocationRealmObject.class).findAll();
                    getWeapons.deleteAllFromRealm();

                }
            });

        } finally {
            mRealm.close();

        }

    }

    public RealmResults<WeaponSet> getWeapons(){

        mRealm = Realm.getDefaultInstance();
        RealmResults<WeaponSet> getWeapons = mRealm.where(WeaponSet.class).findAll();
        return getWeapons;

    }


    public int removeWeapondAt(int index){

        int weaponPower;
        try {
            mRealm = Realm.getDefaultInstance();
            RealmResults<WeaponSet> resultWeapon = mRealm.where(WeaponSet.class).findAll();
            final WeaponSet weapon = resultWeapon.get(index);
            weaponPower = weapon.getWeaponDamage();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    weapon.deleteFromRealm();
                }
            });
        } finally {
            if(mRealm != null) {
                mRealm.close();
            }
        }

        return weaponPower;
    }

    public void addItem(final RealmObject item){

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(item);
                }
            });
        } finally {
            if(mRealm != null) {
                mRealm.close();
            }
        }
    }

    public void addWeapon(final RealmObject item){

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(item);
                }
            });
        } finally {
            if(mRealm != null) {
                mRealm.close();
            }
        }
    }


    public void restoreHealth(final Player item){

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    item.setHealth(100);
                    realm.insertOrUpdate(item);
                }
            });
        } finally {
            if(mRealm != null) {
                mRealm.close();
            }
        }
    }

    public void setFirst(){

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                WeaponSet realFirst = mRealm.where(WeaponSet.class).findFirst();

                @Override
                public void execute(Realm realm) {
                    realFirst.setViewType(0);
                    realm.insertOrUpdate(realFirst);
                }
            });
        } finally {
            if(mRealm != null) {
                mRealm.close();
            }
        }
    }


    public void dealDamage(final Player item, final int damage){

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    item.setHealth(item.getHealth() - damage);
                    realm.insertOrUpdate(item);
                }
            });
        } finally {
            if(mRealm != null) {
                mRealm.close();
            }
        }
    }

    public void delItem(final Item item){

        try {
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    item.deleteFromRealm();
                }
            });
        } finally {
            if(mRealm != null) {
                mRealm.close();
            }
        }
    }


    public RealmResults retrieveAllItem(){

        RealmResults result = null;
        try {
            mRealm = Realm.getDefaultInstance();
            result = mRealm.where(Item.class).findAllAsync();
        } finally {
            if(mRealm != null) {
                mRealm.close();
            }
        }
        return result;
    }

    public String getFirstStar(){

        mRealm = Realm.getDefaultInstance();

        String firstStar= mRealm.where(LocationRealmObject.class).findFirst().getLocationStar();

        return firstStar;


    }

}
