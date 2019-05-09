package com.petterp.guosai;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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
import static android.content.Context.MODE_PRIVATE;

/**
 * Post请求工具类，惰性单例
 */
@SuppressLint("Registered")
public class PostUtils {
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final MediaType JSON = MediaType.parse("application/json");
    private final ExecutorService service = Executors.newSingleThreadExecutor();
    /**
     * MyApplication为一个全局Context类,在程序启动时已被初始化，在这里调用方法即可
     */
    private final SharedPreferences data = MyApplication.getContext().getSharedPreferences("IP", MODE_PRIVATE);

    public interface Post {
        void success(String s);
    }

   public static PostUtils Builder() {
        return Client.postUtils;
    }

   public void setOkHttpClient(String url, String neirong, Post post) {
        service.execute(() -> {
            RequestBody body = RequestBody.create(JSON, neirong);
            Request request = new Request
                    .Builder()
                    .url("http://" + data.getString("IP", "192.168.1.101") + ":8088/transportservice/action/" + url)
                    .post(body)
                    .build();
            try {
                Response execute = okHttpClient.newCall(request).execute();
                assert execute.body() != null;
                String string = execute.body().string();
                handler.post(() -> post.success(string));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static class Client {
        private static PostUtils postUtils = new PostUtils();
    }
}
