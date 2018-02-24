package com.example.franc.misteryapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;


public class MenuFragmentWeapons extends Fragment implements MyEnemyAdapter.OnItemSelectionListener{
    static Spinner spinner;
    static Realm realm;
    static String starSystem;
    static RecyclerView listWeapons;
    static RealmHelper helper = new RealmHelper();
    MyWeaponsAdapter weaponsAdapter;
    static RealmList<AllEnemies> selectedEnemies = new RealmList<>();
    private MyEnemyAdapter enemyAdapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.enemyAdapter = new MyEnemyAdapter(this);
        realm = Realm.getDefaultInstance();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_weapons, container, false);
    }

    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        listWeapons = view.findViewById(R.id.rv_weapons);
        startRecyclerViewWeapons();
    }

    // genera stringhe casuali
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

    // inserisce N armi e ne restituisce il RealmResult
    public RealmResults<WeaponSet> generateWeapons(int weaponsNumber){
        helper.resetWeapons();
        for (int i = 0 ; i < weaponsNumber ; i++){
            WeaponSet weapons = new WeaponSet();
            if (i == 0)
                weapons.setViewType(0);
            else
                weapons.setViewType(1);
            weapons.setWeaponName("Missile");
            weapons.setWeaponDamage(3);
            weapons.setWeapondID(randomIdentifier());

            helper.addWeapon(weapons);
        }

        return helper.getWeapons();
    }

    public void startRecyclerViewWeapons(){
        RealmResults<WeaponSet> weapons;

        RecyclerView.LayoutManager mLayoutManagerWeapons;
        weapons = generateWeapons(25);
        mLayoutManagerWeapons = new LinearLayoutManager(this.getContext());

        weaponsAdapter = new MyWeaponsAdapter(weapons, helper);
        listWeapons.setLayoutManager(mLayoutManagerWeapons);
        listWeapons.setAdapter(weaponsAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listWeapons);



    }

    @Override
    public void onItemDeselected(AllEnemies item) {
        selectedEnemies.remove(item);
        helper.setEnemyUnselected(item);

    }

    @Override
    public void onItemSelected(AllEnemies item) {
        selectedEnemies.add(item);
        helper.setEnemySelected(item);

    }

    // interfaccia per il controllo dello swipe delle armi
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            return false;
        }

        // inizializza le direzioni di swipe, null se non è swipabile
        @Override
        public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getItemViewType() > 0) return 0;
            return super.getSwipeDirs(recyclerView, viewHolder);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return super.isItemViewSwipeEnabled();
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            int position = viewHolder.getAdapterPosition();

            //WAIT FOR AMMO TO LOAD
            new ReloadTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, (MyWeaponsAdapter.ViewHolder) listWeapons.findViewHolderForAdapterPosition(0));


            //REMOVE AMMO FROM LIST AND RETURNS DAMAGE POWER
            int power = weaponsAdapter.deleteItemAt(position);

            //DAMAGE SELECTED ENEMY WITH SWIPED AMMO
            helper.dealEnemyDamage(selectedEnemies.get(0), power);

            //SET ENEMYATTACKED IF NOT ALREADY SET
            //THEN START ENEMY FIGHT BEAVHIOUR
            if (selectedEnemies.get(0).getAttacked() == false){
                helper.setEnemyAttacked(selectedEnemies.get(0).getId());
                startThreads(selectedEnemies.get(0));
            }
            if (selectedEnemies.get(0).getHealth() <= 0){

                //REMOVE ENEMY FROM BATTLE BUFFER
                helper.removeEnemyFromQueue(selectedEnemies.get(0));

                //REMOVE ENEMY FROM SELECTION
                selectedEnemies.remove(selectedEnemies.get(0));

                //NOTIFY IN BATTLE ENEMY ADAPTER: ENEMY NO LONGER EXISTS
                BattleActivity.enemyAdapter.notifyDataSetChanged();

                //NOTIFY IN NAVIGATION ENEMY ADAPTER: ENEMY NO LONGER EXISTS
                NavigationActivity.navigationEnemyAdapter.notifyDataSetChanged();

                //NO ENEMY NO BATTLEACTIVITY. WORKS ONLY WITH 1 ENEMY
/*
                BattleActivity.finish();
*/
            }

            //NOTIFY IN BATTLE ENEMY ADAPTER: ENEMY HEALTH CHANGED
            BattleActivity.enemyAdapter.notifyDataSetChanged();
        }


    };

    // riceve enemies e per ognuno avvia un thread
    public void startThreads(AllEnemies enemies){
        //
/*
        new EnemyFight().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, enemies.getId());
*/

    }

    // thread per il tempo di caricamento delle arrmi
    private class ReloadTask extends AsyncTask<MyWeaponsAdapter.ViewHolder, Void, Void> {

        MyWeaponsAdapter.ViewHolder currentHolder;

        void Sleep(int ms){
            try{
                Thread.sleep(ms);
            }
            catch (Exception e){
            }
        }

        @Override
        protected Void doInBackground(MyWeaponsAdapter.ViewHolder... arg0) {
            currentHolder = arg0[0];
            Sleep(1000);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            currentHolder.reloadTime.setText(R.string.generic_ok);
            // primo nel db diventa primo nella lista e swipable
            helper.setFirst();
            weaponsAdapter.notifyDataSetChanged();

        }
    }

}
