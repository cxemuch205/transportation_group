package com.maker.transportationgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Models.ApiResponse;
import com.maker.contenttools.Models.TGGroup;
import com.maker.contenttools.Models.Trip;
import com.maker.contenttools.Tools;
import com.maker.transportationgroup.Adapters.TripsAdapter;

import java.util.ArrayList;


public class TripsActivity extends AppCompatActivity {

    public static final String TAG = "TripsActivity";

    private ProgressBar pbLoad;
    private ListView lvTrips;
    private TextView tvMessage;
    private Api api;
    private TripsAdapter adapter;

    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lvTrips = (ListView) findViewById(R.id.lv_trips);
        tvMessage = (TextView) findViewById(R.id.tv_message);

        api = new Api(this);

        groupId = getIntent().getExtras().getString(App.Keys.ID);
        if (groupId != null && !groupId.isEmpty()) {
            loadGroup(groupId);
        } else {
            Tools.showToastCenter(this, getString(R.string.group_is_empty));
        }
    }

    private void loadGroup(String groupId) {
        enablePB(true);
        api.requestGetTripsByGroupId(groupId, responseListener, errorListener);
    }

    private Response.Listener<String> responseListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.i(TAG, "RESPONSE:\n" + response);
            //TODO: will make parse data and put to listView
            /*ApiResponse apiResponse = ApiParser.getGson().fromJson(response, ApiResponse.getTypeToken());
            if (apiResponse != null && apiResponse.isSuccess()) {
                //TODO: parse data
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupAdapter(new ArrayList<Trip>());
                    }
                });
            }*/
            enablePB(false);
        }
    };

    private void setupAdapter(ArrayList<Trip> trips) {
        if (adapter == null) {
            adapter = new TripsAdapter(this, trips);
        } else {
            adapter.clear();
            adapter.addAll(trips);
        }

        lvTrips.setAdapter(adapter);
    }

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Tools.processError(TripsActivity.this, error);
            enablePB(false);
        }
    };

    private void enablePB(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    tvMessage.setVisibility(TextView.GONE);
                    pbLoad.setVisibility(ProgressBar.VISIBLE);
                } else {
                    if (adapter != null && adapter.isEmpty()) {
                        tvMessage.setText(R.string.empty_trips);
                        tvMessage.setVisibility(TextView.VISIBLE);
                    }
                    pbLoad.setVisibility(ProgressBar.GONE);
                }
            }
        });
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
