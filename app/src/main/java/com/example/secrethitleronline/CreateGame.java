package com.example.secrethitleronline;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class CreateGame extends AppCompatActivity {

    Button createButton;
    EditText playerCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_game);
        createButton = findViewById(R.id.button6);
        playerCount = findViewById(R.id.editText);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(playerCount.getText().toString());
                if (count < 5 || count > 10)
                    playerCount.setError("invalid input");
                else {
                    //TODO: create game
                }
            }
        });

    }
}
