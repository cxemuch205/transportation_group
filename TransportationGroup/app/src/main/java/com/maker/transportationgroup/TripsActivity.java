package com.maker.transportationgroup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Models.Trip;
import com.maker.contenttools.Tools;
import com.maker.contenttools.Views.FloatingActionButton;
import com.maker.transportationgroup.Adapters.TripsAdapter;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class TripsActivity extends AppCompatActivity {

    public static final String TAG = "TripsActivity";

    private ProgressBar pbLoad;
    private ListView lvTrips;
    private TextView tvMessage;
    private Api api;
    private TripsAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private AsyncTask threadRepeatLoadTrips;

    private String groupId;
    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lvTrips = (ListView) findViewById(R.id.lv_trips);
        tvMessage = (TextView) findViewById(R.id.tv_message);


        api = new Api(this);

        groupId = getIntent().getExtras().getString(App.Keys.ID);
        groupName = getIntent().getExtras().getString(App.Keys.NAME);

        initActionBar();
        createActionFloatingButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (groupId != null && !groupId.isEmpty()) {
            loadAllTripsByGroupId(groupId, true);
        } else {
            Tools.showToastCenter(this, getString(R.string.group_is_empty));
        }
        runTimerRepeat();
    }

    private void runTimerRepeat() {
        threadRepeatLoadTrips = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                while (!isCancelled()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loadAllTripsByGroupId(groupId, false);
                }
                return null;
            }
        };
        threadRepeatLoadTrips.execute();
    }

    private void createActionFloatingButton() {
        floatingActionButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp))
                .withButtonColor(getResources().getColor(R.color.floating_btn))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewTrip = new Intent(TripsActivity.this, CreateTripActivity.class);
                addNewTrip.putExtra(App.Keys.ID, groupId);
                startActivity(addNewTrip);
            }
        });
    }

    private void initActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setSubtitle(groupName);
        }
    }

    private void loadAllTripsByGroupId(String groupId, boolean showProgressDialog) {
        enablePB(showProgressDialog);
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
                    if (adapter == null || adapter.isEmpty()) {
                        tvMessage.setText(R.string.empty_trips);
                        tvMessage.setVisibility(TextView.VISIBLE);
                    }
                    pbLoad.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (threadRepeatLoadTrips != null) {
            threadRepeatLoadTrips.cancel(false);
        }
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
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
