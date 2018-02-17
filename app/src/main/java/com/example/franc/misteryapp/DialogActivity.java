package com.example.franc.misteryapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.ArrayList;

public class DialogActivity extends AppCompatActivity implements DialogFragmentGoto.SendToDialogActivity {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    final ArrayList<Fragment> fragments = new ArrayList <>();

    static Button next;
    static String starSystem = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        next = findViewById(R.id.next);

        fragments.add(new DialogFragmentGoto());
        startFragmentsInActivityDialog(fragments);
    }

    /**
     * Starts a fragments sequence in dialogactivity
     *
     * @param fragmentsArrayList arraylist filled with fragments to show
     */
    public void startFragmentsInActivityDialog(ArrayList<Fragment> fragmentsArrayList){
        int i = 0;
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment frag:fragmentsArrayList) {
            fragmentTransaction.add(R.id.fragmentcontainer, frag);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * SET PLAYER NEW LOCATION AND UPDATES UI
     * IFACE CALLED FROM DIALOGFRAMENTGOTO
     * @param star String new location to navigate to
     */
    public void navigateTo(String star) {
        RealmHelper helper = new RealmHelper();
        helper.setPlayerLocation(star);
        NavigationActivity.navigationEnemyAdapter.UpdateAdapter(helper.getEnemiesAtPLayerPosition());
        NavigationActivity.navigationAdapter.UpdateAdapter(helper.getPlacesAtPLayerPosition());
        finish();
    }
}
