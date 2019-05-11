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

import com.petterp.guosai.PostUtils;
import com.petterp.guosai.R;

import java.util.List;

/**
 * @author Petterp on 2019/5/9
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class ResignFragment extends Fragment implements View.OnClickListener {
    private EditText edit_name;
    private EditText edit_email;
    private EditText edit_pswd;
    private Button regison_btn;
    private EditText edit_pswd2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.regison_fragment, container, false);
        initView(view);
        TextView title=view.findViewById(R.id.title);
        title.setText("注册");
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
        edit_pswd = (EditText) view.findViewById(R.id.edit_pswd);
        regison_btn = (Button) view.findViewById(R.id.regison_btn);

        regison_btn.setOnClickListener(this);
        edit_pswd2 = (EditText) view.findViewById(R.id.edit_pswd2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regison_btn:
                submit();
                break;
        }
    }

    private void submit() {
        // validate
        boolean mode = true;
        StringBuffer res = new StringBuffer();
        String name = edit_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            res.append("用户名不能为空\n");
            mode = false;

        } else if (name.length() < 4) {
            mode = false;
            res.append("用户名不能少于4位\n");
        }

        String email = edit_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            res.append("邮箱不能为空\n");
            mode = false;
        } else if (email.length() < 6) {
            mode = false;
            res.append("邮箱不能少于6位\n");
        }

        String pswd = edit_pswd.getText().toString().trim();
        String pswd2 = edit_pswd2.getText().toString().trim();

        if (TextUtils.isEmpty(pswd)) {
            mode = false;
            res.append("密码不能为空\n");
            mode = false;
        } else if (pswd.length() < 6) {
            mode = false;
            res.append("密码不能小于6位\n");
        } else if (!pswd2.equals(pswd)) {
            mode = false;
            res.append("两次密码不一致\n");
        }
        if (mode) {
            if (Utils.getRegison(name)){
                Toast.makeText(getContext(), "已存在此用户", Toast.LENGTH_SHORT).show();
                return;
            }else{
                List<Frits> list=Utils.getList();
                list.add(new Frits(name,email,pswd));
                Utils.setList(list);
                Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), res.substring(0, res.length() - 1), Toast.LENGTH_SHORT).show();
        }
    }
}
