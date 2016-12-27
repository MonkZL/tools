package ysccc.com.tghp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import ysccc.com.tghp.R;

/**
 * Created by 张梁 on 2016/11/25.
 */
public class DialogLoading {

    private static ImageView img;
    private static Animation animation;
    private static View view;
    private static AlertDialog alertDialog;

    private static void setLoading(Context context) {

        DisplayMetrics out = new DisplayMetrics();
        view = ((View) LayoutInflater.from(context).inflate(R.layout.dialog_loading, null, false));
        img = (ImageView) view.findViewById(R.id.img);
        setAnimation();

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setView(view);

        alertDialog = builder.create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                animation.start();
            }
        });

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                animation.cancel();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private static void setAnimation() {
        if (animation == null) {
            animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(3000);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.RESTART);
        }
        img.setAnimation(animation);
    }

    public static void show(Context context) {
        setLoading(context);

        alertDialog.show();

        DisplayMetrics out = new DisplayMetrics();
        ((Activity) context).getWindow().getWindowManager().getDefaultDisplay().getMetrics(out);


        alertDialog.getWindow().setLayout(out.widthPixels / 2, out.widthPixels / 2);

    }

    public static void cancel() {
        alertDialog.cancel();
    }
}