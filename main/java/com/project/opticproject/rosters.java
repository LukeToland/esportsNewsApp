package com.project.opticproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class rosters extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private String imagesNA="https://www.callofduty.com/esports/league/na/teams";
    private String imagesEU="https://www.callofduty.com/esports/league/eu/teams";
    private String imagesANZ="https://www.callofduty.com/esports/league/anz/teams";
    private String teamName;
    LinearLayout yourLayout;
    HorizontalScrollView sc;
    private ArrayList<ImageItem> imageList;
    private ArrayList<Player> teamPlayers;
    private ListView lstviewPlayers;
    private ArrayList<ImageView> images;

    PlayerListAdapter adapter;
    ArrayAdapter<CharSequence> staticAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rosters);

        //Title bar
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(" Team Rosters ");
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //Dropdown selector
        Spinner staticSpinner = (Spinner) findViewById(R.id.static_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.regions,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);
        staticSpinner.setPrompt("Select");
        staticSpinner.setOnItemSelectedListener(this);

        //Add items to horizontallistview
        yourLayout = (LinearLayout)findViewById(R.id.imageLayout);
        //Horizontal
        sc = (HorizontalScrollView) findViewById(R.id.scroller);

        //Images list
        imageList = new ArrayList<ImageItem>();
        teamPlayers = new ArrayList<Player>();
        images = new ArrayList<>();

        TextView team = (TextView) findViewById(R.id.teamname);
        team.setText("Cloud 9");

        //Players list
        lstviewPlayers = (ListView) findViewById(R.id.teamMembers);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //String linkTo = mainList.get(position).getLink();

        //String linkTo="http://www.dexerto.com/esports/cod/";

//        Toast.makeText(this, linkTo,
//                Toast.LENGTH_LONG).show();

//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(Uri.parse(linkTo));
//        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = (String) parent.getItemAtPosition(position);
        try{
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        if(selectedItem.equals("North America")){
            imageList = new ArrayList<>();//Empty current mainList
            images = new ArrayList<>();
            yourLayout.removeAllViews();
            //Execute asynctasks
            new getTeamImages().execute(imagesNA);//

            //Get team details
            teamPlayers = new ArrayList<>();
            TextView team = (TextView) findViewById(R.id.teamname);
            team.setText("Cloud 9");
            new getTeamDetails().execute("https://www.callofduty.com/esports/team/cloud9");
        }
        else if(selectedItem.equals("Europe")){
            imageList = new ArrayList<>();
            images = new ArrayList<>();
            new getTeamImages().execute(imagesEU);//
            yourLayout.removeAllViews();;

            teamPlayers = new ArrayList<>();
            TextView team = (TextView) findViewById(R.id.teamname);
            team.setText("Epsilon eSports");
            new getTeamDetails().execute("https://www.callofduty.com/esports/team/epsilon-esports");
        }
        else if(selectedItem.equals("Oceania")){
            imageList = new ArrayList<>();
            images = new ArrayList<>();
            yourLayout.removeAllViews();
            new getTeamImages().execute(imagesANZ);//

            teamPlayers = new ArrayList<>();
            TextView team = (TextView) findViewById(R.id.teamname);
            team.setText("Chiefs eSports Club");
            new getTeamDetails().execute("https://www.callofduty.com/esports/team/chiefs-esports-club");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addImages(){
        try{
            for (int i = 0; i < imageList.size(); i++) {

                final ImageView item = new ImageView(getApplicationContext());
                String url = imageList.get(i).getUrl();
                item.setId(i);
                item.setColorFilter(Color.argb(0, 0, 0, 0));
                item.setBackgroundColor(Color.WHITE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
                item.setLayoutParams(layoutParams);

                images.add(item);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String teamPage = imageList.get(item.getId()).getArticle();
                        teamName = imageList.get(item.getId()).getTeam();
                        TextView team = (TextView) findViewById(R.id.teamname);
                        team.setText(teamName);
                        teamPlayers = new ArrayList<Player>();

                        //Set all images back to normal
                        for(int j = 0; j<imageList.size(); j++){
                            images.get(j).setColorFilter(Color.argb(0, 0, 0, 0));
                        }
                        //item.setBackgroundColor(R.color.black);
                        item.setColorFilter(Color.argb(75,0,0,0));
                        try{
                            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

                        }catch (Exception e)
                        {}
                        new getTeamDetails().execute(teamPage);
                    }


                });

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(160, 160);
                params.setMargins(1, 1, 1, 1);

                item.setLayoutParams(params);

                //item.setBackgroundColor(Color.parseColor("#757D75"));
                //item.setBackgroundResource(R.mipmap.logo);

                Picasso.with(this)
                        .load(url)
                        .resize(160,160)
                        .into(item);
                yourLayout.addView(item);
            }
        }catch (Exception e){

        }
    }

    public void addItemsToList(){

        try{
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        }catch (Exception e)
        {

        }

        //Add data to list
        //ArrayAdapter<NewsItem> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.listfeed, R.id.Itemname, mainList);
        adapter=new PlayerListAdapter(rosters.this, teamPlayers);
        lstviewPlayers.setAdapter(adapter);
    }


    //Asycn task to retrieve webpage
    class getTeamImages extends AsyncTask<String, Void, String>
    {
        org.jsoup.nodes.Document doc;
        Elements docElements;
        String content;
        Elements link;
        Elements li;


        @Override
        protected String doInBackground(String... arg0){
            try {
                String url = arg0[0];
                doc = Jsoup.connect(url)
                        .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                        .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(6000000)
                        .get();

                Elements ul = doc.select("div.inner > ul");
                li = ul.select("li"); // select all li from ul

            }catch (IOException e){
                e.printStackTrace();
            }
            return "Executed";

        }
        protected void onPostExecute(String result)
        {
            String images;
            String team;
            String linkUrl;

            try{

                for(int i = 0; i< li.size(); i++) {

                    team = li.get(i).select("div.team-logo.ir").text();
                    images=li.get(i).select("div.team-logo.ir").attr("style");
                    images = images.substring(images.indexOf("'")+1, images.lastIndexOf("'"));//get proper image url
                    linkUrl = "https://www.callofduty.com"+li.get(i).select("a").attr("href").toString();

                    imageList.add(new ImageItem(images, team, linkUrl));
                }

                //Add all to listView
                addImages();

            }catch (Exception e){

            }

        }
        protected void onPreExecute(String res){

        }
    }

    //Asycn task to retrieve webpage
    class getTeamDetails extends AsyncTask<String, Void, String>
    {
        org.jsoup.nodes.Document doc;
        ArrayList<org.jsoup.nodes.Document> playerDoc;
        Elements docElements;
        String content;
        Elements link;
        Elements li;


        @Override
        protected String doInBackground(String... arg0){
            String teamPage = arg0[0];
            playerDoc = new ArrayList<>();


            try {
                doc = Jsoup.connect(teamPage)
                        .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                        .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(6000000)
                        .get();

                docElements = doc.select("div.inner > ul > li");

                for(int i = 0; i< docElements.size(); i++){
                    org.jsoup.nodes.Document newDoc;
                    String link = "https://www.callofduty.com"+docElements.get(i).select("a").attr("href").toString();

                    newDoc = Jsoup.connect(link)
                            .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                            .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                            .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                            .maxBodySize(0)
                            .timeout(6000000)
                            .get();

                    playerDoc.add(newDoc);

                }

            }catch (IOException e){
                e.printStackTrace();
            }
            return "Executed";

        }
        protected void onPostExecute(String result)
        {
            Element player;
            String name;
            String photo;

            try{
                //Get player attributes
                for(int j = 0; j< playerDoc.size(); j++) {

                    try{
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                    }catch (Exception e)
                    {

                    }

                    String nickname =  playerDoc.get(j).getElementsByClass("user-name").text();

                    String playerName =  playerDoc.get(j).getElementsByClass("real-name").text();

                    String fullName = playerName.replaceAll(" ", " '"+nickname+ "' ");

                    String age = playerDoc.get(j).select("div.player-bio-container > ul > li.bio-item.age > div.entry").text();

                    if(age.equals("")){
                        age = "Age: --";
                    }
                    else
                    {
                        age = "Age: "+ age;
                    }

                    String town = playerDoc.get(j).select("div.player-bio-container > ul > li.bio-item.hometown > div.entry").text();
                    if(town.equals("")){
                        town = "Home: --";
                    }
                    else
                    {
                        town = "Home: "+ town;
                    }

                    photo = playerDoc.get(j).select("div.portrait-image.ir").attr("style");;

                    photo = photo.substring(photo.indexOf("'")+1, photo.lastIndexOf("'"));//get proper image url
                    if(photo.equals("https://www.callofduty.com/content/dam/atvi/callofduty/esportal/players/cutouts/Aches 360px.png")){
                        photo = "https://www.callofduty.com/content/dam/atvi/callofduty/esportal/players/cutouts/Aches%20360px.png";
                    }
                    else
                    {

                    }
                    teamPlayers.add(new Player(fullName, age, town, photo));
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
