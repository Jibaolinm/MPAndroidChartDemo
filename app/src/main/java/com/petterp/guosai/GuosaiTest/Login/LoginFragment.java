package com.petterp.guosai.GuosaiTest.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.petterp.guosai.PostUtils;
import com.petterp.guosai.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Petterp on 2019/5/9
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText edit_name;
    private EditText edit_pswd;
    private Button login_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_login, container, false);
        initView(view);
        TextView title=view.findViewById(R.id.title);
        title.setText("login");
        return view;
    }

    private void initView(View view) {
        edit_name = (EditText) view.findViewById(R.id.edit_name);
        edit_pswd = (EditText) view.findViewById(R.id.edit_pswd);
        login_btn = (Button) view.findViewById(R.id.login_btn);
        view.findViewById(R.id.login_zhaohui).setOnClickListener(this);
        view.findViewById(R.id.login_resign).setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                submit();
                break;
            case R.id.login_resign:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new ResignFragment()).commit();break;
            case R.id.login_zhaohui:
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new ZhaohuiFragment()).commit();break;
        }
    }

    private void submit() {
        // validate
        boolean mode = true;
        String name = edit_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "name不能为空", Toast.LENGTH_SHORT).show();
            mode = false;
        }
        String pswd = edit_pswd.getText().toString().trim();
        if (TextUtils.isEmpty(pswd)) {
            Toast.makeText(getContext(), "pswd不能为空", Toast.LENGTH_SHORT).show();
            mode = false;
        }
        if (mode) {
            PostUtils.Builder().setOkHttpClient("user_login.do", "{\"UserName\":\"" + name + "\",\"UserPwd\": \"" + pswd + "\"}", new PostUtils.Post() {
                @Override
                public void success(String s) {
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("RESULT").equals("S")) {
                            Toast.makeText(getContext(), "登陆成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (Utils.getMode(name,pswd)){
                            Toast.makeText(getContext(), "登录成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "登录失败，请检查您的输入", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }
}
