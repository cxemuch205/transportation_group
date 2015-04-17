package com.maker.transportationgroup;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Adapters.RoomsAdapter;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Interfaces.OnDialogAdapter;
import com.maker.contenttools.Interfaces.OnDialogListener;
import com.maker.contenttools.Models.ApiResponse;
import com.maker.contenttools.Models.TGGroup;
import com.maker.contenttools.Tools;

import org.json.JSONArray;

import java.util.ArrayList;


public class AddRoomsActivity extends ActionBarActivity {

    public static final String TAG = "AddRoomsActivity";
    private static final int REQUEST_CREATE_GROUP = 102;

    private ProgressBar pbLoad;
    private ListView lvGroups;
    private TextView tvMessage;
    private EditText etTextSearch;
    private RoomsAdapter adapter;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        lvGroups = (ListView) findViewById(R.id.lv_list_groups);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        etTextSearch = (EditText) findViewById(R.id.et_text_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        api = new Api(this);

        executeLoadRooms();
        initListeners();
    }

    private void initListeners() {
        lvGroups.setOnItemClickListener(itemRoomClickListener);
        etTextSearch.addTextChangedListener(textChangeListener);
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
        api.requestGetGroups(new Response.Listener<String>() {
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

    private TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count > 2) {
                existSearch(String.valueOf(s));
            } else if (count == 0) {
                executeLoadRooms();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void existSearch(String text) {
        api.requestSearchGroups(text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null && response.length() > 0) {
                    final ArrayList<TGGroup> tgGroups = ApiParser.getGson().fromJson(response, TGGroup.getArrayTypeToken());
                    if (tgGroups != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setupAdapter(tgGroups);
                            }
                        });
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    private AdapterView.OnItemClickListener itemRoomClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TGGroup room = adapter.getItem(position);
            if (room != null) {
                Dialog dialogAuth = Tools.buildDialogAuthRoom(AddRoomsActivity.this, room, dialogCallback);
                dialogAuth.show();
            }
        }
    };

    private OnDialogListener dialogCallback = new OnDialogAdapter() {
        @Override
        public void onOK(Intent data) {
            super.onOK(data);
            if (data != null && !data.getExtras().isEmpty()) {
                requestAddRoom(data);
            }
        }

        @Override
        public void onCancel(Intent data) {
            super.onCancel(data);

        }
    };

    private void requestAddRoom(Intent data) {
        api.requestAddGroup(data, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //TODO: parse response by add new room to myRooms list, if password no correct reshow dialog for put new password
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    String response = new String(error.networkResponse.data);
                    ApiResponse apiResponse = ApiParser.getGson()
                            .fromJson(response,
                                    ApiResponse.getTypeToken());
                    if (apiResponse != null) {
                        Tools.showToastCenter(AddRoomsActivity.this, apiResponse.errors.full_messages.get(0));
                    }
                } catch (Exception e) {}
            }
        });
    }

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
                Intent createNewGroup = new Intent(this, CreateRoomActivity.class);
                startActivityForResult(createNewGroup, REQUEST_CREATE_GROUP);
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_GROUP && resultCode == RESULT_OK) {
            executeLoadRooms();
        }
    }
}
