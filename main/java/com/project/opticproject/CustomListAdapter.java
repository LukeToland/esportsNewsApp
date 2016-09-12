package com.project.opticproject;

/**
 * Created by Luke on 29/08/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<NewsItem> {

    private final Activity context;
    private ArrayList<NewsItem> newsItems;

    public CustomListAdapter(Activity context, ArrayList<NewsItem> newsItems) {
        super(context, R.layout.listfeed);
        // TODO Auto-generated constructor stub

        this.newsItems = newsItems;
        this.context = context;

    }

    private static class PlanetHolder {
        public TextView txtTitle;
        public TextView txtDate;
        public TextView txtAuthor;
        public ImageView img;

    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        PlanetHolder holder = new PlanetHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.listfeed, null);
            // Now we can fill the layout with the right values
            TextView tv = (TextView) v.findViewById(R.id.Itemname);
            TextView reporter = (TextView) v.findViewById(R.id.reporter);
            TextView date = (TextView) v.findViewById(R.id.date);
            ImageView img = (ImageView) v.findViewById(R.id.icon);

            holder.txtTitle = tv;
            holder.txtDate = date;
            holder.txtAuthor = reporter;
            holder.img = img;

            v.setTag(holder);
        } else {
            holder = (PlanetHolder) v.getTag();
        }

        String imageUrl = newsItems.get(position).getImage();
        String header = newsItems.get(position).getNewsHeading();
        String author = newsItems.get(position).getAuthor();
        String pub = newsItems.get(position).getDatePublished();

        holder.txtDate.setText(pub);
        holder.txtAuthor.setText(author);
        holder.txtTitle.setText(header);

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


