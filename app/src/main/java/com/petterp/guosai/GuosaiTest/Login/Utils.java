package com.petterp.guosai.GuosaiTest.Login;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.petterp.guosai.MyApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Petterp on 2019/5/9
 * Summary:
 * 邮箱：1509492795@qq.com
 */
public class Utils {
    public final static SharedPreferences.Editor shar=MyApplication.getContext().getSharedPreferences("login",Context.MODE_PRIVATE).edit();
    public final static SharedPreferences data=MyApplication.getContext().getSharedPreferences("login",Context.MODE_PRIVATE);

    public static void setList(List<Frits> list){
        Gson gson=new Gson();
        shar.putString("login",gson.toJson(list)).apply();
    }
    public static List<Frits> getList(){
        List<Frits> list=new ArrayList<>();
        String res=data.getString("login","");
        if (res.equals("")){
            return list;
        }
        Gson gson=new Gson();
        list=gson.fromJson(res,new TypeToken<Frits>(){}.getType());
        return list;
    }

    public static boolean getMode(String name,String pswd){
        List<Frits> list=Utils.getList();
        int size=list.size();
        for (int i=0;i<size;i++){
            if (list.get(i).name.equals(name)&list.get(i).pswd.equals(pswd)){
               return true;
            }
        }
        return false;
    }
    public static boolean getRegison(String name){
        List<Frits> list=Utils.getList();
        int size=list.size();
        for (int i=0;i<size;i++){
            if (list.get(i).name.equals(name)){
                return true;
            }
        }
        return false;
    }
    public static String getPswd(String name,String pswd){
        List<Frits> list=Utils.getList();
        int size=list.size();
        for (int i=0;i<size;i++){
            if (list.get(i).name.equals(name)&list.get(i).pswd.equals(pswd)){
                return list.get(i).pswd;
            }
        }
        return "";
    }
}
