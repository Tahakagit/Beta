package com.example.franc.misteryapp;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import io.realm.Realm;

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
        Realm mrealm = Realm.getDefaultInstance();

        RealmHelper helper = new RealmHelper();
        WorldManagementHelper worldHelper = new WorldManagementHelper(helper);
        worldHelper.spawnEnemy();

        mrealm.close();
        // get allenemies morti e li rimuove

/*
        RealmResults<AllEnemies> enemylist = helper.getDeadEnemies();
        for (AllEnemies res: enemylist) {
            helper.delItem(res);
        }
*/

        // far agire tutti i nemici esistenti
        // plan()
        // do()

        // If we get killed, afSter returning from here, restart
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);


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
