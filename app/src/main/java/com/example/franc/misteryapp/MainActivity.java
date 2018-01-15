package com.example.franc.misteryapp;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    TextView io;
    Realm mRealm = null;
    static RealmHelper helper = new RealmHelper();
    static Player  getPlayer = null;
    private final static int INTERVAL = 2000; //2 minutes
    static Handler mHandler = new Handler();
    static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    static MyListAdapter adapter;
    static ListView list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);

        io = (TextView)findViewById(R.id.health);
        isPlayer();
/*
        io = findViewById(R.id.my_health);
*/




/*
        Enemy newEnemy = new Enemy();

        mHandler.post(newEnemy);
        mHandler.postDelayed(newEnemy, 2000);
*/
/*
        io.setText(String.valueOf(getPlayer.getHealth()));
*/

        list = (ListView)findViewById(R.id.view_listview_main);

        adapter = new MyListAdapter(this, enemies);
        list.setAdapter(adapter);
        BackgroundTask task = new BackgroundTask();
        task.execute();

  /*      setInputForm();
*/
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

    public void setListView(){
        list = (ListView)findViewById(R.id.view_listview_main);
        adapter = new MyListAdapter(this, enemies);
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


private class BackgroundTask extends AsyncTask<Void, Integer, BackgroundTask.Wrapper>{

    public class Wrapper{

        ArrayList<Enemy> enemi;
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
    protected Wrapper doInBackground(Void... arg0) {
        Enemy enemy = new Enemy();
        Wrapper wrapper = new Wrapper();


        if (enemy.isAdded() == false){
            enemy.setName("Pino");
            enemy.setAdded(true);
            enemies.add(enemy);
            adapter = new MyListAdapter(MainActivity.this, wrapper.enemi);


        }
        if (enemy.getHealth() > 0) {
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

        wrapper.enemi = enemies;
        wrapper.salute = enemy.getHealth();
        return wrapper;
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
        io.setText(String.valueOf(values[0]));
    }

    @Override
    protected void onPostExecute(Wrapper wrapper){
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
/*
    void spawnEnemy()
    {
        mHandlerTask.run();


    }
*/

/*
    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);
    }
*/


}
