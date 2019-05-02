package com.petterp.test51;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author Petterp on 2019/5/1
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Post {
    @FunctionalInterface
    public interface post {
        void Ok(String res);
    }

    private Post() {

    }

    private Handler handler = new Handler(Looper.getMainLooper());

    public static Post budler() {
        return Client.post;
    }

    public void setPost(final String url, final String json, final post p) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                try {
                    final String res = new OkHttpClient().newCall(request).execute().body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            p.Ok(res);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static class Client {
        private static Post post = new Post();
    }

}
