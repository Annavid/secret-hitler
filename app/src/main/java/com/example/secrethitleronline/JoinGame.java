package com.example.secrethitleronline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JoinGame extends AppCompatActivity {

    String token;
    ArrayList<Game> games = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join_game);
        token = getIntent().getStringExtra("token");
        String json = getIntent().getStringExtra("game_list");
        ObjectMapper mapper = new ObjectMapper();
        assert json != null;
        try {
            games = mapper.readValue(json, new TypeReference<ArrayList<Game>>() {
                @Override
                public Type getType() {
                    return super.getType();
                }
            });
        } catch (JsonProcessingException e) {
            showJoinError();
        }

        ArrayList<String> urls = new ArrayList<>();
        ArrayList<String> counts = new ArrayList<>();
        for (Game game: games) {
            urls.add(game.getJoin_url());
            counts.add(game.getPlayers_to_start());}


        CustomListAdapter adapter = new CustomListAdapter(this, urls, counts);
        ListView listView = findViewById(R.id.lv);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //TODO: send web socket request with urls[positon]
            }
        });

    }

    private void showJoinError() {
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
    }

}
