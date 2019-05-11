package com.petterp.guosai.GuosaiTest.DingzhiBanChe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Okhttp {
    OkHttpClient okHttpClient;
    MediaType JSON=MediaType.parse("application/json");
    Handler handler=new Handler(Looper.getMainLooper());
    public  interface   Post{
        void  on(String s);
    }
    public  interface  Image{
        void  on(Bitmap s);
    }
    ExecutorService service=Executors.newSingleThreadExecutor();
    public  void  setOkHttpClient(String url, String neirong, Post post){
        service.execute(new Runnable() {
            @Override
            public void run() {
                okHttpClient=new OkHttpClient();
                RequestBody body=RequestBody.create(JSON,neirong);
                Request request=new Request
                        .Builder()
                        .url("http://192.168.1.105:8088/transportservice/"+url)
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
        });
    }
    public  void  setOkHttpClient_tu(String url, Image post){
        service.execute(new Runnable() {
            @Override
            public void run() {
                okHttpClient=new OkHttpClient();
                Request request=new Request
                        .Builder()
                        .url("http://192.168.1.105:8088/transportservice/"+url)
                        .get()
                        .build();
                try {
                    Response execute = okHttpClient.newCall(request).execute();
                    byte[] bytes = execute.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            post.on(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
