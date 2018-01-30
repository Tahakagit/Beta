package com.example.franc.misteryapp;

import android.content.Context;
import android.webkit.WebMessagePort;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by franc on 09/01/2018.
 */

public class BackgroundRealmHelper {

    private Realm mRealm;

    public BackgroundRealmHelper(Realm realm) {
        this.mRealm = realm;
    }

    public Realm getRealm(){
        return this.mRealm;
    }
    public Player getPlayer(){

        final Player[] player = new Player[1];

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                player[0] = realm.where(Player.class).findAll().first();

            }
        });

        return player[0];

    }
    public int getPlayerHealth(){

        int health;
        Player getPlayer = mRealm.where(Player.class).findAll().first();
        health = getPlayer.getHealth();
        return health;

    }
    public String getPlayerLocation(){

        String position;
        try {
            Player getPlayer = mRealm.where(Player.class).findAll().first();
            position = getPlayer.getLocation();
        } finally {
        }
        return position;
    }
    public void setPlayerLocation(final String firstStar){

        final Player player = mRealm.where(Player.class).findAll().first();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                player.setLocation(firstStar);

            }
        });

    }
    public void resetLocation(){
        String position;
        final Player getPlayer = mRealm.where(Player.class).findAll().first();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getPlayer.setLocation(null);

            }
        });


    }

    public void resetEnemies(){
        String position;
        final RealmResults<AllEnemies> getEnemies = mRealm.where(AllEnemies.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                getEnemies.deleteAllFromRealm();
            }
        });


    }
    public void setEnemySelected(final AllEnemies enemy){
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                enemy.setSelected(true);
            }
        });


    }
    public void setEnemyUnselected(final AllEnemies enemy){
        final Player player = mRealm.where(Player.class).findAll().first();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                enemy.setSelected(false);
            }
        });


    }

    public int getEnemyHealth(){
        final int health ;
        try {
            RealmList<AllEnemies> enemy = mRealm.where(EnemyQueue.class).findFirst().getEnemyBuffer();
            if (enemy.size() != 0)
                health = enemy.get(0).getHealth();
            else
                return 0;
        } finally {
        }
        return health;
    }
    public void addEnemyToQueue(final AllEnemies enemy){

        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mRealm.copyToRealmOrUpdate(enemy);
/*
                    EnemyQueue queue = mRealm.where(EnemyQueue.class).findFirst();
                    queue.setEnemyBuffer(enemy);
*/
                }
            });
        } finally {
        }

    }
    public RealmList<AllEnemies> getEnemyQueue(){

        RealmList<AllEnemies> enemyList ;
        try {
            enemyList = new RealmList<AllEnemies>();
            enemyList = mRealm.where(EnemyQueue.class).findFirst().getEnemyBuffer();
        } finally {
        }

        return enemyList;

    }
    public void removeEnemyFromQueue(final AllEnemies enemy){

        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    EnemyQueue enemyList = mRealm.where(EnemyQueue.class).findFirst();
                    enemyList.removeFromEnemyBuffer(enemy);
                }
            });

        } finally {
        }

    }

    public RealmResults<AllEnemies> getEnemiesAtPLayerPosition(){

        RealmResults<AllEnemies> listOfEnemies;
        try {
            listOfEnemies = mRealm.where(AllEnemies.class).equalTo("location", getPlayerLocation()).findAll();
        } finally {
        }


        return listOfEnemies;
    }


    public void resetUniverse(){

        try {

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<LocationRealmObject> allLocations = mRealm.where(LocationRealmObject.class).findAll();
                    allLocations.deleteAllFromRealm();

                }
            });

        } finally {
        }

    }
    public String getRandomLocation(){
        Random r = new Random();

        String randomStarSystem;
        try {
            int getSize = mRealm.where(LocationRealmObject.class).findAll().size();
            int Low = 10;
            int High = getSize;
            int Result = r.nextInt(High-Low) + Low;

            RealmResults<LocationRealmObject> all = mRealm.where(LocationRealmObject.class).findAll();
            RealmResults<LocationRealmObject> location = mRealm.where(LocationRealmObject.class).equalTo("locationId", Result).findAll();
            randomStarSystem = location.get(0).getLocationStar();
        } finally {

        }
        return randomStarSystem;

    }
    public RealmResults<LocationRealmObject> getPlacesAtPLayerPosition(){

        RealmResults<LocationRealmObject> listOfPlaces;
        try {
            listOfPlaces = mRealm.where(LocationRealmObject.class).equalTo("locationStar", getPlayerLocation()).findAll();
        } finally {
        }

        return listOfPlaces;
    }



    public void resetWeapons(){

        try {

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<WeaponSet> getWeapons = mRealm.where(WeaponSet.class).findAll();
                    getWeapons.deleteAllFromRealm();

                }
            });

        } finally {

        }

    }
    public RealmResults<WeaponSet> getWeapons(){

        RealmResults<WeaponSet> listOfWeapons;
        try {
            listOfWeapons = mRealm.where(WeaponSet.class).findAll();
        } finally {
        }

        return listOfWeapons;

    }
    public int removeWeaponAt(final int index){

        final int[] weaponPower = new int[1];
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<WeaponSet> resultWeapon = mRealm.where(WeaponSet.class).findAll();
                final WeaponSet weapon = resultWeapon.get(index);
                weaponPower[0] = weapon.getWeaponDamage();

                weapon.deleteFromRealm();
            }
        });
        if(mRealm != null) {
        }

        return weaponPower[0];
    }
    public void addWeapon(final RealmObject item){

        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(item);
                }
            });
        } finally {
        }
    }


    public RealmResults<AllEnemies> getDeadEnemies(){

        RealmResults<AllEnemies> listOfDeadEnemies;
        try {
            listOfDeadEnemies = mRealm.where(AllEnemies.class).lessThan("health", 1).findAll();
        } finally {
        }

        return listOfDeadEnemies;

    }


    public void addItem(final RealmObject item){

        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(item);
                }
            });
        } finally {
        }
    }


    public void restoreHealth(final Player item){

        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    item.setHealth(100);
                    realm.insertOrUpdate(item);
                }
            });
        } finally {
            if(mRealm != null) {
            }
        }
    }

    public void setFirst(){

        try {
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
            }
        }
    }

    public void dealDamage(final Player item, final int damage){

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setHealth(item.getHealth() - damage);
                realm.insertOrUpdate(item);
            }
        });
    }

    public void dealEnemyDamage(final AllEnemies item, final int damage){

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setHealth(item.getHealth() - damage);
                realm.insertOrUpdate(item);
            }
        });
    }


    public void delItem(final AllEnemies item){

        try {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    item.deleteFromRealm();
                }
            });
        } finally {
        }
    }


    public void delEnemy(){

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                AllEnemies item = mRealm.where(AllEnemies.class).equalTo("isDead", true).findFirst();
                item.deleteFromRealm();
            }
        });
    }

    public RealmResults retrieveAllItem(){

        RealmResults result = null;
        try {
            result = mRealm.where(Item.class).findAllAsync();
        } finally {
            if(mRealm != null) {
            }
        }
        return result;
    }

    public String getFirstStar(){


        String firstStar= mRealm.where(LocationRealmObject.class).findFirst().getLocationStar();

        return firstStar;


    }

}
