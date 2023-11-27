package com.my.gymcenter.utils;

import com.google.gson.Gson;
import com.my.gymcenter.entity.ResponseData;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.Type;

import okhttp3.Response;

public abstract class ResponseCallback<T> extends Callback<ResponseData<T>>
{
    @Override
    public ResponseData<T> parseNetworkResponse(Response response, int id) throws Exception{
        String string = null;
        if (response.body() != null) {
            string = response.body().string();
        }
        return new Gson().fromJson(string, this.getType());
    }
    public abstract Type getType();
}