package com.example.franc.misteryapp;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by franc on 28/01/2018.
 */

public class EnemyQueue extends RealmObject {

    private RealmList<AllEnemies> enemyBuffer = new RealmList<>();
    @PrimaryKey
    private int exist = 0;

    public EnemyQueue() {
    }

    public RealmList<AllEnemies> getEnemyBuffer() {
        return enemyBuffer;
    }

    public void setEnemyBuffer(AllEnemies enemyBuffer) {
        this.enemyBuffer.add(enemyBuffer);
    }
    public void removeFromEnemyBuffer(AllEnemies enemyBuffer) {
        this.enemyBuffer.remove(enemyBuffer);
    }


    public int getExist() {
        return exist;
    }

    public void setExist(int exist) {
        this.exist = exist;
    }
}
