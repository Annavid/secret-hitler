package com.example.secrethitleronline;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button login_button;
    Button register_button;
    EditText username;
    EditText password;
    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        primarySettings();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                
                if (isEmpty(username))
                    username.setError("Username is empty");
                else if (isEmpty(password))
                    password.setError("Password is empty");
                else {
                    try {
                        sendLoginRequest();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }});


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(username))
                    username.setError("Username is empty");
                else if (isEmpty(password))
                    password.setError("Password is empty");
                else
                {
                    try {
                        sendRegisterRequest();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showRegisterError() {
        Toast.makeText(this, R.string.RegisterError, Toast.LENGTH_LONG).show();
    }

    private void sendRegisterRequest() throws IOException {
        sendRequest(getResources().getString(R.string.localhost) + NetTools.getRegisterURL(), username.getText().toString(), password.getText().toString(), true);
    }

    private void showLoginError() {
        Toast.makeText(this, R.string.loginError, Toast.LENGTH_LONG).show();
    }

    private void sendLoginRequest() throws IOException {
        sendRequest(getResources().getString(R.string.localhost) + NetTools.getLoginURL(), username.getText().toString(), password.getText().toString(), false);
    }

    public void nextActivity(){
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("token", response);
        startActivity(intent);
        finish();
    }

    private void primarySettings() {
        login_button = findViewById(R.id.button4);
        register_button = findViewById(R.id.button5);
        username = findViewById(R.id.editText4);
        password = findViewById(R.id.editText5);
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() <= 0;
    }

    void sendRequest(final String url, final String usernameText, final String passwordText, final boolean state) throws IOException {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", usernameText);
            jsonBody.put("password", passwordText);

            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonBody,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                if (state){
                                    response = jsonObject.getString("username");
                                    if (!response.equals(usernameText))
                                        showRegisterError();
                                    else
                                        sendRequest(getResources().getString(R.string.localhost) + NetTools.getLoginURL(), usernameText, passwordText, false);
                                }
                                else {
                                    response = jsonObject.getString("token");
                                    if (!response.isEmpty())
                                        nextActivity();
                                    else
                                        showLoginError();
                                }
                            } catch (JSONException | IOException e) {
                                if (state)
                                    showRegisterError();
                                else
                                    showLoginError();
                            }}
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (state)
                        showRegisterError();
                    else
                        showLoginError();
                }
            });

            myRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(myRequest);
        } catch (JSONException e) {
            if (state)
                showRegisterError();
            else
                showLoginError();
        }
    }

}
