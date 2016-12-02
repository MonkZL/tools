package com.doctoryun.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/6/25.
 */
public class UploadPicUtil {
    public static final int SIZETYPE_SAMLL = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_BIG = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_ORIGIN = 3;//获取文件大小单位为KB的double值


    public static List<String> setPicSmall(List<String> filePaths, int type) {
        List<String> newPaths = new ArrayList<>();
        if (type == SIZETYPE_SAMLL) {
            for (String path : filePaths) {
                File scal = scal(Uri.fromFile(new File(path)), 50);
                newPaths.add(scal.getPath());
            }
        } else if (type == SIZETYPE_BIG) {
            for (String path : filePaths) {
                File scal = scal(Uri.fromFile(new File(path)), 80);
                newPaths.add(scal.getPath());
            }
        } else if (type == SIZETYPE_ORIGIN) {
            for (String path : filePaths) {
                File scal = scal(Uri.fromFile(new File(path)), -1);
                newPaths.add(scal.getPath());
            }
        }
        return newPaths;
    }

    public static String picType(String statusStr) {
        Map<String, String> map = new HashMap<>();
        map.put("jpg", "1");
        map.put("pdf", "2");
        map.put("bmp", "3");
        map.put("png", "4");
        map.put("gif", "5");
        map.put("jpeg", "6");
        map.put("ico", "7");
        String s = map.get(statusStr);
        if (s == null || s.contentEquals(""))
            return "20";
        else return s;
    }

    public static File scal(Uri fileUri, int ratio) {
        String path = fileUri.getPath();
        File outputFile = new File(path);
        if (ratio != -1) {
            long fileSize = outputFile.length();
            final long fileMaxSize = 200 * 1024;
            if (fileSize >= fileMaxSize) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                int height = options.outHeight;
                int width = options.outWidth;

                double scale = Math.sqrt((float) fileSize / fileMaxSize);
                options.outHeight = (int) (height / scale);
                options.outWidth = (int) (width / scale);
                options.inSampleSize = (int) (scale + 0.5);
                options.inJustDecodeBounds = false;

                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                outputFile = new File(createImageFile().getPath());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(outputFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, ratio, fos);
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                } else {
                    File tempFile = outputFile;
                    outputFile = new File(createImageFile().getPath());
                    copyFileUsingFileChannels(tempFile, outputFile);
                }

            } else {
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }
        } else if (ratio == -1) {
            File tempFile = outputFile;
            outputFile = new File(createImageFile().getPath());
            copyFileUsingFileChannels(tempFile, outputFile);
        }
        return outputFile;
    }

    public static Uri createImageFile() {
        // Create an image file name
        String imageFileName = PictureUtil.getCharacterAndNumber();
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg",
                    storageDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }

    public static void copyFileUsingFileChannels(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
