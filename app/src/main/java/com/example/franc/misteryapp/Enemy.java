package com.example.franc.misteryapp;

import android.app.Service;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by franc on 14/01/2018.
 */

public class Enemy{

    private String name = null;
    private int health = 20;
    private String id = null;

    private boolean isAdded = false;
    private boolean isSelected = false;
    private String location = null;
    private Boolean isAttacked = false;
    static AsyncTask spawnEnemyTask;


    RealmHelper helper = new RealmHelper();

    public Enemy(String name, String id, String location) {
        this.name = name;
        this.id = id;
        this.location = location;
        AllEnemies enemy = new AllEnemies();
        enemy.setId(this.id);
        enemy.setName(this.name);
        enemy.setLocation(this.location);
        enemy.setAttacked(false);
        helper.addItem(enemy);

    }
    public void killMe(String id) {

        helper.delEnemy(this.id);
    }

    void startSpawnEnemies(){
         spawnEnemyTask = new SpawnEnemy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    void stopSpawning(){
        spawnEnemyTask.cancel(true);
    }


    public Enemy(){}
    public Enemy fetchById(String enemyId){
        Enemy retrievedEnemy = helper.getEnemyFromID(enemyId);


        return retrievedEnemy;
    }

    public List<Enemy> fetchNotFighting(){
        List<Enemy> updatedEnemyList = new ArrayList<>();

        updatedEnemyList = helper.getNotFightingEnemies();

        return updatedEnemyList;
    }

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

    // not used
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    //not used
    public boolean isSelected() {
        return isSelected;
    }

    public void getDamage(int damage){
        this.health = helper.dealEnemyDamage(id, damage);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        helper.setEnemyLocation(id, location);
    }

    public Boolean getAttacked() {
        return isAttacked;
    }

    public void setAttacked(Boolean attacked) {
        isAttacked = attacked;
    }

    private class SpawnEnemy extends AsyncTask<Void, Void, Void> {

        /**
         *
         * @param ms Time to sleep
         */
        void Sleep(int ms){
            try{
                Thread.sleep(ms);
            }
            catch (Exception e){
            }
        }

        /**
         *
         *
         * @param arg0
         * @return
         */
        @Override
        protected Void doInBackground(final Void... arg0) {
            final Player player;
            final AllEnemies enemies;

            RealmHelper helper = new RealmHelper();
            new EnemyRoutine().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            WorldManagementHelper worldHelper = new WorldManagementHelper(helper);

            helper.resetEnemies();

            while (true) {
                worldHelper.spawnEnemy();
                publishProgress();
                Sleep(5000);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values){
            try {

                NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
/*                NavigationActivity.navigationAdapter.UpdateAdapter(helper.getPlacesAtPLayerPosition());
*/

            } catch (NullPointerException e) {
                Log.e("SpawnEnemy", "Failed to update navigationEnemyAdapter " + e);
            }

        }

        @Override
        protected void onCancelled() {
/*
            NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
*/
        }

    }

    private class EnemyRoutine extends AsyncTask<Void, Void, Void> {

        //todo to fix: enemy si sposta quando in battaglia
        /**
         *
         * @param ms Time to sleep
         */
        void Sleep(int ms){
            try{
                Thread.sleep(ms);
            }
            catch (Exception e){
            }
        }

        @Override
        protected Void doInBackground(final Void... arg0) {

            RealmHelper helper = new RealmHelper();


            while (isCancelled() != true) {
                Sleep(5000);


/*
                List<String> enemiesNotBattleResults = helper.getNotFightingEnemies();
*/
                List<Enemy> enemiesNotBattleResults = fetchNotFighting();
                Log.d("Enemy Spawn", "Found " + enemiesNotBattleResults.size() + " Enemy to start enemyroutine");
                if (enemiesNotBattleResults.size() > 0) {
                    for (Enemy enemy:enemiesNotBattleResults) {
                        //todo implement pngs routine
                        enemy.setLocation(getRandomLocation());
/*
                        String newLocation = helper.setEnemyLocation(enemy);
*/
                        publishProgress();

                        Log.d("Enemy AI action",  enemy.getName() + " moved" + enemy.getLocation());


                    }
                }
            }
            cancel(true);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values){

            NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
/*            NavigationActivity.navigationAdapter.UpdateAdapter(helper.getPlacesAtPLayerPosition());
*/

        }

        @Override
        protected void onCancelled() {
/*
            NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
*/
        }

    }
    // GET RANDOM LOCATION
    String getRandomLocation(){
        Realm mRealm = Realm.getDefaultInstance();

        Random r = new Random();
        String randomStarSystem;
        try {
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


}
