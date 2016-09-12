package com.project.opticproject;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

public class menu extends Activity {


    private Button btnNews;
    private Button btnRosters;
    private Button btnStandings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Set buttons
        btnNews = (Button) findViewById(R.id.btnNews);
        btnNews.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i=new Intent(menu.this,MainActivity.class);
                startActivity(i);
            }
        });

        btnRosters = (Button) findViewById(R.id.btnRosters);
        btnRosters.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i=new Intent(menu.this,rosters.class);
                startActivity(i);
            }
        });

        btnStandings = (Button) findViewById(R.id.btnStandings);
        btnStandings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i=new Intent(menu.this,cwlinfo.class);
                startActivity(i);
            }
        });
    }

}
