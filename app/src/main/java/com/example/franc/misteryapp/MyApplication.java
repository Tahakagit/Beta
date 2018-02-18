package com.example.franc.misteryapp;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by franc on 08/11/2017.
 */

public class MyApplication extends Application {
    static String playerLocation;


    RealmHelper helper = new RealmHelper();

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());


        startUniverse();

        isPlayer();
        new SpawnEnemy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new EnemyAction().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    // PLAYER INIT
    public boolean isPlayer(){
        /**
         * Check if player already exists if not create it and set
         * location
         *
         * todo set start point if !player
         */
        Player player = helper.getPlayer();
        helper.resetLocation();
        /*
        helper.resetLocation();
*/
        helper.restorePlayerHealth();
/*
        if (helper.getPlayer() == null){
            Player player = new Player();
            helper.addItem(player);
        }else {
            helper.resetLocation();
            helper.restoreHealth(helper.getPlayer());
        }
*/
/*
        playerLocation = player.getLocation();
*/
/*
        if (helper.getPlayerLocation() == null)
            helper.setPlayerLocation(helper.getRandomLocation());
*/

        return true;
    }

    public void startUniverse(){
        this.helper.resetUniverse();
        // creare routine di inserimento delle location
        int z = 1;

        for (int i = 0; i < 4; i++) {

            String sectorName = "Sector - " + randomIdentifier();
            for (int u = 0 ; u < 2 ; u++){
                String starName = "Star - " + randomIdentifier();
                for (int p = 0; p < 4; p++){
                    String locationName = "Location - " + randomIdentifier();
                    LocationRealmObject location = new LocationRealmObject();
                    location.setLocationName(locationName);
                    location.setLocationStar(starName);
                    location.setLocationSector(sectorName);
                    location.setLocationId(z);
                    this.helper.addItem(location);
                    z++;
                }
            }

        }

    }


    // GENERA STRINGHE CASUALI
    public String randomIdentifier() {
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
        final java.util.Random rand = new java.util.Random();
        final Set<String> identifiers = new HashSet<String>();

        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
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

            // todo remove this realm instance?
            RealmHelper helper = new RealmHelper();

            WorldManagementHelper worldHelper = new WorldManagementHelper(helper);

/*
            final RealmHelper backgroundHelper = new RealmHelper();
*/
            //
            helper.resetEnemies();

            while (true) {
                worldHelper.spawnEnemy();
                publishProgress();
                Sleep(5000);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values){
            NavigationActivity.navigationEnemyAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
/*
            NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
*/
        }

    }

    private class EnemyAction extends AsyncTask<Void, Void, Void> {

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

            WorldManagementHelper worldHelper = new WorldManagementHelper(helper);

            while (true) {
                Sleep(5000);

                List<String> enemiesNotBattleResults = helper.getNotFightingEnemies();
                if (enemiesNotBattleResults.size() > 0) {
                    for (String enemy:enemiesNotBattleResults) {

        /*
                        String id = enemy.getId();
        */
        /*
                        Sleep(5000);
        */
                        String newLocation = helper.setEnemyLocation(enemy);
                        publishProgress();

                        Log.d("tag",  helper.getEnemyFromID(enemy).getName() + " moved To " + newLocation);


                    }
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values){
            NavigationActivity.navigationEnemyAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
/*
            NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
*/
        }

    }

}
