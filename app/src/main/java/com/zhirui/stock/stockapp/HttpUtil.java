package com.zhirui.stock.stockapp;

/**
 * Created by super on 2017-01-12.
 */

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {

    public static void sendOkHttpRequest(final String address, final okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static String sendOkHttpRequestSync(final String address){
        OkHttpClient client = new OkHttpClient();
        Response response = null;

        Request request = new Request.Builder()
                .url(address)
                .build();
        try {
            response =  client.newCall(request).execute();
            String responseText = response.body().string();
            return responseText;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
