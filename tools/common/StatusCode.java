package com.doctoryun.common;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.doctoryun.activity.account.LoginActivity;
import com.doctoryun.application.MyApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adolf Li on 2015/12/11.
 */
public class StatusCode {

    public static void reLogin(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, LoginActivity.class);
        Preference.putString(Constant.PREFERENCE_TOKEN, null);
        Preference.putString(Constant.PREFERENCE_PHONE, null);
        Preference.putString(Constant.PREFERENCE_ID, null);
        Utils.resetPushCallbackToLogin(context);
        //取消所有推送
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();

        context.startActivity(intent);
    }

    public static boolean login(String statusStr){
        Map<String,String> map = new HashMap<>();
        map.put("USER_NOT_FOUND","没找到用户");
        map.put("ACCOUNT_OR_PWD_ERROR","密码或账号不正确");
        map.put("SIGN_VALID_FAIL","签名验证错误");
        if (!statusStr.equals("SUCCESS")){
            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    public static boolean verifyCode(String statusStr){
        Map<String,String> map = new HashMap<>();
        map.put("FAIL","验证码发送失败");
        if (!statusStr.equals("SUCCESS")){
            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    public static boolean regist(String statusStr){
        Map<String,String> map = new HashMap<>();
        map.put("FAIL","注册失败");
        map.put("USER_EXIST","注册失败，用户已存在");
        if (!statusStr.equals("SUCCESS")){
            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    public static boolean getNews(String statusStr){
        Map<String,String> map = new HashMap<>();
        map.put("USER_NOT_FOUND","没找到用户");
        map.put(" SIGN_VALID_FAIL","签名验证错误");
        if (!statusStr.equals("SUCCESS")){
            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public static boolean getUserInfo(String statusStr){
        Map<String,String> map = new HashMap<>();
        map.put("USER_NOT_FOUND","没找到用户");
        map.put(" SIGN_VALID_FAIL","签名验证错误");
        if (!statusStr.equals("SUCCESS")){
            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public static boolean isCodeValid(String statusStr) {
        Map<String,String> map = new HashMap<>();
        map.put("FAIL","短信验证码验证失败");
        map.put("USER_NOT_EXIST","合法验证失败，用户不存在");
        if (!statusStr.equals("SUCCESS")){
            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public static boolean resetPwd(String statusStr) {
        Map<String,String> map = new HashMap<>();
        map.put("FAIL","重置失败");
        if (!statusStr.equals("SUCCESS")){
            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    public static boolean getTemplate(String statusStr) {
        Map<String,String> map = new HashMap<>();
        map.put(" SIGN_VALID_FAIL","签名验证错误");
        map.put("WPTEMPLATE_NOT_EXIST","模板不存在");
        map.put("WPPLAN_NOT_EXIST","计划不存在");
        if (!statusStr.equals("SUCCESS")){
//            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public static boolean getSchedule(String statusStr) {
        Map<String,String> map = new HashMap<>();
        if (!statusStr.equals("SUCCESS")){
            Toast.makeText(MyApplication.getInstance(),map.get(statusStr),Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    public static boolean process(String statusStr) {
        Map<String,String> map = new HashMap<>();
        map.put("FAIL","执行失败");
        map.put(" SIGN_VALID_FAIL","签名验证错误");
        map.put("EXECUTEDATE_NULL","日程时间不能为空");
        if (!statusStr.equals("SUCCESS")){
            String str = map.get(statusStr);
            if (str == null){
                str = statusStr;
            }
            Toast.makeText(MyApplication.getInstance(),str,Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
}
