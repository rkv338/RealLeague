package com.example.realleague;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.champion_mastery.methods.GetChampionMasteryScoresBySummoner;
import net.rithms.riot.api.endpoints.league.dto.LeagueList;
import net.rithms.riot.api.endpoints.league.methods.GetChallengerLeagueByQueue;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

import java.util.List;

public class GetStats extends AppCompatActivity {

    EditText summoner;
    Spinner region;
    Button button;
    TextView txt;
    String summonerName;
    ApiConfig config = new ApiConfig().setKey(API.TOKEN);
    RiotApi riot = new RiotApi(config);
    Platform pf;




    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_stats);


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        region = (Spinner) findViewById(R.id.spinner2);
        summoner = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        txt = (TextView) findViewById(R.id.textView);



        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(GetStats.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.regions));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        region.setAdapter(adapter);



        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                summonerName = summoner.getText().toString();
                int optionPicked = region.getSelectedItemPosition();
                Summoner thisSum = null;
                if (optionPicked == 0 || summonerName == "") {
                    txt.setText("Type in your summoner name and/or pick a region.");
                }
                else {
                    FetchSummonerTask f = new FetchSummonerTask();
                    if (optionPicked == 1) {
                        f.execute(summonerName , "na");
                        pf = Platform.NA;
                    }
                    else if (optionPicked == 2) {
                        f.execute(summonerName , "kr");
                        pf = Platform.KR;
                    }
                    else if (optionPicked == 3) {
                        f.execute(summonerName , "euw");
                        pf = Platform.EUW;
                    }
                }

            }
        });
    }
    public class FetchSummonerTask extends AsyncTask<String, Void, Summoner> {

        @Override
        protected Summoner doInBackground(String... params) {
            try {
                Summoner thisSum = riot.getSummonerByName(givePlatform(params[1]), params[0]);
                return thisSum;
            } catch (RiotApiException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Summoner result) {
            try {
                String sumStats = "";
                sumStats += result.getName() + "\n" + "Summoner Level: " + result.getSummonerLevel();
                List ll = riot.getLeagueBySummonerId(pf, result.getId());
                String queue ="" +  ll.get(0).getClass();

                txt.setText(queue);
            } catch(RiotApiException e) {
                e.printStackTrace();
            }
        }
    }

    public Platform givePlatform (String region) {
        switch (region) {
            case "na" : return Platform.NA;
            case "euw" : return Platform.EUW;
            case "kr" : return Platform.KR;
        }
        return Platform.NA;
    }
}


