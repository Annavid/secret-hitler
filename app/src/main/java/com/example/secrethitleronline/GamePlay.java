package com.example.secrethitleronline;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

public class GamePlay extends AppCompatActivity {

    private WebSocketClient webSocketClient;
    String token;
    String username;
    ProgressBar progressBar;
    TextView textView;
    ArrayList<String> users = new ArrayList<>();
    ArrayList<TextView> usernames = new ArrayList<>();
    ArrayList<ImageView> roles = new ArrayList<>();
    ImageView liberalBoard;
    ImageView fascistBoard;
    TextView drawPile;
    TextView discardPile;
    TextView president;
    TextView chancellor;
    TextView gameInstructor;
    TextView electionTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        token = getIntent().getStringExtra("token");
        username = LoginActivity.getUser();
        connectWebSocket();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        textView = findViewById(R.id.textView4);
    }

    private void callThisMethodWhenAllPlayersJoined() throws JSONException {
        setContentView(R.layout.main_game_board);
        MenuActivity.getMusic().stop();
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("header", "token");
        jsonBody.put("token", token);
        webSocketClient.send(jsonBody.toString());
    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI(getResources().getString(R.string.ws_host) + NetTools.getWebSocketFirstAddress() + getIntent().getStringExtra("uri") + "/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            String header = Objects.requireNonNull(jsonObject).getString("header");
                            handleWSMessage(header, jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        webSocketClient.connect();
    }

    private void handleWSMessage(String header, JSONObject jsonObject) throws JSONException {
        switch (header){

            case "game_start":
                callThisMethodWhenAllPlayersJoined();
                break;
            case "players":
                gameInitiation(jsonObject);
                break;
            case "president":
                president.setText(jsonObject.getString("username"));
                break;
            case "role":
                handleRoles(jsonObject);
        }
    }

    private void handleRoles(JSONObject jsonObject) throws JSONException {
        if (jsonObject.getString("role").equals("liberal")){
            for (int i=0; i<users.size(); i++){
                if (username.equals(users.get(i))){
                    usernames.get(i).setTextColor(Color.parseColor("#76d2e1"));
                    roles.get(i).setImageResource(R.drawable.lib_role);}

            }
        }
        if (jsonObject.getString("role").equals("fascist")){
            for (int i=0; i<users.size(); i++){
                if (username.equals(users.get(i)) || jsonObject.getString("yar").equals(users.get(i))){
                    usernames.get(i).setTextColor(Color.parseColor("#e9954f"));
                    if (jsonObject.getString("secret").equals("hitler"))
                        roles.get(i).setImageResource(R.drawable.hitty_role);
                    else
                        roles.get(i).setImageResource(R.drawable.fasc_role);
                }
            }
        }
    }

    private void gameInitiation(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("username");
        initialSettings();
        for (int i=0;i< jsonArray.length();i++){
            users.add(jsonArray.get(i).toString());
            usernames.get(i).setText(jsonArray.get(i).toString());
        }

    }

    private void initialSettings() {
        String temp;
        usernames.add((TextView) findViewById(R.id.user1));
        usernames.add((TextView) findViewById(R.id.user2));
        usernames.add((TextView) findViewById(R.id.user3));
        usernames.add((TextView) findViewById(R.id.user4));
        usernames.add((TextView) findViewById(R.id.user5));
        roles.add((ImageView) findViewById(R.id.player1));
        roles.add((ImageView) findViewById(R.id.player2));
        roles.add((ImageView) findViewById(R.id.player3));
        roles.add((ImageView) findViewById(R.id.player4));
        roles.add((ImageView) findViewById(R.id.player5));
        liberalBoard = findViewById(R.id.boardLib);
        fascistBoard = findViewById(R.id.boardFasc);
        discardPile = findViewById(R.id.discardPile);
        temp = getResources().getString(R.string.discard_pile) + " 0";
        discardPile.setText(temp);
        drawPile = findViewById(R.id.drawPile);
        temp = getResources().getString(R.string.draw_pile) + " 18";
        drawPile.setText(temp);
        president = findViewById(R.id.president);
        chancellor = findViewById(R.id.chanc);
        gameInstructor = findViewById(R.id.messages);
        electionTracker = findViewById(R.id.textView14);
    }

}
