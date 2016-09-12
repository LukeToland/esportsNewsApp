package com.project.opticproject;

/**
 * Created by Luke on 02/09/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PlayerListAdapter extends ArrayAdapter<NewsItem> {

    private final Activity context;
    private ArrayList<Player> players;

    public PlayerListAdapter(Activity context, ArrayList<Player> players) {
        super(context, R.layout.listfeed);
        // TODO Auto-generated constructor stub

        this.players = players;
        this.context = context;

    }

    private static class PlanetHolder {
        public TextView txtName;
        public TextView txtKD;
        public TextView txtSPM;
        public ImageView img;

    }

    @Override
    public int getCount() {
        return players.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        PlanetHolder holder = new PlanetHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.players_feed, null);
            // Now we can fill the layout with the right values
            TextView txtName = (TextView) v.findViewById(R.id.name);
            TextView txtKD = (TextView) v.findViewById(R.id.kd);
            TextView txtSPM = (TextView) v.findViewById(R.id.spm);
            ImageView img = (ImageView) v.findViewById(R.id.icon);

            holder.txtName = txtName;
            holder.txtKD = txtKD;
            holder.txtSPM = txtSPM;
            holder.img = img;

            v.setTag(holder);
        } else {
            holder = (PlanetHolder) v.getTag();
        }

        String imageUrl = players.get(position).getPhoto();
        String nameof = players.get(position).getName();
        String kill = players.get(position).getKd();
        String spm = players.get(position).getSpm();

       // String pub = players.get(position).getSpm();

        holder.txtKD.setText(kill);
        holder.txtSPM.setText(spm);
        holder.txtName.setText(nameof);

        if (imageUrl.equals("")) {
            holder.img.setBackgroundResource(R.drawable.noimg);
        }
        else if (imageUrl.contains("gamurs.com")) {
            Picasso.with(context)
                    .load(imageUrl)
                    .resize(480, 340)
                    //now we have an ImageView, that we can use as target
                    .into(holder.img);

        }
        else {
            Picasso.with(context)
                    .load(imageUrl)
                    //now we have an ImageView, that we can use as target
                    .into(holder.img);

        }


        v.setBackgroundColor(Color.WHITE);

        return v;
    }


    //we need this class as holder object
    public static class PlaceViewHolder {
        private ImageView placeIcon;
        private TextView title;
    }
}


