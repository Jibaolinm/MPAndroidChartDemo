package com.petterp.guosai.GuosaiTest.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.petterp.guosai.R;

/**
 * @author Petterp on 2019/5/9
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class ZhaohuiFragment extends Fragment implements View.OnClickListener {
    private EditText edit_name;
    private EditText edit_email;
    private Button login_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhaohui_fragment, container, false);
        initView(view);
        TextView title=view.findViewById(R.id.title);
        title.setText("找回密码");
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new LoginFragment()).commit();
            }
        });
        return view;
    }

    private void initView(View view) {
        edit_name = (EditText) view.findViewById(R.id.edit_name);
        edit_email = (EditText) view.findViewById(R.id.edit_email);
        login_btn = (Button) view.findViewById(R.id.login_btn);

        login_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        boolean mode=true;
        String name = edit_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
            mode=false;
        }

        String email = edit_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mode=false;
            Toast.makeText(getContext(), "请输入邮箱", Toast.LENGTH_SHORT).show();
        }

        if (mode){
            String pswd=Utils.getPswd(name,email);
            if (pswd.equals("")){
                Toast.makeText(getContext(), "输入有误", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "您的密码为"+pswd, Toast.LENGTH_SHORT).show();
            }
        }


        // TODO validate success, do something


    }
}
