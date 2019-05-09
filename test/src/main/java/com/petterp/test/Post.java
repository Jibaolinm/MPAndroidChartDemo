package com.petterp.test;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    ExecutorService fool=Executors.newFixedThreadPool(1);
    public static Post budler() {
        return Client.post;
    }
    public void setPost(final String url, final String json, final post p) {
        fool.execute(() -> {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            try {
                final String res = new OkHttpClient().newCall(request).execute().body().string();
                handler.post(() -> p.Ok(res));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });



      /*  ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        singleThreadPool.execute(()-> System.out.println(Thread.currentThread().getName()));
        singleThreadPool.shutdown();*/
    }

    private static class Client {
        private static Post post = new Post();
    }

}
