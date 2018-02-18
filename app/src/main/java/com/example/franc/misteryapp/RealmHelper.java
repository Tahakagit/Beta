package com.example.franc.misteryapp;

import android.provider.SyncStateContract;
import android.support.annotation.NonNull;

import java.util.Random;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * HELPER TO INTERACT WITH DB
 */
public class RealmHelper {

    private Realm mRealm;

    RealmHelper() {
    }

    public Realm getRealm(){
        this.mRealm = Realm.getDefaultInstance();
        return this.mRealm;
    }


    // GET PLAYER OR CREATES IT IF NOT EXISTS
    Player getPlayer(){
        mRealm = Realm.getDefaultInstance();
        Player player = null;


        try {
            player = mRealm.where(Player.class).findAll().first();
        } catch (IndexOutOfBoundsException e) {

            mRealm.beginTransaction();
            player = new Player();
            player.setLocation(getRandomLocation());
            mRealm.copyToRealm(player);
            mRealm.commitTransaction();
        }
        mRealm.close();
        return player;
    }

    // GET PLAYER HEALTH
    int getPlayerHealth(){
        mRealm = Realm.getDefaultInstance();
        int playerHealth = mRealm.where(Player.class).findAll().first().getHealth();
        mRealm.close();
        return playerHealth;
    }

    // GET PLAYER LOCATION
    String getPlayerLocation(){
        mRealm = Realm.getDefaultInstance();
        String playerLocation = null;
        try {
            playerLocation = getPlayer().getLocation();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        mRealm.close();
        return playerLocation;
    }

    // SET PLAYER LOCATION
    void setPlayerLocation(@Nonnull final String firstStar){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                getPlayer().setLocation(firstStar);
            }
        });

    }

    // RESET PLAYER LOCATION
    void resetLocation(){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                Player player = getPlayer();
                player.setLocation(getRandomLocation());
            }
        });
    }

    // DELETE ALL ENEMIES
    void resetEnemies(){
        mRealm = Realm.getDefaultInstance();
        final RealmResults<AllEnemies> getEnemies = mRealm.where(AllEnemies.class).findAll();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                getEnemies.deleteAllFromRealm();
            }
        });
    }

    // SET ENEMY SELECTED
    void setEnemySelected(@Nonnull final AllEnemies enemy){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                enemy.setSelected(true);
            }
        });
    }

    //SET ENEMY UNSELECTED
    void setEnemyUnselected(@Nonnull final AllEnemies enemy){
        mRealm = Realm.getDefaultInstance();
        final Player player = mRealm.where(Player.class).findAll().first();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                enemy.setSelected(false);
            }
        });
    }

    // GET ENEMY HEALTH
    int getEnemyHealth(){
        final int health ;
        mRealm = Realm.getDefaultInstance();
        RealmList<AllEnemies> enemy = mRealm.where(EnemyQueue.class).findFirst().getEnemyBuffer();
        if (enemy.size() != 0)
            health = enemy.get(0).getHealth();
        else
            return 0;
        mRealm.close();
        return health;
    }

    // ADD ENEMY TO BATTLE QUEUE
    void addEnemyToQueue(@Nonnull final AllEnemies enemy){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                mRealm.copyToRealmOrUpdate(enemy);
            }
        });
    }

    // RETURN ENEMIES BATTLE QUEUE
    RealmList<AllEnemies> getEnemyQueue(){
        RealmList<AllEnemies> enemyList ;
        mRealm = Realm.getDefaultInstance();
        enemyList = new RealmList<AllEnemies>();
        try {
            enemyList = mRealm.where(EnemyQueue.class).findFirst().getEnemyBuffer();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mRealm.close();
        return enemyList;
    }

    // REMOVE ENEMY FROM BATTLE QUEUE
    void removeEnemyFromQueue(@Nonnull final AllEnemies enemy){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                EnemyQueue enemyList = mRealm.where(EnemyQueue.class).findFirst();
                try {
                    enemyList.removeFromEnemyBuffer(enemy);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // GET ENEMIES AT PLAYER'S STAR SYSTEM
    RealmResults<AllEnemies> getEnemiesAtPLayerPosition(){
        RealmResults<AllEnemies> listOfEnemies;
        mRealm = Realm.getDefaultInstance();
        listOfEnemies = mRealm.where(AllEnemies.class).equalTo("location", getPlayerLocation()).findAll();
        mRealm.close();
        return listOfEnemies;
    }

    // RESET UNIVERSE
    void resetUniverse(){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                RealmResults<LocationRealmObject> allLocations = mRealm.where(LocationRealmObject.class).findAll();
                allLocations.deleteAllFromRealm();
            }
        });
    }

    // GET RANDOM LOCATION
    String getRandomLocation(){
        Random r = new Random();
        String randomStarSystem;
        try {
            mRealm = Realm.getDefaultInstance();
            int getSize = mRealm.where(LocationRealmObject.class).findAll().size();
            int Low = 10;
            int High = getSize;
            int Result = r.nextInt(High-Low) + Low;
            RealmResults<LocationRealmObject> location = mRealm.where(LocationRealmObject.class).equalTo("locationId", Result).findAll();
            randomStarSystem = location.get(0).getLocationStar();
        } finally {
            mRealm.close();
        }
        return randomStarSystem;
    }

    // GET LOCATION AT PLAYER POSITION
    RealmResults<LocationRealmObject> getPlacesAtPLayerPosition(){
        RealmResults<LocationRealmObject> listOfPlaces;
        try {
            mRealm = Realm.getDefaultInstance();
            listOfPlaces = mRealm.where(LocationRealmObject.class).equalTo("locationStar", getPlayerLocation()).findAll();
        } finally {
            mRealm.close();
        }
        return listOfPlaces;
    }

    // DELETTE ALL EXISTING WEAPONS
    void resetWeapons(){
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@Nonnull Realm realm) {
                RealmResults<WeaponSet> getWeapons = mRealm.where(WeaponSet.class).findAll();
                getWeapons.deleteAllFromRealm();
            }
        });
    }

    // GET ALL WEAPONS
    RealmResults<WeaponSet> getWeapons(){
        RealmResults<WeaponSet> listOfWeapons;
        try {
            mRealm = Realm.getDefaultInstance();
            listOfWeapons = mRealm.where(WeaponSet.class).findAll();
        } finally {
            mRealm.close();
        }

        return listOfWeapons;

    }

    // REMOVE WEAPON ON SWIPED
    int removeWeaponAt(@Nonnull final int index){
        final int[] weaponPower = new int[1];
        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<WeaponSet> resultWeapon = mRealm.where(WeaponSet.class).findAll();
                final WeaponSet weapon = resultWeapon.get(index);
                weaponPower[0] = weapon.getWeaponDamage();
                weapon.deleteFromRealm();
            }
        });
        return weaponPower[0];
    }

    // ADD WEAPON
    void addWeapon(@Nonnull final RealmObject item){
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@Nonnull Realm realm) {
                    realm.insertOrUpdate(item);
                }
            });
    }


    RealmResults<AllEnemies> getDeadEnemies(){

        RealmResults<AllEnemies> listOfDeadEnemies;
        try {
            mRealm = Realm.getDefaultInstance();
            listOfDeadEnemies = mRealm.where(AllEnemies.class).lessThan("health", 1).findAll();
        } finally {
            mRealm.close();
        }

        return listOfDeadEnemies;

    }

    // ADD OBJECT TO DB
    public void addItem(final RealmObject item){

        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(item);
            }
        });
    }


    void restorePlayerHealth(){

            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@Nonnull Realm realm) {
                    Player player = realm.where(Player.class).findFirst();

                    player.setHealth(100);
/*
                    realm.insertOrUpdate(item);
*/
                }
            });
    }

    void setFirst(){
            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                WeaponSet realFirst = mRealm.where(WeaponSet.class).findFirst();
                @Override
                public void execute(@Nonnull Realm realm) {
                    realFirst.setViewType(0);
                    realm.insertOrUpdate(realFirst);
                }
            });
    }

    public void dealDamage(final Player item, final int damage){

        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setHealth(item.getHealth() - damage);
                realm.insertOrUpdate(item);
            }
        });
        if(mRealm != null) {
            mRealm.close();
        }
    }

    public void dealEnemyDamage(final AllEnemies item, final int damage){

        mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                item.setHealth(item.getHealth() - damage);
                realm.insertOrUpdate(item);
            }
        });
        if(mRealm != null) {
            mRealm.close();
        }
    }


/*
    public void closeSession(){
        mRealm.close();
    }
*/
    public void delItem(final AllEnemies item){

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


    public void delEnemy(){

            mRealm = Realm.getDefaultInstance();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    AllEnemies item = mRealm.where(AllEnemies.class).equalTo("isDead", true).findFirst();
                    item.deleteFromRealm();
                }
            });
            if(mRealm != null) {
                mRealm.close();
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
        mRealm.close();

        return firstStar;


    }

}
