package com.project.opticproject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class cwlinfo extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private String seasonOneURL = "http://www.codupdate.com/cwl-north-america-standings-call-duty-world-league-season-1/";
    private String seasonTwoURL = "http://www.codupdate.com/cwl-standings-season-2-north-america/";

    TextView txt;

    private ArrayList<TableData> tableData;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cwlinfo);

        //Title bar
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(" Seasons Standings ");
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        //Dropdown selector
        Spinner staticSpinner = (Spinner) findViewById(R.id.spinner2);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.seasons,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        //staticSpinner.setBackgroundColor(Color.WHITE);
        staticSpinner.setAdapter(staticAdapter);
        staticSpinner.setOnItemSelectedListener (this);


        txt = (TextView) findViewById(R.id.txtSeason);
        tableData = new ArrayList<>();
        //new seasonTwoData().execute();
    }

    public void createTable()
    {
        tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        LayoutInflater inflaterTop = getLayoutInflater();
        View rowView1 = inflaterTop.inflate(R.layout.my_row, null);


        for (int i = 0; i < tableData.size(); i++)
        {
            TableRow tableRow = new TableRow(this);

//            LayoutInflater inflater = LayoutInflater.from(cwlinfo.this);
//            tableRow = (TableRow) inflater.inflate(R.layout.my_row, tableLayout, false);

            LayoutInflater inflater = getLayoutInflater();
            View rowView = inflater.inflate(R.layout.my_row, null);


            //Rank
            TextView rank =(TextView) rowView.findViewById(R.id.txtRank);
            rank.setText(tableData.get(i).getRank());

            //Team
            TextView team =(TextView) rowView.findViewById(R.id.txtTeam);
            team.setText(tableData.get(i).getTeam());

            //wins
            TextView wins =(TextView) rowView.findViewById(R.id.txtWins);
            wins.setText(tableData.get(i).getWins());

            //losses
            TextView losses =(TextView) rowView.findViewById(R.id.txtLosses);
            losses.setText(tableData.get(i).getLosses());

            //winPerc
            TextView winPerc =(TextView) rowView.findViewById(R.id.txtWinPerc);
            winPerc.setText(tableData.get(i).getWinPerc());


            tableLayout.setBackgroundColor(Color.WHITE);
            tableLayout.addView(rowView);
        }
        //setContentView(tableLayout);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = (String) parent.getItemAtPosition(position);
        if(selectedItem.equals("Season 1")){
            tableData = new ArrayList<>();//Empty current mainList
            if(tableLayout!=null){
                tableLayout.removeAllViews();
                tableLayout.invalidate();
                tableLayout.refreshDrawableState();
                tableLayout = null;
                txt.setText("");
                try{
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }

            new seasonOneData().execute();//
        }
        else if(selectedItem.equals("Season 2")){
            tableData = new ArrayList<>();
            if(tableLayout!=null){
                tableLayout.removeAllViews();
                tableLayout.invalidate();
                tableLayout.refreshDrawableState();
                tableLayout = null;
                txt.setText("");

                try{
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            new seasonTwoData().execute();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Asycn task to retrieve webpage
    class seasonTwoData extends AsyncTask<String, Void, String>
    {
        org.jsoup.nodes.Document doc;
        Elements docElements;
        String content;
        Elements link;
        Elements rows;

        @Override
        protected String doInBackground(String... arg0){
            try {
                doc = Jsoup.connect(seasonTwoURL)
                        .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                        .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(6000000)
                        .get();

                Elements containingDiv = doc.getElementsByClass("entry-content");
                Elements table = containingDiv.select("table");
                rows = table.select("tr");



            }catch (IOException e){
                e.printStackTrace();
            }
            return "Executed";

        }
        protected void onPostExecute(String result)
        {

            for (Element row : rows) {
                String rank;
                String team;
                String wins;
                String losses;
                String winPerc;

                rank= row.child(0).text();
                team= row.child(1).text();
                wins= row.child(2).text();
                losses= row.child(3).text();
                winPerc= row.child(4).text();

                System.out.println("Rank: "+row.child(0).text()+", Team: "+row.child(1).text()
                        +", Wins: "+row.child(2).text() +", Losses: "+row.child(3).text());
                // LOG.i("label: "+row.child(0).text()+", value:"+row.child(1).text());


                tableData.add(new TableData(rank, team, wins, losses, winPerc));
            }


            try{
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            }catch (Exception e)
            {
                e.printStackTrace();
            }
            txt.setText(" Season 2 ");
            txt.setGravity(Gravity.CENTER);
            createTable();
        }
        protected void onPreExecute(String res){

        }
    }

    class seasonOneData extends AsyncTask<String, Void, String>
    {
        org.jsoup.nodes.Document doc;
        Elements docElements;
        String content;
        Elements link;
        Elements rows;

        @Override
        protected String doInBackground(String... arg0){
            try {
                doc = Jsoup.connect(seasonOneURL)
                        .header("Accept-Language", "pt-BR,pt;q=0.8") // missing
                        .header("Accept-Encoding", "gzip,deflate,sdch") // missing
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                        .maxBodySize(0)
                        .timeout(6000000)
                        .get();

                Elements containingDiv = doc.getElementsByClass("entry-content");
                Elements table = containingDiv.select("table");
                rows = table.select("tr");



            }catch (IOException e){
                e.printStackTrace();
            }
            return "Executed";

        }
        protected void onPostExecute(String result)
        {

            for (Element row : rows) {
                String rank;
                String team;
                String wins;
                String losses;
                String winPerc;

                rank= row.child(0).text();
                team= row.child(1).text();
                wins= row.child(2).text();
                losses= row.child(3).text();
                winPerc= row.child(4).text();

                System.out.println("Rank: "+row.child(0).text()+", Team: "+row.child(1).text()
                        +", Wins: "+row.child(2).text() +", Losses: "+row.child(3).text());
                // LOG.i("label: "+row.child(0).text()+", value:"+row.child(1).text());

                try{
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                txt.setText(" Season 1 ");
                txt.setGravity(Gravity.CENTER);
                tableData.add(new TableData(rank, team, wins, losses, winPerc));
            }


            createTable();
        }
        protected void onPreExecute(String res){

        }
    }
}
