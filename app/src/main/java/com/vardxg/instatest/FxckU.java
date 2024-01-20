package com.vardxg.instatest;

import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FxckU {
    boolean msmsms222 = false;
    static OkHttpClient client = new OkHttpClient();

    public FxckU(Context context) {
        String androidid = getAndroidId(context);
        sm324234234kdikd(androidid);
    }

    public String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void sm324234234kdikd(String androidId) {
        try {
            Request request = new Request.Builder()
                    .url("https://pastebin.com/raw/uk5FAZDs")
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            if (responseBody.contains(androidId)) {
                                msmsms222 = true;
                            } else {
                                msmsms222 = false;
                            }
                        } else {
                            msmsms222 = false;
                        }
                    } catch (Exception d) {
                        d.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
