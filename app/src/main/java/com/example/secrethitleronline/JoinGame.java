package com.example.secrethitleronline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

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

        try {
            JSONArray array = new JSONArray(json);
            for (int i=0;i< array.length();i++){
                games.add((mapper.readValue(array.get(i).toString(), Game.class)));
            }
        } catch (JSONException e) {
            showJoinError();
        } catch (JsonMappingException ignored) {

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        final ArrayList<String> urls = new ArrayList<>();
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
                startGame(urls.get(position));
            }
        });

    }

    private void startGame(String uri) {
        Intent intent = new Intent(this, GamePlay.class);
        intent.putExtra("uri", uri);
        intent.putExtra("token", token);
        startActivity(intent);
        finish();
    }

    private void showJoinError() {
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
    }

}
