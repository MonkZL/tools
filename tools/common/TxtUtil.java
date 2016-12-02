package com.doctoryun.common;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by admin on 2016/10/9.
 */
public class TxtUtil {


    public static Spanned formatStringColor2(String orgStr1, String color1,
                                             String orgStr2, String color2) {
        String str = "<font color=\'" + color1 + "\'>" + orgStr1 + "</font>"
                + "<font color=\'" + color2 + "\'>" + orgStr2 + "</font>";
        return Html.fromHtml(str);
    }
    
}
