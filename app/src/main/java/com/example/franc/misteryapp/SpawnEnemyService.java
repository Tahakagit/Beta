package com.example.franc.misteryapp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by franc on 27/01/2018.
 */

public class SpawnEnemyService extends IntentService {
    Handler handler = new Handler();
    static final int SERVICE_ID = 622;


    /**
     * A constructor is required, and must call the super IntentService(String)
     * constructor with a name for the worker thread.
     */
    public SpawnEnemyService() {
        super("SpawnEnemyService");
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
/*
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        try {
            RealmHelper helper = new RealmHelper();
            WorldManagementHelper worldHelper = new WorldManagementHelper(helper);
            worldHelper.spawnEnemy();
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Restore interrupt status.
            Thread.currentThread().interrupt();
        }
*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        RealmHelper helper = new RealmHelper();
        WorldManagementHelper worldHelper = new WorldManagementHelper(helper);
        worldHelper.spawnEnemy();

        // far agire tutti i nemici esistenti
        // plan()
        // do()

        // If we get killed, after returning from here, restart
        return START_NOT_STICKY;
    }

    public void enemyRoutine(){
        // query tutti i nemici
        // foreach
    }

    @Override
    public Context getBaseContext() {
        return super.getBaseContext();
    }
}
