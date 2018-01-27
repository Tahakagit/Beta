package com.example.franc.misteryapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class DialogActivity extends AppCompatActivity {

    static int i = 0;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    final ArrayList<Fragment> fragments = new ArrayList <>();

    static Button next;
    static Button prev;
    static String conto2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);

        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        fragments.add(new DialogFragmentGoto());

        fragmentTransaction.add(R.id.fragmentcontainer, fragments.get(i));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();



    }

    //ON PREV PRESS
    public void goBack(){
        --i;
        FragmentTransaction ft2 = fragmentManager.beginTransaction();
        ft2.replace(R.id.fragmentcontainer, fragments.get(i));
        ft2.addToBackStack(null);
        ft2.commit();
    }




    //ON NEXT PRESS RECUPERA DATI
    public void getLocation(String star) {
        RealmHelper helper = new RealmHelper();
        conto2 = star;
        helper.setPlayerLocation(conto2);
        NavigationActivity.navigationAdapter.UpdateAdapter(helper.getPlacesAtPLayerPosition());
        i = 0;
        finish();

    }

    //todo interfacciarsi con navigation activity
    public interface sendLocationIface{
        void sendLocation(String location);
    }



    public void EndDialogActivity(){

    }
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(DialogActivity.this, "Oncreateview", Toast.LENGTH_SHORT);
    }

}
