package com.maker.transportationgroup;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;


public class AddRoomsActivity extends AppCompatActivity {

    public static final String TAG = "AddRoomsActivity";
    private static final int REQUEST_CREATE_GROUP = 102;

    private ProgressBar pbLoad, pbExecute;
    private ListView lvGroups;
    private TextView tvMessage;
    private EditText etTextSearch;
    private RoomsAdapter adapter;
    private Api api;
    private Dialog dialogAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);
        pbExecute = (ProgressBar) findViewById(R.id.pb_execute);
        lvGroups = (ListView) findViewById(R.id.lv_list_groups);
        tvMessage = (TextView) findViewById(R.id.tv_message);
        etTextSearch = (EditText) findViewById(R.id.et_text_data);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        api = new Api(this);
        etTextSearch.setHint(R.string.search_hint);

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

    private void enableExecutePB(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (enable) {
                    pbExecute.setVisibility(ProgressBar.VISIBLE);
                } else {
                    pbExecute.setVisibility(ProgressBar.GONE);
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
                enableExecutePB(false);
                executeLoadRooms();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void existSearch(String text) {
        enableExecutePB(true);
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
                enableExecutePB(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                enableExecutePB(false);
            }
        });
    }

    private AdapterView.OnItemClickListener itemRoomClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TGGroup room = adapter.getItem(position);
            if (room != null) {
                dialogAuth = Tools.buildDialogAuthRoom(AddRoomsActivity.this, room, dialogCallback);
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
            dialogAuth = null;
        }
    };

    private void requestAddRoom(Intent data) {
        api.requestAddGroup(data, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                processReponse(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                processError(error);
            }
        });
    }

    private void processReponse(String response) {
        Log.d(TAG, "RESPONSE ADD: \n" + response);
        if (response != null && !response.isEmpty()) {
            Tools.showToastCenter(AddRoomsActivity.this, getString(R.string.added));
        }
    }

    private void processError(VolleyError error) {
        if (error != null) {
            try {
                String response = new String(error.networkResponse.data);
                Log.d(TAG, "RESPONSE ERROR: " + response);
                ApiResponse apiResponse = ApiParser.getGson()
                        .fromJson(response,
                                ApiResponse.getTypeToken());
                if (apiResponse != null && apiResponse.errors != null) {
                    if (apiResponse.errors.full_messages != null) {
                        Tools.showToastCenter(AddRoomsActivity.this,
                                Tools.convertArrayToString(apiResponse.errors.full_messages));
                    } else {
                        Tools.showToastCenter(AddRoomsActivity.this, apiResponse.errors.password);
                        if (apiResponse.errors.password != null
                                && !apiResponse.errors.password.isEmpty()
                                && apiResponse.errors.password.equals("incorrect")
                                && dialogAuth != null) {
                            dialogAuth.show();
                        } else {
                            dialogAuth = null;
                        }
                    }
                } else {
                    dialogAuth = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
