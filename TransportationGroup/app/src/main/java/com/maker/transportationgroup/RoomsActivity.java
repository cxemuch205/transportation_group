package com.maker.transportationgroup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Models.ApiResponse;
import com.maker.contenttools.Tools;
import com.maker.contenttools.Views.FloatingActionButton;
import com.maker.transportationgroup.Adapters.RoomsAdapter;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Models.TGGroup;

import java.util.ArrayList;


public class RoomsActivity extends AppCompatActivity {

    public static final String TAG = "RoomsActivity";

    private ProgressBar pbLoad;
    private ListView lvGroups;
    private TextView tvMessage;
    private RoomsAdapter adapter;
    private Api api;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lvGroups = (ListView) findViewById(R.id.lv_list_groups);
        tvMessage = (TextView) findViewById(R.id.tv_message);

        api = new Api(this);
        createActionFloatingButton();
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
                Intent addNewGroup = new Intent(RoomsActivity.this, AddRoomsActivity.class);
                startActivity(addNewGroup);
            }
        });
    }

    private void initListeners() {
        lvGroups.setOnItemClickListener(itemRoomClickListener);
    }

    private void enablePB(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    tvMessage.setVisibility(TextView.GONE);
                    pbLoad.setVisibility(ProgressBar.VISIBLE);
                } else {
                    if (adapter != null && adapter.isEmpty()) {
                        tvMessage.setText(R.string.empty_my_rooms);
                        tvMessage.setVisibility(TextView.VISIBLE);
                    }
                    pbLoad.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }

    private void executeLoadRooms() {
        enablePB(true);
        api.requestGetMyGroups(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    Log.i(TAG, "RESPONSE: " + response);
                    final ArrayList<TGGroup> rooms = ApiParser.getGson().fromJson(response, TGGroup.getArrayTypeToken());
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
                Tools.processError(RoomsActivity.this, error);
                enablePB(false);
            }
        });
    }

    private void setupAdapter(ArrayList<TGGroup> rooms) {
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
            TGGroup group = adapter.getItem(position);
            if (group != null) {
                Intent openListTrips = new Intent(RoomsActivity.this, TripsActivity.class);
                openListTrips.putExtra(App.Keys.ID, String.valueOf(group.id));
                startActivity(openListTrips);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rooms, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sign_out:
                showDialogSignOut();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDialogSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.sign_out);
        builder.setMessage(R.string.are_you_sure);
        builder.setPositiveButton(R.string.action_sign_out, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                signOut();
                dialog.cancel();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void signOut() {
        enablePB(true);
        api.requestSignOut(new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    ApiResponse apiResponse = ApiParser.getGson().fromJson(response, ApiResponse.getTypeToken());
                    if (apiResponse != null && apiResponse.isSuccess()) {
                        Tools.dropUserData(RoomsActivity.this);
                        Tools.setUserIsRegistered(RoomsActivity.this, false);
                        openLogin();
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

    private void openLogin() {
        Intent login = new Intent(RoomsActivity.this, LoginActivity.class);
        RoomsActivity.this.startActivity(login);
        RoomsActivity.this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        executeLoadRooms();
        initListeners();
    }
}
