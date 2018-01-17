package com.example.franc.misteryapp;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;

/**
 * Created by franc on 17/01/2018.
 */

public class Enemy2 implements Runnable {

    Handler mHandler;
    Player mPlayer;
    int currentHealth;
    View view;

    public Enemy2(Handler mHandler){
        this.mHandler = mHandler;
    }
    @Override
    public void run() {

        enemyRoutine();
    }


    private void enemyRoutine(){


        Realm realm = Realm.getDefaultInstance();

        final Player player = realm.where(Player.class).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                player.setHealth(player.getHealth() - 20);
                realm.insertOrUpdate(player);

            }
        });

        int salute = player.getHealth();

        if(realm != null) {
            realm.close();
        }


    }
}
