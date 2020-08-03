package com.example.secrethitleronline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MenuActivity extends AppCompatActivity {

    Button create_game;
    Button join_game;
    Button exit;
    ImageView sound_button;
    MediaPlayer music = new MediaPlayer();
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
                JoinGameActivity();
            }
        });
    }

    private void JoinGameActivity() {
        Intent intent = new Intent(this, JoinGame.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    private void CreateGameActivity() {
        Intent intent = new Intent(this, CreateGame.class);
        intent.putExtra("token", token);
        startActivity(intent);
    }

}
