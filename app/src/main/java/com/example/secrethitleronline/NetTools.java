package com.example.secrethitleronline;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

class NetTools {

    static String getLoginURL() {
        return "http://139c03950db5.ngrok.io/api/v1/users/obtain-token/";
    }

    static String getRegisterURL() {
        return "http://139c03950db5.ngrok.io/api/v1/users/create/";
    }
}
