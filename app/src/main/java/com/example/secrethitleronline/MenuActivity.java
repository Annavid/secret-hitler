package com.example.secrethitleronline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    Button create_game;
    Button join_game;
    Button exit;
    ImageView sound_button;
    static MediaPlayer music = new MediaPlayer();
    boolean music_sound = true;
    int length;
    String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        primarySettings();
        musicHandler();
    }


    private void musicHandler() {
        sound_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!music_sound){
                    music_sound = true;
                    sound_button.setImageResource(R.drawable.music_icon);
                    music.seekTo(length);
                    music.start();
                }
                else{
                    music_sound = false;
                    sound_button.setImageResource(R.drawable.mute_icon);
                    music.pause();
                    length = music.getCurrentPosition();
                }
            }
        });
    }

    private void primarySettings() {

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        create_game = findViewById(R.id.button);
        join_game = findViewById(R.id.button2);
        exit = findViewById(R.id.button3);
        sound_button = findViewById(R.id.imageView3);

        music = MediaPlayer.create(this, R.raw.sound);
        music.start();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

        create_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateGameActivity();
            }
        });

        join_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonArrayRequest myRequest = new JsonArrayRequest(Request.Method.GET,
                        getResources().getString(R.string.localhost) + NetTools.getAvailableGamesList(), null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray jsonObject) {
                                JoinGameActivity(jsonObject);
                                //showResponse(jsonObject.toString());
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showJoinError();
                        Log.d("errorws:", error.toString());
                    }
                })

                {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", "Token " + token);
                        return params;
                    }
                };


                myRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(myRequest);

            }
        });
    }

    private void showResponse(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }

    private void showJoinError() {
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
    }

    private void JoinGameActivity(JSONArray jsonObject) {
        Intent intent = new Intent(this, JoinGame.class);
        intent.putExtra("token", token);
        intent.putExtra("game_list",  jsonObject.toString());
        startActivity(intent);
    }

    private void CreateGameActivity() {
        Intent intent = new Intent(this, CreateGame.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    public static MediaPlayer getMusic(){
        return music;
    }

}
