package com.maker.transportationgroup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Api.ApiParser;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Models.ApiError;
import com.maker.contenttools.Models.ApiResponse;
import com.maker.contenttools.Tools;


public class CreateRoomActivity extends AppCompatActivity {

    public static final String TAG = "CreateRoomActivity";

    private EditText etName,
            etPassword,
            etPasswordConfirmed;
    private Button btnCreate;
    private ProgressBar pbLoad;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);
        setResult(RESULT_CANCELED);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        api = new Api(this);

        btnCreate = (Button) findViewById(R.id.btn_create);

        etName = (EditText) findViewById(R.id.et_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordConfirmed = (EditText) findViewById(R.id.et_password_confirmed);
        pbLoad = (ProgressBar) findViewById(R.id.pb_load);

        initListeners();
    }

    private void initListeners() {
        btnCreate.setOnClickListener(clickCreateGroupListener);
    }

    private View.OnClickListener clickCreateGroupListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            executeCreateGroup();
        }
    };

    private void executeCreateGroup() {
        String name = etName.getText().toString();
        String password = etPassword.getText().toString();
        String confirmedPassword = etPasswordConfirmed.getText().toString();

        if (!name.isEmpty() && !password.isEmpty() && !confirmedPassword.isEmpty()) {
            Intent data = new Intent();
            data.putExtra(App.Keys.NAME, name);
            data.putExtra(App.Keys.PASSWORD, password);
            data.putExtra(App.Keys.PASSWORD_CONFIGURED, confirmedPassword);

            enablePB(true);

            api.requestCreateGroup(data, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null && !response.isEmpty()) {
                        Tools.showToastCenter(CreateRoomActivity.this, getString(R.string.create_group_successful));
                        setResult(RESULT_OK);
                        finish();
                    }
                    enablePB(false);
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
                            Tools.showToastCenter(CreateRoomActivity.this,
                                    Tools.convertArrayToString(((ApiError)apiResponse.errors).full_messages));
                        }
                    } catch (Exception e) {}
                    enablePB(false);
                }
            });
        } else {
            Tools.showToastCenter(this, getString(R.string.empty_field));
        }
    }

    private void enablePB(final boolean enable) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                enableControls(!enable);
                if (enable) {
                    pbLoad.setVisibility(ProgressBar.VISIBLE);
                } else {
                    pbLoad.setVisibility(ProgressBar.GONE);
                }
            }
        });
    }

    private void enableControls(boolean enable) {
        etName.setEnabled(enable);
        etPassword.setEnabled(enable);
        etPasswordConfirmed.setEnabled(enable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
