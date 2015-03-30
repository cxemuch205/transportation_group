package com.maker.transportationgroup;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Adapters.RoomsAdapter;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Models.TGRoom;
import com.maker.contenttools.Tools;

import org.json.JSONArray;

import java.util.ArrayList;


public class RoomsActivity extends ActionBarActivity {

    public static final String TAG = "RoomsActivity";

    private ProgressBar pbLoad;
    private ListView lvGroups;
    private RoomsAdapter adapter;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lvGroups = (ListView) findViewById(R.id.lv_list_groups);

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
}
