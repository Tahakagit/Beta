package com.example.franc.misteryapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class BattleActivity extends AppCompatActivity implements MyEnemyAdapter.OnItemSelectedListener, MyEnemyAdapter.OnItemDeselectedListener{
    TextView io;
    TextView weaponReload;
    Realm mRealm = null;
    static RealmHelper helper;
    static Player  getPlayer = null;
    static MyEnemyAdapter enemyAdapter;
    static MyWeaponsAdapter weaponsAdapter;
    static Context context;

    //variabili per la generazione delle stringhe casuali


    static RealmResults<WeaponSet> weapons;
    static RecyclerView list;
    static RecyclerView listWeapons;

    static ArrayList<Enemy> mEnemies = new ArrayList<>();
    static RealmList<AllEnemies> selectedEnemies = new RealmList<>();

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManagerWeapons;

    ArrayList<Enemy> existingEnemies = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer);
        helper = new RealmHelper();
        EnemyQueue enemyQueue = new EnemyQueue();
        context = this;

/*
        Realm.init(this);
*/
        io = (TextView)findViewById(R.id.health);

/*
        mEnemies = generateEnemies(2);
*/
        enemyAdapter = new MyEnemyAdapter(helper.getEnemyQueue(), BattleActivity.this);

        weapons = generateWeapons(25);
        weaponsAdapter = new MyWeaponsAdapter(weapons, helper);

        startNavDrawer();
        //crea Realmobject Player se non esiste e ricarica energia
/*
        isPlayer();
*/

        RealmList<AllEnemies> enemyqueue = helper.getEnemyQueue();
        startThreads(enemyqueue);

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

/*
        RealmResults<WeaponSet> weapons = helper.getWeapons();
        weapons.addChangeListener(new RealmChangeListener<RealmResults<WeaponSet>>() {
            @Override
            public void onChange(RealmResults<WeaponSet> weaponSets) {
                helper.setFirst();
                weaponsAdapter.notifyDataSetChanged();
            }
        });
*/


    }


    //NAVIGATION DRAWER
    public void startNavDrawer(){
        final DrawerLayout mDrawerLayout;
        final Intent creaConto = new Intent(this, NavigationActivity.class);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            switch (menuItem.getItemId())
                            {
                                case R.id.action_category_1:
                                    startActivity(creaConto);
                                    break;
                                case R.id.action_category_2:
                                    //tabLayout.getTabAt(1).select();
                                    break;
                                case R.id.action_category_3:
                                    //tabLayout.getTabAt(2).select();
                            }

                            mDrawerLayout.closeDrawers();
                            return true;
                        }
                    });
        }


    }

    // inizializza il Player
    public boolean isPlayer(){
        //todo ritorna il player

/*
        mRealm = Realm.getDefaultInstance();
*/

        try {
            getPlayer = helper.getPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        mRealm.close();
*/
        if (getPlayer == null){
            Player player = new Player();
            helper.addItem(player);
        }else {
            helper.resetLocation();
            helper.restoreHealth(getPlayer);
        }
        return true;
    }

    // riceve enemies e per ognuno avvia un thread
    public void startThreads(RealmList<AllEnemies> enemies){
        for (AllEnemies res:enemies) {
            new BackgroundTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res.getId());

        }
    }

    // genera N nemici
    public ArrayList<Enemy> generateEnemies(int enemiesNumber){
        ArrayList<Enemy> enemies = new ArrayList<>();

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

    //rimuove dai selezionati l'elemento deselezionato
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


    // thread per il comportamento del nemico

    /**
     * Enemy behaviour thread
     * Must reference db items from scratch
     * Todo remove BackgroundRealmHelper usage
     *
     */
    private class BackgroundTask extends AsyncTask<String, Integer, Void>{

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
         * @param arg0  enemy's id
         * @return nothing
         */
        @Override
        protected Void doInBackground(final String... arg0) {
            final Player player;
            final AllEnemies enemies;

            // todo remove this realm instance?
            mRealm = Realm.getDefaultInstance();
            RealmHelper bHelper = new RealmHelper();
            Realm realm = helper.getRealm();
            AllEnemies enemy = realm.where(AllEnemies.class).equalTo("id", arg0[0]).findFirst();
            realm.close();
/*
            final RealmHelper backgroundHelper = new RealmHelper();
*/
            player = bHelper.getPlayer();
            //

            if (arg0[0] == null)
                cancel(true);
            while (arg0[0] != null) {
                if (isCancelled())
                    break;


                while (bHelper.getEnemyHealth() > 0) {
                    if (isCancelled())
                        break;

    /*
                        Log.i("MainACtivity", arg0[0].getName() + " attacca");
    */
                    try {
                        if (isCancelled())
                            break;

                        Sleep(2000);
                        Log.d("Asynctask:", "IS ATTACKING");
                        bHelper.dealDamage(player, 2);
                        int salute = player.getHealth();
                        publishProgress(salute);

                    } finally {
                    }

                }

                bHelper.delItem(enemy);
                cancel(true);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values){
            io.setText(String.valueOf(values[0]));

        }

        @Override
        protected void onCancelled() {
            NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
        }

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
            //Remove swiped item from list and notify the RecyclerView
            int position = viewHolder.getAdapterPosition();

            new reloadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (MyWeaponsAdapter.ViewHolder)listWeapons.findViewHolderForAdapterPosition(0));


            // rimuove on swipe il proiettile e ne ritorna in danno
            int power = weaponsAdapter.deleteItemAt(position);
            helper.dealEnemyDamage(selectedEnemies.get(0), power);
            if (selectedEnemies.get(0).getHealth() <= 0){
                helper.removeEnemyFromQueue(selectedEnemies.get(0));
                selectedEnemies.remove(selectedEnemies.get(0));
                enemyAdapter.notifyDataSetChanged();
                NavigationActivity.navigationEnemyAdapter.notifyDataSetChanged();
                finish();
            }
            enemyAdapter.notifyDataSetChanged();
        }


    };

    // thread per il tempo di caricamento delle arrmi
    private class reloadTask extends AsyncTask<MyWeaponsAdapter.ViewHolder, Void, Void> {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
