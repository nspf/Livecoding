package com.example.paul.livecoding.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paul.livecoding.BuildConfig;
import com.example.paul.livecoding.endpoints.TokenRefresh;
import com.example.paul.livecoding.pojo.RefreshAccessToken;
import com.example.paul.livecoding.R;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements Callback<RefreshAccessToken> {

    String parsedCode;
    String grant_type = "refresh_token";
    String redirect_uri = "http://localhost";
    String onresponse;
    public static String code = "";
    private static String RANDOM_STATE = "state=random_state_string";
    Intent liveStreamsIntent;
    Button auth;
    SharedPreferences pref;
    TextView Access;
    private WebView webView;
    private String OAUTH_URL = "https://www.livecoding.tv/o/authorize/?client_id=" + BuildConfig.CLIENT_ID + "&response_type=code&" + RANDOM_STATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        this.setTitle(getResources().getString(R.string.title_activity_login));

        pref = getSharedPreferences("access_code", MODE_PRIVATE);
        Access = (TextView) findViewById(R.id.Access);

        String stored_code = pref.getString("code", code);
        if (!TextUtils.isEmpty(stored_code)) {

            liveStreamsIntent = new Intent(LoginActivity.this,
                    LiveStreamsOnAirA.class);

            liveStreamsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(liveStreamsIntent);
            Log.e("code", code);

        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.authorize), Toast.LENGTH_SHORT).show();
        }

        auth = (Button) findViewById(R.id.auth);
        auth.setOnClickListener(new View.OnClickListener() {

            Dialog auth_dialog;

            @Override
            public void onClick(View arg0) {

                auth_dialog = new Dialog(LoginActivity.this);
                auth_dialog.setContentView(R.layout.auth_dialog);
                auth_dialog.show();
                auth_dialog.setCancelable(true);

                webView = (WebView) auth_dialog.findViewById(R.id.webv);
                webView.getSettings().setJavaScriptEnabled(true);

                Log.e("oauthurl", OAUTH_URL);

                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        if (url.contains("http://localhost")) {

                            Uri uri = Uri.parse(url);
                            parsedCode = uri.getQueryParameter("code");
                            getNewAccessToken();

                            Log.e("url", url);
                            Log.e("parsedcode", parsedCode);
                        }
                        return false;
                    }
                });

                webView.loadUrl(OAUTH_URL);
            }
        });
    }

    public void getNewAccessToken() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.livecoding.tv/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        TokenRefresh tokenRefresh = retrofit.create(TokenRefresh.class);

        Call<RefreshAccessToken> call = tokenRefresh.getNewAccessToken(parsedCode, BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET, redirect_uri, grant_type);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<RefreshAccessToken> call, Response<RefreshAccessToken> response) {

        RefreshAccessToken refreshAccessToken;

        refreshAccessToken = response.body();

        int code = response.code();

        Log.e("reponse", response.raw().toString());

        Log.e("getAcessToken()", refreshAccessToken.getAccessToken());
        Log.e("getTokenType()", refreshAccessToken.getTokenType());
        Log.e("getExpiry()", String.valueOf(refreshAccessToken.getExpiry()));
        Log.e("getRefreshToken()", refreshAccessToken.getRefreshToken());
        Log.e("getScope)()", refreshAccessToken.getScope());
        Log.e("getClientID", refreshAccessToken.getClientID());
        Log.e("getClientSecret()", refreshAccessToken.getClientSecret());

        if (code == 200) {
            Toast.makeText(this, getResources().getString(R.string.ready), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_connection_made), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<RefreshAccessToken> call, Throwable t) {
        t.printStackTrace();

        Toast.makeText(this, getResources().getString(R.string.failed), Toast.LENGTH_SHORT).show();
    }
}















