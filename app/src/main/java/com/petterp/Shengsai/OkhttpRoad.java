package com.petterp.Shengsai;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * auther: Petterp on 2019/4/17
 * Summary:
 */
public class OkhttpRoad {
    OkHttpClient okHttpClient;
    Handler handler=new Handler(Looper.getMainLooper());
    MediaType JSON=MediaType.parse("application/json");
    private final SharedPreferences sharedPreference;

    public  interface  Post{
        void  on(String s);
    }
    Context context;

    public OkhttpRoad(Context context) {
        this.context = context;
        sharedPreference = context.getSharedPreferences("ip",0);
    }

    public  void  setOkHttpClient(String url, String neirong, Post post){
                okHttpClient=new OkHttpClient();
                RequestBody body=RequestBody.create(JSON,neirong);
                Request request=new Request
                        .Builder()
                        .url("http://192.168.1.113:8080/get?count="+url)
                        .post(body)
                        .build();
                try {
                    Response execute = okHttpClient.newCall(request).execute();
                    String string = execute.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            post.on(string);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

    }
}

