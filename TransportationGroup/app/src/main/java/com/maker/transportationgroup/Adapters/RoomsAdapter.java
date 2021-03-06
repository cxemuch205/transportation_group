package com.maker.transportationgroup.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.maker.contenttools.Models.TGGroup;
import com.maker.contenttools.R;

import java.util.ArrayList;


/**
 * Created by Daniil on 30-Mar-15.
 */
public class RoomsAdapter extends ArrayAdapter<TGGroup> {

    private Context context;
    private ArrayList<TGGroup> data;

    public RoomsAdapter(Context context, ArrayList<TGGroup> data) {
        super(context, R.layout.item_room, data);
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_room, parent, false);

            holder = new ViewHolder();
            holder.tvName = (TextView) view.findViewById(R.id.tv_name);
            holder.tvId = (TextView) view.findViewById(R.id.tv_id);
            holder.tvDescription = (TextView) view.findViewById(R.id.tv_description);
            holder.tvNumberOfMembers = (TextView) view.findViewById(R.id.tv_number_of_members);
            holder.tvNumberOfNews = (TextView) view.findViewById(R.id.tv_news_transportation);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        TGGroup item = data.get(position);
        holder.tvName.setText(item.name);
        holder.tvId.setText(context.getString(R.string.id) + " " + String.valueOf(item.id));
        if (item.description != null && !item.description.isEmpty()) {
            holder.tvDescription.setVisibility(TextView.VISIBLE);
            holder.tvDescription.setText(item.description);
        } else {
            holder.tvDescription.setVisibility(TextView.GONE);
        }
        holder.tvNumberOfMembers.setText(context.getString(R.string.members) + " "
                + String.valueOf(item.numberOfMembers));
        holder.tvNumberOfNews.setText(context.getString(R.string.news) + " "
                + String.valueOf(item.numberOfNews));

        return view;
    }

    private static class ViewHolder {
        TextView tvName;
        TextView tvId;
        TextView tvDescription;
        TextView tvNumberOfMembers;
        TextView tvNumberOfNews;
    }
}
