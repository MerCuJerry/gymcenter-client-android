package com.my.gymcenter.utils;

import android.app.Application;
import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;

import net.gotev.cookiestore.SharedPreferencesCookieStore;
import net.gotev.cookiestore.okhttp.JavaNetCookieJar;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class GymApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CookieManager cookieManager = new CookieManager(
                new SharedPreferencesCookieStore(getApplicationContext(), "MyCookies"),
                CookiePolicy.ACCEPT_ALL
        );
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
        OkHttpUtils.initClient(okHttpClient);
        Log.i("GymAPP", "okHttp initialized");
    }
}
