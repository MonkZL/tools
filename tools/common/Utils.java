
package com.doctoryun.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.PushService;
import com.doctoryun.R;
import com.doctoryun.activity.account.LoginActivity;
import com.doctoryun.activity.account.WelcomeActivity;
import com.doctoryun.application.MyApplication;
import com.doctoryun.bean.PatientsInfo;
import com.doctoryun.bean.RequestInfo;
import com.doctoryun.bean.TemplateListInfo;
import com.doctoryun.service.AESCoder;
import com.doctoryun.view.camera.utils.TimeUtils;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;
import org.xmlpull.v1.XmlPullParser;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/*
/common function
Created by Adolf Li
 */
public class Utils {


    public static String millisecsToDateString(long timestamp) {
        long gap = System.currentTimeMillis() - timestamp;
        if (gap < 1000 * 60 * 60 * 24) {
            String s = (new PrettyTime()).format(new Date(timestamp));
            return s;
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
            return format.format(new Date(timestamp));
        }
    }


    /**
     * 根据手机屏幕将dp转换成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void sortData(List<PatientsInfo> data1) {
        Collections.sort(data1, new Comparator<PatientsInfo>() {
            @Override
            public int compare(PatientsInfo lhs, PatientsInfo rhs) {
                if (lhs.getId() != null && !lhs.getId().contentEquals("")
                        && rhs.getId() != null && !rhs.getId().contentEquals("")) {
                    if (Integer.parseInt(lhs.getId()) > Integer.parseInt(rhs.getId()))
                        return -1;
                    else if (Integer.parseInt(lhs.getId()) < Integer.parseInt(rhs.getId()))
                        return 1;
                    else
                        return 0;

                } else
                    return 0;
            }
        });
    }

    public static void sortData2(List<RequestInfo.DataEntity> data1) {
        Collections.sort(data1, new Comparator<RequestInfo.DataEntity>() {
            @Override
            public int compare(RequestInfo.DataEntity lhs, RequestInfo.DataEntity rhs) {
                if (lhs.getTime() != null && !lhs.getTime().contentEquals("")
                        && rhs.getTime() != null && !rhs.getTime().contentEquals("")) {
                    if (TimeUtils.parseToLong(lhs.getTime()) > TimeUtils.parseToLong(rhs.getTime()))
                        return -1;
                    else if (TimeUtils.parseToLong(lhs.getTime()) < TimeUtils.parseToLong(rhs.getTime()))
                        return 1;
                    else
                        return 0;
                } else
                    return 0;
            }
        });
    }

    public static CharSequence getTimeOfCircle(Context context, String date) {
        long l = TimeUtils.parseToLong(date);
//        if (l/(60 * 24 ?))
        return android.text.format.DateUtils.getRelativeTimeSpanString(context, l);
    }


    public static void resetPushCallbackToWelcome(Context context) {
        PushService.setDefaultPushCallback(context, WelcomeActivity.class);
        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.subscribe(context, "public", WelcomeActivity.class);
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }


    public static int getNumByDay() {
        int i = Integer.parseInt(getCurrentTime().substring(8, 10));
        return i % 3;
    }

    public static String getTimeByMine(Date date) {
        String time = getTime(date);
        return " " + time.substring(11, 16);
    }


    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
        }
    }
//
//    public static void cancleUrlbyTag(String[] strings) {
//        if (strings != null) {
//            for (String string : strings) {
//                VolleyQueueController.instance.cancelPendingRequests(string);
//            }
//        }
//    }

    public static void showShare(Context context, String url, String title, Bitmap enableLogo, View.OnClickListener listener) {
        saveImage(context, R.drawable.icon_share_login);
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
// 分享时Notification的图标和文字
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        if (title != null && !title.contentEquals(""))
            oks.setTitle(title);
        else
            oks.setTitle(getString(R.string.app_name));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("" + url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(context.getFilesDir().getAbsolutePath() + "/image");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

//        Bitmap enableLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.all_patients);
        if (enableLogo != null && listener != null) {
            String label = "患者";
            oks.setCustomerLogo(enableLogo, label, listener);
        }
        oks.show(context);
    }

    public static String getHosId(String hosId) {
        if (hosId == null)
            return "0000";

        hosId = "0000" + hosId;
        String substring = hosId.substring(hosId.length() - 4);
        return substring;
    }

    public static void showShare(Context context, String url, String title, Bitmap enableLogo, View.OnClickListener listener,
                                 Bitmap enableLogo1, View.OnClickListener listener1) {
        if (AuthorityHelper.getInstatnce().isQualifyed()) {
            saveImage(context, R.drawable.icon_share_login);
            ShareSDK.initSDK(context);
            OnekeyShare oks = new OnekeyShare();
            //关闭sso授权
            oks.disableSSOWhenAuthorize();
// 分享时Notification的图标和文字
            //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
            // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
            if (title != null && !title.contentEquals(""))
                oks.setTitle(title);
            else
                oks.setTitle(getString(R.string.app_name));
            // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
            oks.setTitleUrl(url);
            // text是分享文本，所有平台都需要这个字段
            oks.setText("" + url);
            // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
            oks.setImagePath(context.getFilesDir().getAbsolutePath() + "/image");//确保SDcard下面存在此张图片
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(url);
            // comment是我对这条分享的评论，仅在人人网和QQ空间使用
            oks.setComment("我是测试评论文本");
            // site是分享此内容的网站名称，仅在QQ空间使用
            oks.setSite(getString(R.string.app_name));
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(url);

//        Bitmap enableLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.all_patients);
            if (enableLogo != null && listener != null) {
                String label = "患者";
                oks.setCustomerLogo(enableLogo, label, listener);
            }

            if (enableLogo1 != null && listener1 != null) {
                String label = "我的主页";
                oks.setCustomerLogo(enableLogo1, label, listener1);
            }
// 启动分享GUI
            oks.show(context);
        } else {
            Toast.makeText(context, "您还未通过系统认证", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public static void paixu(List<TemplateListInfo.DataEntity> data) {
        Collections.sort(data, new Comparator<TemplateListInfo.DataEntity>() {
            @Override
            public int compare(TemplateListInfo.DataEntity lhs, TemplateListInfo.DataEntity rhs) {
                if (lhs.getId() != null && !lhs.getId().contentEquals("") && rhs.getId() != null && !rhs.getId().contentEquals("")) {
                    if (Integer.parseInt(lhs.getId()) > Integer.parseInt(rhs.getId())) {
                        return -1;
                    } else if (Integer.parseInt(lhs.getId()) < Integer.parseInt(rhs.getId())) {
                        return 1;
                    } else {
                        return 0;
                    }
                } else {
                    return 0;
                }
            }
        });
    }

    /**
     * res目录下面的一张图片保存到本地
     *
     * @param id 图片的id
     */
    public static void saveImage(Context context, int id) {
        // getFilesDir().getAbsolutePath()+"/image"\
        //在本地创建一个文件夹
        File file = new File(context.getFilesDir().getAbsolutePath() + "/logodoc");
        // File absoluteFile = getFilesDir().getAbsoluteFile();
        //判断本地是否存在，防止每次启动App都要创建
        if (file.exists()) {
            return;
        }
        //使用BitmapFactory把res下的图片转换成Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
        FileOutputStream fos = null;
        try {
            //获得一个可写的输入流
            fos = context.openFileOutput("image", Context.MODE_PRIVATE);
            //使用图片压缩对图片进行处理  压缩的格式  可以是JPEG、PNG、WEBP
            //第二个参数是图片的压缩比例，第三个参数是写入流
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String decryptToken(String data) {
        try {
            byte[] aeskey = Hex.decodeHex(AESCoder.aesK.toCharArray());
            String keys = new String(aeskey);
            byte[] toDecrypt = Hex.decodeHex(data.toCharArray());
            byte[] decryptData = AESCoder.decrypt(toDecrypt, aeskey);
            String aesOut = new String(decryptData);
            return aesOut;
        } catch (Exception e) {
            Log.e("", "yunduo--->" + e);
            return "";
        }
    }

    public static String agendaSign(String agendaId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Constant.PARAM_USERID, Preference.getString(Constant.PREFERENCE_ID));
        paramsMap.put(Constant.PARAM_SCHEDULE_ID, agendaId);
        return getSignFromMap(paramsMap);
    }

    public static String agendaSign() {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Constant.PARAM_USERID, Preference.getString(Constant.PREFERENCE_ID));
        return getSignFromMap(paramsMap);
    }

    public static String workSign(String type) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Constant.PARAM_USERID, Preference.getString(Constant.PREFERENCE_ID));
        paramsMap.put(Constant.PARAM_TYPE, type);
        return getSignFromMap(paramsMap);
    }

    public static String sumSign(String pId) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put(Constant.PARAM_USERID, Preference.getString(Constant.PREFERENCE_ID));
        paramsMap.put(Constant.PARAM_PID, pId);
        return getSignFromMap(paramsMap);
    }

    /**
     * bytes字符串转换为Byte值
     *
     * @return byte[]
     */
    public static byte[] hexStr2Bytes(String src) {
        int m = 0, n = 0;
        int l = src.length() / 2;
        System.out.println(l);
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            m = i * 2 + 1;
            n = m + 1;
            //ret[i] = Byte.decode("0x" + src.substring(i*2, m) + src.substring(m,n));
            ret[i] = (byte) (Integer.parseInt(src.substring(i * 2, m) + src.substring(m, n), 16));
        }
        return ret;
    }

    /**
     * bytes转换成十六进制字符串
     *
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
        }
        return sb.toString().toUpperCase().trim();
    }

    public static String getDeviceId(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、177、170（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static String getSignFromMap(Map<String, String> paramsMap) {
        Collection<String> keyset = paramsMap.keySet();
        List<String> list = new ArrayList<String>(keyset);
        //对key键值按字典升序排序
        Collections.sort(list);
        String mStr = "";
        for (int i = 0; i < list.size(); i++) {
            mStr += list.get(i) + "=" + paramsMap.get(list.get(i));
        }
        String inToken = Preference.getString(Constant.PREFERENCE_TOKEN);
        mStr += (decryptToken(inToken));
        return getMD5(mStr);
    }

    public static String getSignFromJson(JSONObject jsonObject) throws JSONException {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("treeId", jsonObject.getString("treeId"));
        paramsMap.put("userId", jsonObject.getString("userId"));
        return getSignFromMap(paramsMap);
    }

    static public int getScreenWidthPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    static public int dipToPx(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    public static int clearCacheFolder(File dir, long numDays) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }
                    if (child.lastModified() < numDays) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    static public float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

    public static void resetPushCallbackToLogin(Context context) {
        PushService.setDefaultPushCallback(context, LoginActivity.class);
        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.subscribe(context, "public", LoginActivity.class);
    }

    public static String getString(int resID) {
        return MyApplication.getInstance().getString(resID);
    }

    public static String getMD5(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(str.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String s = Integer.toHexString(0xff & bytes[i]);

                if (s.length() == 1) {
                    sb.append("0" + s);
                } else {
                    sb.append(s);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5 failed");
        }

    }

    public static String getSign(Map<String, String> paramsMap) {
        Collection<String> keyset = paramsMap.keySet();
        List<String> list = new ArrayList<String>(keyset);
        //对key键值按字典升序排序
        Collections.sort(list);
        String mStr = "";
        for (int i = 0; i < list.size(); i++) {
            mStr += list.get(i) + "=" + paramsMap.get(list.get(i));
        }
        mStr += (Preference.getString(Constant.PREFERENCE_TOKEN));
        return getMD5(mStr);
    }

    public static Map<String, String> getHashMapResource(Context c, int hashMapResId) {
        Map<String, String> map = null;
        XmlResourceParser parser = c.getResources().getXml(hashMapResId);

        String key = null, value = null;

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("utils", "Start document");
                } else if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("map")) {
                        boolean isLinked = parser.getAttributeBooleanValue(null, "linked", false);

                        map = isLinked ? new LinkedHashMap<String, String>() : new HashMap<String, String>();
                    } else if (parser.getName().equals("entry")) {
                        key = parser.getAttributeValue(null, "key");

                        if (null == key) {
                            parser.close();
                            return null;
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("entry")) {
                        map.put(key, value);
                        key = null;
                        value = null;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    if (null != key) {
                        value = parser.getText();
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return map;
    }

    public static String getCurrentTime() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /*
     *timestamp change
     * type = 0 format yyyy-MM-dd
     * type = 1 format MM/dd
     */
    public static String timeStampToDate(String timeStamp, int type) {
        if (timeStamp == null) {
            return "";
        }
        long time = Long.parseLong(timeStamp);
        String date = null;
        switch (type) {
            case 0:
                date = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
                break;
            case 1:
                date = new java.text.SimpleDateFormat("MM/dd HH:mm").format(new Date(time));
                break;
            case 2:
                date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
                break;
            default:
                break;
        }
        Long timestamp = Long.parseLong(timeStamp) * 1000;

        return date;
    }

    public static boolean isValidIP(String ipStr) {
        boolean result = false;
        String s = "\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\:\\d{1,5}\\b";
        Pattern pattern = Pattern.compile(s);
        Matcher matcher = pattern.matcher(ipStr);
        boolean base = matcher.matches();
        if (base) {
            String portStr = ipStr.substring(ipStr.indexOf(":") + 1);
            int port = Integer.parseInt(portStr);
            if (port < 65535) {
                result = true;
            }
        }
        return result;
    }

    /*
     *timestamp change
     */
    public static String dateToTimeStamp(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dat = format.parse(date);
        return dat.getTime() + "";
    }

    /*
     * number format
     *
     * */
    public static String decimalForamt(String decimalStr) {
        double num = 0;
        if (decimalStr != null) {
            num = Double.parseDouble(decimalStr);
            String result = new DecimalFormat("0.00").format(num);
            return result;
        } else {
            return "";
        }
    }

    public static String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static String getCurrentVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.versionName;
        }
        return "0";
    }

    public static int getCurrentVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            return info.versionCode;
        }
        return 0;
    }

    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return apiKey;
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 把数据源HashMap转换成json
     *
     * @param map
     */
    public static String hashMapToJson(HashMap map) {
        String string = "{";
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            string += "\"" + e.getKey() + "\":";
            String temp = (String) map.get(e.getKey());
            if (temp.indexOf("[") == 0) {
                string += e.getValue() + ",";
            } else {
                string += "\"" + e.getValue() + "\",";
            }
        }
        string = string.substring(0, string.lastIndexOf(","));
        string += "}";
        return string;
    }

    public static int getAge(String birthday) {
        int age = 0;
        long now = System.currentTimeMillis();
        try {
            String birStr = dateToTimeStamp(birthday);
            long bir = Long.parseLong(birStr);
            age = (int) ((now - bir) / 1000 / 60 / 60 / 24 / 365);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }

    public static String join(String[] strs, String seperator) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            str.append(strs[i]);
            if (i != (strs.length - 1)) {
                str.append(seperator);
            }
        }
        return str.toString();
    }


    static public List<String> getHostoryUser() {
        List<String> strList = Preference.getStrList(Constant.PREFERENCE_LIST_HOSTORY_USER);
        return strList;
    }

    static public void putHostoryUser(String str) {
        List<String> strList = Preference.getStrList(Constant.PREFERENCE_LIST_HOSTORY_USER);
        for (int i = 0; i < strList.size(); i++) {
            if (strList.get(i).contentEquals(str)) {
                strList.remove(i);
            }
        }
        strList.add(0, str);
        if (strList.size() >= 5) {
            for (int i = 5; i < strList.size(); i++) {
                strList.remove(i);
            }
        }
        Preference.putStrList(strList, Constant.PREFERENCE_LIST_HOSTORY_USER);
    }


}
