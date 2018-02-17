package com.example.franc.misteryapp;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import io.realm.Realm;

/**
 * Created by franc on 08/11/2017.
 */

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(getApplicationContext());

        //todo enemyspawn asynctask

        new SpawnEnemy().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


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

            while (true) {
                worldHelper.spawnEnemy();
                publishProgress();
                Sleep(4000);
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

            //todo helper.getAllEnemies()
            //todo foreach enemie found whos not fighting enemyAction
            return null;
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
