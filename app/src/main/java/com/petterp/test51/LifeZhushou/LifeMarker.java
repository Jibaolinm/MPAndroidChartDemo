package com.petterp.test51.LifeZhushou;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.petterp.test51.R;

import java.text.DecimalFormat;

/**
 * @author Petterp on 2019/5/4
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class LifeMarker extends MarkerView {
    private TextView textView;
    private DecimalFormat decimalFormat=new DecimalFormat("###");
    public LifeMarker(Context context) {
        super(context, R.layout.life_marker);
        textView=findViewById(R.id.text);
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        textView.setText(decimalFormat.format(e.getY()));
    }
}
