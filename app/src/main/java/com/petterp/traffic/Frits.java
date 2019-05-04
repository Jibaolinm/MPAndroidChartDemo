package com.petterp.traffic;


import com.petterp.test51.R;

/**
 * @author Petterp on 2019/5/2
 * Summary:Bean类，处理数据set,get
 * 邮箱：1509492795@qq.com
 */
public class Frits {
    public int id;      //路口id
    public int red;     //默认配置红，绿,黄
    public int gre;
    public int yel;

    public int stateAred;      //左侧时间变化
    public int stateAgre;
    public int stateAyel;

    public int stateBred;      //右侧时间变化
    public int stateBgre;
    public int stateByel;

    private int modeA=5,colorA= R.drawable.green;       //当前所处时间，及相应的颜色配置，有默认数据
    private int modeB=5,colorB=R.drawable.red;          //后期通过set可以更改

    public Frits(int id, int red, int gre, int yel) {
        this.id = id;
        this.red = red;
        this.gre = gre;
        this.yel = yel;
        setTime();
    }
    private void setTime(){
        this.stateAred=red;
        this.stateAgre=gre;
        this.stateAyel=yel;
        this.stateBred=red;
        this.stateBgre=gre;
        this.stateByel=yel;
    }

    public int getModeA() {
        return modeA;
    }

    public void setModeA(int modeA) {
        this.modeA = modeA;
    }

    public int getColorA() {
        return colorA;
    }

    public void setColorA(int colorA) {
        this.colorA = colorA;
    }

    public int getModeB() {
        return modeB;
    }

    public void setModeB(int modeB) {
        this.modeB = modeB;
    }

    public int getColorB() {
        return colorB;
    }

    public void setColorB(int colorB) {
        this.colorB = colorB;
    }
}
