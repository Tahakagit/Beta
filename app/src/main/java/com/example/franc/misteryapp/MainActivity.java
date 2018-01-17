package com.example.franc.misteryapp;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    static TextView io;
    Realm mRealm = null;
    static RealmHelper helper = new RealmHelper();
    static Player  getPlayer = null;
/*
    static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
*/
    static MyListAdapter adapter;
    static RecyclerView list;
    private RecyclerView.LayoutManager mLayoutManager;
    Handler mHandler = new Handler();
    int mHealth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        mRealm = Realm.getDefaultInstance();
/*
        spawnEnemies(generateEnemies(3));
*/

        io = (TextView)findViewById(R.id.health);
        isPlayer();
        list = (RecyclerView)findViewById(R.id.rv);


        Enemy2 runn = new Enemy2(mHandler);
        runn.run();
        mLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(mLayoutManager);


/*
        ArrayList<Enemy> enemies = spawnEnemies(generateEnemies(3));
*/

/*
        adapter = new MyListAdapter(enemies);
*/
        list.setAdapter(adapter);

        RealmResults res = mRealm.where(Player.)

    }
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

    final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
        }
    };
    private void updateUI(){
        //update ui
        io.setText(String.valueOf(mHealth));
    }

    private void doStuff(){
        //do other stuff
        enemyRoutine();
        mHandler.post(updateRunnable);
    }



    //todo spawnEnemies()
/*
    public ArrayList<Enemy> spawnEnemies(ArrayList<Enemy> enemies){

        BackgroundTask task = new BackgroundTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, enemies);

        return enemies;
    }
*/

    public void setListView(){
        list = (RecyclerView) findViewById(R.id.rv);
/*
        adapter = new MyListAdapter(enemies);
*/
        list.setAdapter(adapter);
        /**
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Vuoi davvero cancellare?");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RealmHelper helper = new RealmHelper();
                        helper.delItem(adapter.getItem(pos));
                    }
                });
                dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                return false;
            }
        });
        */
    }
    public int enemyRoutine(){
/*
        helper.dealDamage(realm, getPlayer, 20);
*/
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
        mHealth = salute;

        return salute;

    }


/**
    public void setInputForm(){
        Button sendBttn = findViewById(R.id.button_send);
        final EditText editT = findViewById(R.id.text_send);
        final EditText editC = findViewById(R.id.category_send);

        sendBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmHelper helper = new RealmHelper();

                //Get current time and date
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM   HH:mm.ss");
                String formattedDate = sdf.format(c.getTime());

                //create db entry
                Item item = new Item();
                item.setText(editT.getText().toString());
                item.setCategory(editC.getText().toString());
                item.setDateCreated(formattedDate);

                //post entry
                helper.addItem(item);

                editC.setText("");
                editT.setText("");
            }
        });
    }
*/


/**
    Runnable mHandlerTask = new Runnable(){
        Enemy enemy = new Enemy();

        @Override
        public void run() {
            if (enemy.isAdded() == false){
                enemy.setName("Pino");
                enemy.setAdded(true);
                enemies.add(enemy);
            }
            if (enemy.getHealth() <= 0) {
                mHandler.removeCallbacks(mHandlerTask);
            }
            enemyRoutine();
            mHandler.postDelayed(mHandlerTask, INTERVAL);

        }
    };
*/
    private ArrayList<Enemy> generateEnemies(int enemiesNumber){
        ArrayList<Enemy> enemies = new ArrayList<>();

        for (int i = 0; i < enemiesNumber; i++){

            Enemy enemy = new Enemy();
            enemies.add(enemy);
        }
        return enemies;
    }

    private class BackgroundTask extends AsyncTask<Enemy, Integer, Void>{

    //todo externalise class
    public class Wrapper{

        Enemy enemi;
        int salute;
    }

    void Sleep(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (Exception e)
        {
        }
    }
    @Override
    protected Void doInBackground(Enemy... arg0) {

        Wrapper wrapper = new Wrapper();


/*
        if (enemy.isAdded() == false){
            enemy.setName(givenUsingPlainJava_whenGeneratingRandomStringUnbounded_thenCorrect());
            enemy.setAdded(true);
            arg0[0].add(enemy);


        }
*/
        if (arg0[0].getHealth() > 0) {
            Realm realm = Realm.getDefaultInstance();

            player = realm.where(Player.class).findFirst();

            while (player.getHealth() > 0) {
                try {
    /*
                realm = Realm.getDefaultInstance();
    */
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            player.setHealth(player.getHealth() - 20);
                            realm.insertOrUpdate(player);
                            int salute = player.getHealth();

                            publishProgress(salute);
                            Sleep(2000);

                        }
                    });
                } finally {
                }
            }

            if(realm != null) {
                realm.close();
            }

        }
/*
        Sleep(2000);
*/

        wrapper.enemi = arg0[0];
/*
        wrapper.salute = enemy.getHealth();
*/
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
        io.setText(String.valueOf(values[0]));
/*
        adapter.updateData(enemies);
*/
    }

    @Override
    protected void onPostExecute(Void wrapper){

    }
    Player player;
    public int enemyRoutine(){
/*
        helper.dealDamage(realm, getPlayer, 20);
*/
        Realm realm = Realm.getDefaultInstance();

        player = realm.where(Player.class).findFirst();

        while (player.getHealth() > 0) {
            try {
    /*
                realm = Realm.getDefaultInstance();
    */
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        player.setHealth(player.getHealth() - 20);
                        realm.insertOrUpdate(player);

                    }
                });
            } finally {
            }
        }
        int salute = player.getHealth();

        if(realm != null) {
            realm.close();
        }
        return salute;

    }


    }
}
