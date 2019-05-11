package com.petterp.guosai.GuosaiTest.DingzhiBanChe;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.petterp.guosai.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Petterp on 2019/5/10
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Dingzhi_F2 extends Fragment implements View.OnClickListener {
    private ImageView image;
    private TextView name;
    private TextView money;
    private TextView licheng;
    private Button button;
    private Okhttp okhttp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dingzhi_f2, container, false);
        initView(view);
        okhttp=new Okhttp();
        return view;
    }

    private void initView(View view) {
        image = (ImageView) view.findViewById(R.id.image);
        name = (TextView) view.findViewById(R.id.name);
        money = (TextView) view.findViewById(R.id.money);
        licheng = (TextView) view.findViewById(R.id.licheng);
        button = (Button) view.findViewById(R.id.button);
        name.setText(Frits.name);
        money.setText("票价：￥"+Frits.money);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:

                break;
        }
    }

    private void Post(){
        okhttp.setOkHttpClient("", "", new Okhttp.Post() {
            @Override
            public void on(String s) {
                try {
                    JSONObject jsonObject=new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                okhttp.setOkHttpClient_tu("", new Okhttp.Image() {
                    @Override
                    public void on(Bitmap s) {

                    }
                });
            }
        });

    }
}
