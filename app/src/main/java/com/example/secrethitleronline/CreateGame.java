package com.example.secrethitleronline;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class CreateGame extends AppCompatActivity {

    Button createButton;
    EditText playerCount;
    String joinURL;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_game);
        token = getIntent().getStringExtra("token");
        createButton = findViewById(R.id.button6);
        playerCount = findViewById(R.id.editText);
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(playerCount.getText().toString());
                if (count < 5 || count > 10)
                    playerCount.setError("invalid input");
                else {

                    JSONObject jsonBody = new JSONObject();
                    try {
                        jsonBody.put("player_count", playerCount.getText().toString());
                    } catch (JSONException e) {
                        playerCount.setError("invalid input");
                    }

                    JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST,
                            getResources().getString(R.string.localhost) + NetTools.getCreateGameURL(), jsonBody,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    try {
                                        joinURL = jsonObject.getString("join_url");
                                    } catch (JSONException e) {
                                        showError();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showError();
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
            }
        });

    }

    private void showError() {
        Toast.makeText(this, getResources().getString(R.string.create_game_error), Toast.LENGTH_LONG).show();
    }
}
