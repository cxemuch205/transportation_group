package com.maker.transportationgroup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Adapters.RoomsAdapter;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Models.TGRoom;

import org.json.JSONArray;

import java.util.ArrayList;


public class AddRoomsActivity extends ActionBarActivity {

    public static final String TAG = "AddRoomsActivity";

    private ProgressBar pbLoad;
    private ListView lvGroups;
    private TextView tvMessage;
    private RoomsAdapter adapter;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lvGroups = (ListView) findViewById(R.id.lv_list_groups);
        tvMessage = (TextView) findViewById(R.id.tv_message);

        api = new Api(this);

        executeLoadRooms();
        initListeners();
    }

    private void initListeners() {
        lvGroups.setOnItemClickListener(itemRoomClickListener);
    }

    private void enablePB(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    pbLoad.setVisibility(ProgressBar.VISIBLE);
                } else {
                    pbLoad.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }

    private void executeLoadRooms() {
        enablePB(true);
        api.requestGetRooms(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    Log.i(TAG, "RESPONSE: " + response);
                    final ArrayList<TGRoom> rooms = ApiParser.getGson().fromJson(String.valueOf(response), TGRoom.getArrayTypeToken());
                    if (rooms != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupAdapter(rooms);
                            }
                        });
                    }
                }

                enablePB(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                enablePB(false);
            }
        });
    }

    private void setupAdapter(ArrayList<TGRoom> rooms) {
        if (adapter == null) {
            adapter = new RoomsAdapter(this, rooms);
        } else {
            adapter.clear();
            adapter.addAll(rooms);
        }

        lvGroups.setAdapter(adapter);
    }

    private AdapterView.OnItemClickListener itemRoomClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //TODO: open room if user have permission for this room or user, put password for this room

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_create_room:
                //TODO: make activity, where user can create Group
                //Intent addNewGroup = new Intent(this, AddRoomsActivity.class);
                //startActivity(addNewGroup);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}