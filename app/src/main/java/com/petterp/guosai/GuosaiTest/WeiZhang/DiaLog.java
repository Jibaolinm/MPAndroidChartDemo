package com.petterp.guosai.GuosaiTest.WeiZhang;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.petterp.guosai.R;

/**
 * @author Petterp on 2019/5/5
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class DiaLog extends Dialog {
    public DiaLog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weizhang_date);
    }
}
