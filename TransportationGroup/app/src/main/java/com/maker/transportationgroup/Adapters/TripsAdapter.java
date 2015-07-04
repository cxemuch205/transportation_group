package com.maker.transportationgroup.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maker.contenttools.Models.Trip;
import com.maker.transportationgroup.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Daniil on 04.07.2015.
 */
public class TripsAdapter extends ArrayAdapter<Trip> {

    public static final String NONE = "NONE";

    private Context context;
    private ArrayList<Trip> data;
    private SimpleDateFormat dateFormat
            = new SimpleDateFormat("dd/MMM/yyyy kk:mm");

    public TripsAdapter(Context context, ArrayList<Trip> data) {
        super(context, R.layout.item_trip, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_trip, null);

            holder = new ViewHolder();
            holder.tvStartLocation = (TextView) view.findViewById(R.id.tv_start_location);
            holder.tvStartTime = (TextView) view.findViewById(R.id.tv_start_date);
            holder.tvEndLocation = (TextView) view.findViewById(R.id.tv_end_location);
            holder.tvEndTime = (TextView) view.findViewById(R.id.tv_end_date);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Trip item = data.get(position);
        if (item.getStartPoint() != null) {
            holder.tvStartLocation.setText(item.getStartPoint().getLocation());
            try {
                holder.tvStartTime.setText(dateFormat.format(item.getStartPoint().getDateTrip()));
            } catch (Exception e) {
                holder.tvStartTime.setText(NONE);
            }
        } else {
            holder.tvStartLocation.setText(NONE);
            holder.tvStartTime.setText(NONE);
        }
        if (item.getEndPoint() != null) {
            holder.tvEndLocation.setText(item.getEndPoint().getLocation());
            try {
                holder.tvEndTime.setText(dateFormat.format(item.getEndPoint().getDateTrip()));
            } catch (Exception e) {
                holder.tvEndTime.setText(NONE);
            }
        } else {
            holder.tvEndLocation.setText(NONE);
            holder.tvEndTime.setText(NONE);
        }

        return view;
    }

    private static class ViewHolder {
        TextView tvStartLocation;
        TextView tvEndLocation;
        TextView tvStartTime;
        TextView tvEndTime;
    }
}
