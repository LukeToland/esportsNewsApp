package com.project.opticproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    String urlClick;

    //All the RSS feeds/ HTML Pages needed
    String opticGamingSiteURL = ("http://optic.tv/news/");
    String dexertoURL = ("http://www.dexerto.com/esports/cod/");
    String mlgURL = ("http://www.majorleaguegaming.com/news/");
    String gamursURL = ("https://gamurs.com/g/cod");
    String callOfDutyURL = ("https://www.callofduty.com/esports/news");

    //Arrays to store all data form each site
    ArrayList<NewsItem> mainList;//Going to store every article
    ArrayList<NewsItem> dexertoData;
    CustomListAdapter adapter;

    //Views
    ListView lv;
    Toolbar mActionBarToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Title bar
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(" CoD eSports News ");
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        //Dropdown selector
        Spinner staticSpinner = (Spinner) findViewById(R.id.static_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.site_names,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
        staticSpinner.setOnItemSelectedListener (this);

        //List view
        lv = (ListView) findViewById(R.id.list);

        mainList = new ArrayList<>();

        //Execute asynctasks
//        if (adapter== null) {
//            new gamursPageData().execute();//Run parser on optic site
//        }


    }

    public void addItemsToList()
    {
        try{
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        }catch (Exception e)
        {

        }
        //Add data to list
        //ArrayAdapter<NewsItem> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.listfeed, R.id.Itemname, mainList);
        adapter=new CustomListAdapter(MainActivity.this, mainList);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String linkTo = mainList.get(position).getLink();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(linkTo));
        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = (String) parent.getItemAtPosition(position);
        if(selectedItem.equals("CWL.com")){
            mainList = new ArrayList<>();//Empty current mainList
            new codPageData().execute();//Run parser on optic site
        }
        else if(selectedItem.equals("Dexerto.com")){
            mainList = new ArrayList<>();
            new dexertoPageData().execute();
        }
        else if(selectedItem.equals("Gamurs.com")){
            mainList = new ArrayList<>();
            new gamursPageData().execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Asycn task to retrieve webpage
    class dexertoPageData extends AsyncTask<String, Void, String>
    {
        //Main page details
        org.jsoup.nodes.Document doc;
        Elements docElements;
        String content;
        Elements link;

        //Article details, e.g. Author, date
        org.jsoup.nodes.Document dexertoArticle;
        ArrayList<ArticleData> articleData;


        @Override
        protected String doInBackground(String... arg0){
            try {
                doc = Jsoup.connect(dexertoURL)
                        .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                        .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(6000000)
                        .get();

                //Retrieve all info for class in HTML
                docElements = doc.getElementsByClass("match");
                content = docElements.text();

                //Retrieve info for this attribute
                link =  doc.getElementsByAttributeValue("class", "match");

                publishProgress();

            }catch (IOException e){
                e.printStackTrace();
            }
            return "Executed";

        }
        protected void onPostExecute(String result)
        {
            //Get images
            try{
                Elements pictures = doc.getElementsByClass("dx-grid-thumb-container");
                Elements images=doc.select("div.dx-grid-thumb-container").select("img");

                for(int i = 0; i< link.size(); i++) {
                    String title = docElements.get(i).text();

                    String linkForItem = link.get(i).select("a").attr("href").toString();

                    String imageUrl = images.get(i).attr("data-original");

                    mainList.add(new NewsItem(linkForItem, title, imageUrl));

                }
                //Add all to listView
                addItemsToList();

            }catch (Exception e){

            }
        }
        protected void onPreExecute(String res){

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    //Asycn task to retrieve webpage
    class opticPageData extends AsyncTask<String, Void, String>
    {
        org.jsoup.nodes.Document doc;
        Elements docElements;
        String content;
        Elements link;

        @Override
        protected String doInBackground(String... arg0){
            try {
                doc = Jsoup.connect(opticGamingSiteURL)
                        .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                        .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(6000000)
                        .get();

                docElements = doc.getElementsByClass("slide-content");
                content = docElements.text();

            }catch (IOException e){
                e.printStackTrace();
            }
            return "Executed";

        }
        protected void onPostExecute(String result)
        {
            dexertoData = new ArrayList<>();

            for(int i = 0; i< docElements.size(); i++) {
                String additionalText = docElements.get(i).text();
                //Link
                String linkForItem = docElements.get(i).select("a").attr("href").toString();
            }

            //Add all to listView
            addItemsToList();
        }
        protected void onPreExecute(String res){

        }
    }

    //Asycn task to retrieve webpage
    class codPageData extends AsyncTask<String, Void, String>
    {
        org.jsoup.nodes.Document doc;
        Elements docElements;
        Elements titles;
        Elements descriptions;
        String content;
        Elements link;
        Elements authors;
        Elements dates;

        @Override
        protected String doInBackground(String... arg0){
            try {
                doc = Jsoup.connect(callOfDutyURL)
                        .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                        .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(6000000)
                        .get();

                docElements = doc.getElementsByClass("content-object");
                content = docElements.text();


            }catch (IOException e){
                e.printStackTrace();
            }
            return "Executed";


        }
        protected void onPostExecute(String result)
        {
            try{

                mainList = new ArrayList<>();
                //Get all titles
                titles = doc.getElementsByClass("title");
                descriptions = doc.getElementsByClass("blurb-container");
                //Elements pictures = doc.getElementsByClass("dx-grid-thumb-container");
                authors = doc.getElementsByClass("author-name");
                dates = doc.getElementsByClass("publication");

                for(int i = 0; i< docElements.size(); i++) {
                    //Title
                    String title = titles.get(i).text();

                    //Description
                    //String description = descriptions.get(i).text(); Works but, not really needed yet

                    //Link
                    String linkForItem ="https://www.callofduty.com" + docElements.get(i).select("a").attr("href").toString();

                    String imageUrl="";
                    try{
                        String imageDec = docElements.select("img").get(i).attr("src");

                        //Means image is based on another site
                        if(imageDec.toLowerCase().contains("/content")){
                            imageUrl ="https://www.callofduty.com"+ imageDec;
                        }else{
                            imageUrl = imageDec;
                        }
                    }catch (Exception e){
                        imageUrl="";
                    }

                    //Published
                    String pub = dates.get(i).text();

                    //Author
                    String author="";
                    try{
                        author = authors.get(i).text();
                    }catch(Exception e){

                    }

                    //Add to mainlist
                    //(String newsHeading, String datePublished, String author, String link, String img) {
                    mainList.add(new NewsItem(title, pub, author, linkForItem, imageUrl));
                }

                //Add all to listView
                addItemsToList();

            }catch(Exception e){
                e.printStackTrace();
            }


        }
        protected void onPreExecute(String res){

        }
    }

    //Asycn task to retrieve webpage
    class gamursPageData extends AsyncTask<String, Void, String>
    {
        org.jsoup.nodes.Document doc;
        Elements docTitles;
        Elements docLinksTop;//Latest articles
        Elements docLinksBottom;//Articles below
        Elements docAuthors;
        Elements docDates;
        Elements docImages;
        String content;
        Elements link;

        @Override
        protected String doInBackground(String... arg0){
            try {
                doc = Jsoup.connect(gamursURL)
                        .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                        .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(6000000)
                        .get();

                //Remove unwanted HTML /articles/author
                doc.getElementsByClass("ArticleWidget-content--hot").remove();
                doc.getElementsByClass("Articles-featured").remove();
                doc.getElementsByClass("infoAuthor--carousel").remove();

                //Assign data elements
                docTitles = doc.getElementsByClass("ArticleCard-infoTitle");
                docLinksTop = doc.getElementsByClass("ArticleCard-infoReadmore");
                docLinksBottom = doc.select("div.ArticleCard.ArticleCard--normal.u-cf");
                docAuthors = doc.getElementsByClass("ArticleCard-infoAuthor");
                docDates = doc.getElementsByClass("ArticleCard-infoTime");
                docImages = doc.getElementsByClass("ArticleCard-previewImage");

            }catch (IOException e){
                e.printStackTrace();
            }
            return "Executed";

        }
        protected void onPostExecute(String result)
        {
            //Item attritbues
            String title;
            String linkForItem;
            String datePublished;
            String author;
            String imageUrl;

            try{

                for(int i = 0; i< docTitles.size(); i++) {
                    //Title
                    title = docTitles.get(i).text();

                    //Link --- yup, HTML sucks
                    String link;
                    if(i<=2){
                        link = "https://gamurs.com"+docLinksTop.select("a").get(i).attr("href").toString();
                    }else{
                        link = "https://gamurs.com"+docLinksBottom.get(i-3).select("a").attr("href").toString();
                    }

                    author = docAuthors.get(i).text();
                    datePublished = docDates.get(i).text();
                    imageUrl=docImages.get(i).attr("style");
                    imageUrl = imageUrl.substring(imageUrl.indexOf("(")+1, imageUrl.lastIndexOf(")"));//get proper image url


                    //Add item to mainList array
                    mainList.add(new NewsItem(title, datePublished, author, link, imageUrl));
                }

                //Add all to listView
                addItemsToList();

            }catch (Exception e){

            }


        }
        protected void onPreExecute(String res){

        }
    }
}
