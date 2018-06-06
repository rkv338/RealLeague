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
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;

public class GetStats extends AppCompatActivity {

    EditText summoner;
    Spinner region;
    Button button;
    TextView txt;
    String summonerName;
    ApiConfig config = new ApiConfig().setKey(API.TOKEN);
    RiotApi riot = new RiotApi(config);



    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        region = (Spinner) findViewById(R.id.spinner);
        summoner = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button3);
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
                    if (optionPicked == 1) {
                        FetchSummonerTask f = new FetchSummonerTask();
                        f.execute(summonerName);
                    }
                }

            }
        });




    }
    public class FetchSummonerTask extends AsyncTask<String, Void, Summoner> {

        @Override
        protected Summoner doInBackground(String... params) {
            try {
                Summoner thisSum = riot.getSummonerByName(Platform.NA, params[0]);
                return thisSum;
            } catch (RiotApiException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Summoner result) {
            txt.setText("Summoner Id: " + result.getId());
        }


    }
}


