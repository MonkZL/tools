package com.doctoryun.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by admin on 2016/3/3.
 */
public class VpImgUtil {

//
//    public static List<Bitmap> getBits(List<ImageInfo> urls){
//        List<Bitmap> bitmaps = new ArrayList<>();
//        for (int i = 0 ; i < urls.size() ; i++){
//            bitmaps.add(getBitmap(urls.get(i).getImage_path()));
//        }
//        return bitmaps;
//    }

    public static void getBitmap(String url, ImageView view){
        try{
            if(url==null || url.isEmpty())
                return;

            URL urlConnection = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            view.setImageBitmap(myBitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return;
    }
}
