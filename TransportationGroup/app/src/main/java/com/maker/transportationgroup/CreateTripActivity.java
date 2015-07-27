package com.maker.transportationgroup;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.maker.contenttools.Api.Api;
import com.maker.contenttools.Constants.App;
import com.maker.contenttools.Models.Trip;
import com.maker.contenttools.Tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTripActivity extends AppCompatActivity {

    public static final String TAG = "CreateTripActivity";

    private Api api;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("kk:mm");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MMM/yy");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat fullDateFormatter = new SimpleDateFormat("dd/MMM/yy kk:mm");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dayFormatter = new SimpleDateFormat("dd");

    private int timeHoursDeparture, timeMinuteDeparture, dateYearDeparture, dateMonthDeparture, dateDayDeparture;
    private int timeHoursArrival, timeMinuteArrival, dateYearArrival, dateMonthArrival, dateDayArrival;
    private boolean isDeparture = true;

    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private ProgressDialog pd;
    private String mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip);

        api = new Api(this);
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.wait_please));

        mGroupId = getIntent().getStringExtra(App.Keys.ID);

        initActionBar();
        initUI();
        initTimeDateDialog();
        initListeners();
    }

    private void initActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private AutoCompleteTextView etDeparture;
    private AutoCompleteTextView etArrival;
    private TextView tvTimeDepartire;
    private TextView tvDateDepartire;
    private TextView tvTimeArrival;
    private TextView tvDateArrival;
    private LinearLayout llSetTimeDeparture;
    private LinearLayout llSetDateDeparture;
    private LinearLayout llSetTimeArrival;
    private LinearLayout llSetDateArrival;
    private CheckBox cbTypeCargo;
    private CheckBox cbTypeDocs;
    private CheckBox cbTypePeople;
    private EditText etSetMaxWeightVolume;
    private EditText etSetMaxPeople;
    private LinearLayout llBlockSetWeight;
    private LinearLayout llBlockSetPeople;
    private Button btnCreate;

    private void initUI() {
        etDeparture = (AutoCompleteTextView) findViewById(R.id.et_departure_place);
        etArrival = (AutoCompleteTextView) findViewById(R.id.et_arrival_place);
        tvTimeDepartire = (TextView) findViewById(R.id.tv_time_departure);
        tvDateDepartire = (TextView) findViewById(R.id.tv_date_departure);
        tvTimeArrival = (TextView) findViewById(R.id.tv_time_arrival);
        tvDateArrival = (TextView) findViewById(R.id.tv_date_arrival);
        llSetTimeDeparture = (LinearLayout) findViewById(R.id.ll_set_time_departure);
        llSetDateDeparture = (LinearLayout) findViewById(R.id.ll_set_date_departure);
        llSetTimeArrival = (LinearLayout) findViewById(R.id.ll_set_time_arrival);
        llSetDateArrival = (LinearLayout) findViewById(R.id.ll_set_date_arrival);
        cbTypeCargo = (CheckBox) findViewById(R.id.cb_type_cargo);
        cbTypeDocs = (CheckBox) findViewById(R.id.cb_type_docs);
        cbTypePeople = (CheckBox) findViewById(R.id.cb_type_people);
        etSetMaxWeightVolume = (EditText) findViewById(R.id.et_max_volume_weight);
        etSetMaxPeople = (EditText) findViewById(R.id.et_max_people);
        llBlockSetWeight = (LinearLayout) findViewById(R.id.ll_block_cargo_weight);
        llBlockSetPeople = (LinearLayout) findViewById(R.id.ll_block_max_people);
        btnCreate = (Button) findViewById(R.id.btn_create);
    }

    private void initTimeDateDialog() {
        timePickerDialog = new TimePickerDialog(
                this,
                timeSetListener,
                0,
                0,
                true
        );
        Date currentDate = new Date();
        datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                Integer.parseInt(yearFormatter.format(currentDate)),
                currentDate.getMonth(),
                Integer.parseInt(dayFormatter.format(currentDate))
        );
    }

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Log.d(TAG, "isDeparture: " + isDeparture + "\n hours: " + hourOfDay + "\n minute: " + minute);
            if (isDeparture) {
                timeHoursDeparture = hourOfDay;
                timeMinuteDeparture = minute;
                tvTimeDepartire.setText(timeFormatter.format(new Date(1, 0, 1, hourOfDay, minute)));
            } else {
                timeHoursArrival = hourOfDay;
                timeMinuteArrival = minute;
                tvTimeArrival.setText(timeFormatter.format(new Date(1, 0, 1, hourOfDay, minute)));
            }
        }
    };

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.d(TAG, "isDeparture: " + isDeparture + "\n Year: " + year + "\n Month: " + monthOfYear + "\n Day: " + dayOfMonth);
            if (isDeparture) {
                dateYearDeparture = year;
                dateMonthDeparture = monthOfYear;
                dateDayDeparture = dayOfMonth;
                tvDateDepartire.setText(dateFormatter.format(new Date(year, monthOfYear, dayOfMonth)));
            } else {
                dateYearArrival = year;
                dateMonthArrival = monthOfYear;
                dateDayArrival = dayOfMonth;
                tvDateArrival.setText(dateFormatter.format(new Date(year, monthOfYear, dayOfMonth)));
            }
        }
    };

    private void initListeners() {
        cbTypeCargo.setOnCheckedChangeListener(checkedCargoListener);
        cbTypePeople.setOnCheckedChangeListener(checkedPeopleListener);

        llSetTimeDeparture.setOnClickListener(clickLLSetTimeDeparture);
        llSetDateDeparture.setOnClickListener(clickLLSetDateDeparture);
        llSetTimeArrival.setOnClickListener(clickLLSetTimeArrival);
        llSetDateArrival.setOnClickListener(clickLLSetDateArrival);

        btnCreate.setOnClickListener(clickCreateBtnListener);
    }

    private CompoundButton.OnCheckedChangeListener checkedCargoListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                llBlockSetWeight.setVisibility(LinearLayout.VISIBLE);
            } else {
                llBlockSetWeight.setVisibility(LinearLayout.GONE);
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener checkedPeopleListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                llBlockSetPeople.setVisibility(LinearLayout.VISIBLE);
            } else {
                llBlockSetPeople.setVisibility(LinearLayout.GONE);
            }
        }
    };

    private View.OnClickListener clickLLSetTimeDeparture = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isDeparture = true;
            timePickerDialog.show();
        }
    };

    private View.OnClickListener clickLLSetDateDeparture = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isDeparture = true;
            datePickerDialog.show();
        }
    };

    private View.OnClickListener clickLLSetTimeArrival = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isDeparture = false;
            timePickerDialog.show();

        }
    };

    private View.OnClickListener clickLLSetDateArrival = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isDeparture = false;
            datePickerDialog.show();
        }
    };

    private View.OnClickListener clickCreateBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            executeCreateTrip();
        }
    };

    private void executeCreateTrip() {
        Trip trip = new Trip();
        if (cbTypePeople.isChecked() && !etSetMaxPeople.getText().toString().isEmpty()) {
            trip.maxPeople = Integer.parseInt(etSetMaxPeople.getText().toString());
        }
        if (cbTypeCargo.isChecked() && !etSetMaxWeightVolume.getText().toString().isEmpty()) {
            trip.maxWeightBagage = Integer.parseInt(etSetMaxWeightVolume.getText().toString());
        }

        if (checkData(trip)) {
            pd.show();

            api.requestCreateTrip(mGroupId, trip, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i(TAG, "RESPONSE: " + response);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            finish();
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Tools.processError(CreateTripActivity.this, error);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                        }
                    });
                }
            });
        } else {
            trip = null;
        }
    }

    private boolean checkData(final Trip trip) {
        //TODO: check correct and full data for trip
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_create_trip, menu);
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
