package com.petterp.guosai.LifeZhushou;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petterp.guosai.R;

/**
 * @author Petterp on 2019/5/4
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class LifeTempFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.life_air_item, container, false);
        return view;
    }
}
