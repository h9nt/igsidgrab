package com.vardxg.instatest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;
import android.app.ProgressDialog;

// if u see this here, then fuck, u and if u sell this app then u are fckin scammer.
// be sure to join https://t.me/vardxg

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    OkHttpClient client;
    String insta_login = "https://i.instagram.com/api/v1/accounts/login/";
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msec.sec(MainActivity.this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in..");
        progressDialog.setCancelable(false);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        client = new OkHttpClient.Builder().build();

        textView = findViewById(R.id.sessionidresult);
        Button buttonlog = findViewById(R.id.loginBtn);

        textView.setOnClickListener(v -> {
            String sessionid = textView.getText().toString();
            String sessionid2 = extractSSID(sessionid);
            if (sessionid2 != null && !sessionid2.isEmpty()) {
                copy2Clipboard(sessionid2);
                Toast.makeText(MainActivity.this, "Copied sesssionid", Toast.LENGTH_SHORT).show();
            }
        });

        buttonlog.setOnClickListener(v -> {
            String username = ((EditText) findViewById(R.id.username)).getText().toString();
            String password = ((EditText) findViewById(R.id.password)).getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                AppAlert("IG Sessionid Grabber 1.0", "Username or password cannot be empty");
            } else {
                try {
                    doLogin(username, password);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }


    public void doLogin(String username, String password) {
        try {
            progressDialog.show();
            String uuid = UUID.randomUUID().toString();

            RequestBody requestBody = new FormBody.Builder()
                    .add("uuid", uuid)
                    .add("password", password)
                    .add("username", username)
                    .add("device_id", "8a993c4a-f7c4-46ff-b0f8-288c00a1850c")
                    .add("from_req", "false")
                    .add("_csrftoken", "missing")
                    .add("login_attempt_countn", "0")
                    .build();

            Request request = new Request.Builder()
                    .url(insta_login)
                    .post(requestBody)
                    .addHeader("User-Agent", "Instagram 113.0.0.39.122 Android (24/5.0; 515dpi; 1440x2416; huawei/google; Nexus 6P; angler; angler; en_US)")
                    .addHeader("X-IG-Capabilities", "3brTvw==")
                    .addHeader("X-IG-Connection-Type", "WIFI")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Host", "i.instagram.com")
                    .addHeader("Connection", "keep-alive")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                    Log.e("MyApp", "onFailure: " + e.getMessage());
                    runOnUiThread(() -> progressDialog.dismiss());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        try {
                            String jsonResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(jsonResponse);

                            if (jsonObject.has("logged_in_user")) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("IG Sessionid Grabber 1.0")
                                        .setMessage("Logged in as: " + username)
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .create().show();
                                HttpUrl url = response.request().url();
                                List<okhttp3.Cookie> cookies = okhttp3.Cookie.parseAll(url, response.headers());
                                String sessionId = findSessionId(cookies);
                                textView.setText("Sessionid: " + sessionId);
                            } else {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("IG Sessionid Grabber 1.0")
                                        .setMessage("Login failed, Bad credentials")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        })
                                        .create().show();
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    private void copy2Clipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("",text);
        clipboard.setPrimaryClip(clip);
    }

    private String findSessionId(List<okhttp3.Cookie> cookies) {
        for (okhttp3.Cookie cookie : cookies) {
            if ("sessionid".equals(cookie.name())) {
                return cookie.value();
            }
        }
        return null;
    }

    public String extractSSID(String sessid) {
        String[] parts = sessid.split(":");
        if (parts.length == 2) {
            return parts[1].trim();
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.telegram) {
            telegram();
        } else if(item.getItemId() == R.id.update) {
            update();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return  true;
    }

    public void telegram() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/vardxg"));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        Toast.makeText(MainActivity.this, "Soon..", Toast.LENGTH_SHORT).show();
    }

    private void AppAlert(String title, String message) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create().show();
    }

}