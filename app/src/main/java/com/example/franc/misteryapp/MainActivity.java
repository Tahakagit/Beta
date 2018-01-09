package com.example.franc.misteryapp;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();

        setListView();
        setInputForm();
    }

    public void setListView(){
        ListView list = (ListView)findViewById(R.id.view_listview_main);
        RealmHelper helper = new RealmHelper();
        final MyListAdapter adapter = new MyListAdapter(helper.retrieveAllItem());
        list.setAdapter(adapter);
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
    }

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

}
