package com.example.franc.misteryapp;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements MyListAdapter.OnItemSelectedListener, MyListAdapter.OnItemDeselectedListener{
    TextView io;
    Realm mRealm = null;
    static RealmHelper helper = new RealmHelper();
    static Player  getPlayer = null;
    static MyListAdapter enemyAdapter;
    static MyWeaponsAdapter weaponsAdapter;

    //variabili per la generazione delle stringhe casuali
    final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final java.util.Random rand = new java.util.Random();
    final Set<String> identifiers = new HashSet<String>();


    static RecyclerView list;
    static RecyclerView listWeapons;

    static ArrayList<Enemy> mEnemies = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManagerWeapons;
    private ArrayList<Enemy> selectedEnemies = new ArrayList<Enemy>();
    private ArrayList<WeaponSet> weaponSets = new ArrayList<WeaponSet>();

    ArrayList<Enemy> existingEnemies = new ArrayList<Enemy>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        io = (TextView)findViewById(R.id.health);

        mEnemies = generateEnemies(2);
        enemyAdapter = new MyListAdapter(mEnemies, MainActivity.this);

/*
        weaponSets = generateWeapons(4);
*/
        weaponsAdapter = new MyWeaponsAdapter(generateWeapons(25));

        //crea Realmobject Player se non esiste e ricarica energia
        isPlayer();

        startThreads(mEnemies);
        listWeapons = findViewById(R.id.rv_weapons);

        list = (RecyclerView)findViewById(R.id.rv);
        mLayoutManagerWeapons = new LinearLayoutManager(this);
        mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);
        listWeapons.setLayoutManager(mLayoutManagerWeapons);
        listWeapons.setAdapter(weaponsAdapter);

        list.setAdapter(enemyAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listWeapons);


    }

    // inizializza il Player
    public boolean isPlayer(){

        mRealm = Realm.getDefaultInstance();

        try {
            getPlayer = mRealm.where(Player.class).findAllAsync().first();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (getPlayer == null){
            Player player = new Player();
            helper.addItem(player);
        }else {
            helper.restoreHealth(getPlayer);
        }
        mRealm.close();
        return true;
    }

    // riceve arraylist di Enemy e per ognuno avvia un thread
    public ArrayList<Enemy> startThreads(ArrayList<Enemy> enemies){
        for (Enemy res:enemies) {
            new BackgroundTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);

        }
        return enemies;
    }

    // genera N nemici
    public ArrayList<Enemy> generateEnemies(int enemiesNumber){
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();

        for (int i = 0 ; i < enemiesNumber ; i++){
            Enemy enemy = new Enemy();
            enemy.setName(randomIdentifier());
            enemies.add(enemy);
        }
        existingEnemies = enemies;

        return existingEnemies;
    }

    // inserisce N armi e ne restituisce il RealmResult
    public RealmResults<WeaponSet> generateWeapons(int weaponsNumber){
        RealmHelper helper = new RealmHelper();
        helper.resetWeapons();
        for (int i = 0 ; i < weaponsNumber ; i++){
            WeaponSet weapons = new WeaponSet();
            weapons.setWeaponName("Missile");
            weapons.setWeaponDamage(3);
            weapons.setWeapondID(randomIdentifier());
            helper.addWeapon(weapons);
        }

        return helper.getWeapons();
    }


    // genera stringhe casuali
    public String randomIdentifier() {
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

    //rimuove dai selezionati l'elemento deselezionato
    public void onItemDeselected(Enemy item) {
        selectedEnemies.remove(item);
    }

    @Override
    public void onItemSelected(Enemy item) {
        selectedEnemies.add(item);
    }

    private class BackgroundTask extends AsyncTask<Enemy, Integer, Void>{
        public class Wrapper{

        ArrayList<Enemy> enemi;
        int salute;
    }

        void Sleep(int ms){
            try{
                Thread.sleep(ms);
            }
            catch (Exception e){
            }
        }

        @Override
        protected Void doInBackground(Enemy... arg0) {
            Wrapper wrapper = new Wrapper();
            final Player player;
            final RealmHelper helper = new RealmHelper();

            player = helper.getPlayer();

            while (arg0[0].getHealth() > 0 && helper.getPlayerHealth() > 0) {

                    Log.i("MainACtivity", arg0[0].getName() + " attacca");

                    try {
                        helper.dealDamage(player, 2);
                        int salute = player.getHealth();
                        publishProgress(salute);

                    } finally {
                    }
                    Sleep(2000);

            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            io.setText(String.valueOf(values[0]));

        }



    }


    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
            int position = viewHolder.getAdapterPosition();

            int power = weaponsAdapter.deleteItemAt(position);

            // danneggia il selezionato
            selectedEnemies.get(0).getDamage(power);

            if (selectedEnemies.get(0).getHealth() <= 0){
                mEnemies.remove(selectedEnemies.get(0));
                selectedEnemies.remove(selectedEnemies.get(0));

                enemyAdapter.notifyDataSetChanged();


            }
            enemyAdapter.notifyDataSetChanged();

        }
    };

}
