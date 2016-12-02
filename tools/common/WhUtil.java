package com.doctoryun.common;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by admin on 2016/10/9.
 */
public class WhUtil {


    /**
     * 获取屏幕宽度
     *
     * @param context
     */
    public static int getPopW(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }


    /**
     * 获取屏幕高度
     *
     * @param context
     */
    public static int getPopH(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }


}
