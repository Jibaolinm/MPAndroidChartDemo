package com.petterp.guosai.GuosaiTest.Zhizhutu;

import android.graphics.Canvas;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @author Petterp on 2019/5/7
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class XAxisRendererRandarCharts extends XAxisRenderer {
    private RadarChart mChart;

    public XAxisRendererRandarCharts(ViewPortHandler viewPortHandler, XAxis xAxis, RadarChart chart) {
        super(viewPortHandler, xAxis, (Transformer)null);
        this.mChart = chart;
    }

    public void renderAxisLabels(Canvas c) {
        if (this.mXAxis.isEnabled() && this.mXAxis.isDrawLabelsEnabled()) {
            float labelRotationAngleDegrees = this.mXAxis.getLabelRotationAngle();
            MPPointF drawLabelAnchor = MPPointF.getInstance(0.5F, 0.25F);
            this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
            this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
            float sliceangle = this.mChart.getSliceAngle();
            float factor = this.mChart.getFactor();
            MPPointF center = this.mChart.getCenterOffsets();
            MPPointF pOut = MPPointF.getInstance(0.0F, 0.0F);

            for(int i = 0; i < ((IRadarDataSet)((RadarData)this.mChart.getData()).getMaxEntryCountSet()).getEntryCount(); ++i) {
                //这里做个变化，更改颜色
                this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());
                String label = this.mXAxis.getValueFormatter().getFormattedValue((float)i, this.mXAxis);
                float angle = (sliceangle * (float)i + this.mChart.getRotationAngle()) % 360.0F;
                Utils.getPosition(center, this.mChart.getYRange() * factor + (float)this.mXAxis.mLabelRotatedWidth / 2.0F, angle, pOut);
                this.drawLabel(c, label, pOut.x, pOut.y - (float)this.mXAxis.mLabelRotatedHeight / 2.0F, drawLabelAnchor, labelRotationAngleDegrees);
            }

            MPPointF.recycleInstance(center);
            MPPointF.recycleInstance(pOut);
            MPPointF.recycleInstance(drawLabelAnchor);
        }
    }

    public void renderLimitLines(Canvas c) {
    }
}

