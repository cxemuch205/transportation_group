package com.maker.transportationgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;


public class TripsActivity extends AppCompatActivity {

    public static final String TAG = "TripsActivity";

    private ProgressBar pbLoad;
    private ListView lvTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lvTrips = (ListView) findViewById(R.id.lv_trips);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_trips, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
        }

        return super.onOptionsItemSelected(item);
    }
}
