package ysccc.com.tghp.views;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by 69567 on 2017/5/2.
 * 解决android 7.0 popWindow位置不对的问题
 */

public class PopWindowCompat extends PopupWindow {


    public PopWindowCompat(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    public PopWindowCompat(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    public PopWindowCompat(Context context) {
        super(context);
    }

    public PopWindowCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopWindowCompat(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PopWindowCompat(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PopWindowCompat() {
    }

    public PopWindowCompat(View contentView) {
        super(contentView);
    }

    public PopWindowCompat(int width, int height) {
        super(width, height);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT >= 24){
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }

        super.showAsDropDown(anchor);
    }
}
