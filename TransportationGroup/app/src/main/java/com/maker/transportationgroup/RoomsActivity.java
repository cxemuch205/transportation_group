package com.maker.transportationgroup;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;


public class RoomsActivity extends ActionBarActivity {

    private ProgressBar pbLoad;
    private ListView lvGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lvGroups = (ListView) findViewById(R.id.lv_list_groups);
    }
}
