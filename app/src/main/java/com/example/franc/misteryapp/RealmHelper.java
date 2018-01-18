package com.example.franc.misteryapp;

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

    public RealmResults<WeaponSet> getWeapons(){

        mRealm = Realm.getDefaultInstance();
        RealmResults<WeaponSet> getWeapons = mRealm.where(WeaponSet.class).findAll();
        return getWeapons;

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

}
