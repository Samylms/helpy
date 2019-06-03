package ij.personal.helpy;

import android.content.Context;

public class Prefs {

    public static int getStudentId(Context context){
        return context.getSharedPreferences("UserInfo", 0).getInt("studentId", 0);
    }

    public static int getClassId(Context context){
        return context.getSharedPreferences("UserInfo", 0).getInt("classId", 0);
    }

    public static String getClassName(Context context){
        return context.getSharedPreferences("UserInfo", 0).getString("className", "");
    }

    public static String getStudentLastName(Context context){
        return context.getSharedPreferences("UserInfo", 0).getString("studentLastName", "");
    }

    public static String getStudentFirstName(Context context){
        return context.getSharedPreferences("UserInfo", 0).getString("studentFirstName", "");
    }

    public static String getStudentEmail(Context context){
        return context.getSharedPreferences("UserInfo", 0).getString("studentEmail", "");
    }

    public static String getStudentPwd(Context context){
        return context.getSharedPreferences("UserInfo", 0).getString("studentPwd", "");
    }

    public static int getStudentPhone(Context context){
        return context.getSharedPreferences("UserInfo", 0).getInt("studentPhone", 0);
    }

    public static boolean isStudentConnected(Context context){
        return context.getSharedPreferences("UserInfo", 0).getBoolean("isStudentConnected", false);
    }

    public static boolean isServerOK(Context context){
        return context.getSharedPreferences("UserInfo", 0).getBoolean("isServerOK", false);
    }
}
