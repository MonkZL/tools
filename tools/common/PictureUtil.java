package com.doctoryun.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.doctoryun.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 疯尘丶
 * @Description 调用系统拍照或进入图库中选择照片, 再进行裁剪, 压缩.
 */
public class PictureUtil {
    /**
     * 用来请求照相功能的常量
     */
    public static final int CAMERA_WITH_DATA = 168;
    /**
     * 用来请求图片选择器的常量
     */
    public static final int PHOTO_PICKED_WITH_DATA = CAMERA_WITH_DATA + 1;
    /**
     * 用来请求图片裁剪的
     */
    public static final int PHOTO_CROP = PHOTO_PICKED_WITH_DATA + 1;
    /**
     * 拍照的照片存储位置
     */
    public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/");

    private static File mCurrentPhotoFile;// 照相机拍照得到的图片

    public File file;        // 截图后得到的图片
    public static Uri imageUri;    // 拍照后的图片路径
    public static Uri imageCaiUri;    // 裁剪后的图片路径


    public static Transformation transformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = Preference.getInt(Constant.PARAM_SCREEN_WIDTH) / 6;
            if (source.getWidth() == 0) {
                return source;
            }

            //如果图片小于设置的宽度，则返回原图
            if (source.getWidth() < targetWidth) {
                return source;
            } else {
                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                if (targetHeight != 0 && targetWidth != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            }
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };


//
//    /**
//     * 显示大图片
//     *
//     * @param context
//     * @param entity
//     * @param mPhotoView
//     */
//    public static void setNetBigPic(final Context context, final ImagesEntity entity, final PhotoView mPhotoView, final LinearLayout ll) {
//        if (entity.getImage_size() != null && !entity.getImage_size().contentEquals("")) {
//            if (Long.parseLong(entity.getImage_size()) / 1024 > 500) {
//                ll.setVisibility(View.VISIBLE);
//                Picasso.with(context).load(getThumbPic2(entity.getPath())).
//                        memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(R.drawable.icon_loading_net)
//                        .error(R.drawable.icon_fail_net).transform(transformation).into(mPhotoView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                    }
//
//                    @Override
//                    public void onError() {
//                        ll.setVisibility(View.GONE);
//                        Picasso.with(context).load(entity.getPath()).
//                                memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(R.drawable.icon_loading_net)
//                                .error(R.drawable.icon_fail_net).transform(transformation).into(mPhotoView, new Callback() {
//                            @Override
//                            public void onSuccess() {
//
//                            }
//
//                            @Override
//                            public void onError() {
//                                Toast.makeText(context, "服务器繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//            } else {
//                ll.setVisibility(View.GONE);
//                Picasso.with(context).load(entity.getPath()).
//                        memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(R.drawable.icon_loading_net)
//                        .error(R.drawable.icon_fail_net).transform(transformation).into(mPhotoView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        Toast.makeText(context, "服务器繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        } else {
//            ll.setVisibility(View.VISIBLE);
//            Picasso.with(context).load(entity.getPath()).
//                    memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(R.drawable.icon_loading_net)
//                    .error(R.drawable.icon_fail_net).transform(transformation).into(mPhotoView, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onError() {
//                    Toast.makeText(context, "服务器繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

    private static Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

//
//    /**
//     * 显示原图
//     *
//     * @param context
//     * @param entity
//     * @param mPhotoView
//     */
//    public static void setNetOriPic(final Context context, final ImagesEntity entity, final PhotoView mPhotoView) {
//        Picasso.with(context).load(entity.getPath()).
//                memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).placeholder(R.drawable.icon_loading_net)
//                .error(R.drawable.icon_fail_net).transform(transformation).into(mPhotoView, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError() {
//                Toast.makeText(context, "服务器繁忙，请稍后重试", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    /**
     * 显示小图片
     *
     * @param context
     * @param url
     * @param mPhotoView
     */
    public static void setNetSmaPic(final Context context, final String url, final PhotoView mPhotoView) {
        Picasso.with(context).load(getThumbPic(url)).placeholder(R.drawable.icon_loading_net)
                .error(R.drawable.icon_fail_small).into(mPhotoView, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(context).load(url).resize(120, 144).placeholder(R.drawable.icon_loading_net)
                        .error(R.drawable.icon_fail_small).into(mPhotoView);
            }
        });
    }

    /**
     * 获取缩略图名字
     *
     * @param url
     * @return
     */
    private static String getThumbPic(String url) {
        String[] split = url.split("\\.");
        String str = "";
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 2)
                str = str + split[i] + "_1" + ".";
            else if (i == split.length - 1)
                str = str + split[split.length - 1];
            else str = str + split[i] + ".";
        }
        return str;
    }

    /**
     * 获取大图名字
     *
     * @param url
     * @return
     */
    private static String getThumbPic2(String url) {
        String[] split = url.split("\\.");
        String str = "";
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 2)
                str = str + split[i] + "_2" + ".";
            else if (i == split.length - 1)
                str = str + split[split.length - 1];
            else str = str + split[i] + ".";
        }
        return str;
    }

    /**
     * 得到当前图片文件的路径
     *
     * @return
     */
    public static File getmCurrentPhotoFile() {
        if (mCurrentPhotoFile == null) {
            if (!PHOTO_DIR.exists()) {
                PHOTO_DIR.mkdirs();        // 创建照片的存储目录
            }
            mCurrentPhotoFile = new File(PHOTO_DIR, getCharacterAndNumber() + ".png"/*此处可更换文件后缀*/);
            if (!mCurrentPhotoFile.exists())    // 判断存储文件是否存在>不存在则创建
                try {
                    mCurrentPhotoFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return mCurrentPhotoFile;
    }

    /**
     * 开始启动照片选择框
     *
     * @param context 是否裁剪
     */
    public static void doPickPhotoAction(final Activity context) {
        final Context dialogContext = new ContextThemeWrapper(context, android.R.style.Theme_Light);
        String[] choices;
        choices = new String[2];
        choices[0] = "拍照";            // 拍照
        choices[1] = "从相册中选择";    // 从相册中选择
        final ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, choices);

        final AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
        builder.setTitle("选择上传图片方式");
        builder.setSingleChoiceItems(adapter, -1,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switch (which) {
                            case 0: {
                                String status = Environment
                                        .getExternalStorageState();
                                if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
                                    doTakePhoto(context);// 用户点击了从照相机获取
                                } else {
                                    Toast.makeText(dialogContext, "没有找到SD卡", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            }
                            case 1:
                                doPickPhotoFromGallery(context);// 从相册中去获取
                                break;
                        }
                    }
                });
        builder.setNegativeButton(
                "取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 调用拍照功能
     */
    public static void doTakePhoto(Activity context) {
        try {
            if (!PHOTO_DIR.exists()) {
                PHOTO_DIR.mkdirs();    // 创建照片的存储目录
            }
//			imageUri = Uri.fromFile(getmCurrentPhotoFile());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            context.startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "没有找到照相机", Toast.LENGTH_SHORT).show();
        }
    }


    public static Bitmap decodeSampledBitmapFromResource(String pathName,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth && height > reqHeight) {
            // 计算出实际宽度和目标宽度的比率
            int widthRatio = Math.round((float) width / (float) reqWidth);
            int heightRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = Math.max(widthRatio, heightRatio);
        }
        return inSampleSize;
    }


    /**
     * 调用相册程序
     *
     * @param context
     */
    public static void doPickPhotoFromGallery(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            ((Activity) context).startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "没有找到相册", Toast.LENGTH_SHORT).show();
        }
    }

    public static Uri getImageUri() {
        File temporaryFile = new File(PHOTO_DIR, getCharacterAndNumber() + ".png");
        imageUri = Uri.fromFile(temporaryFile);
        return imageUri;
    }

    public static Uri getImageCaiUri() {
        File temporaryFile = new File(PHOTO_DIR, getCharacterAndNumber() + ".png");
        imageCaiUri = Uri.fromFile(temporaryFile);
        return imageCaiUri;
    }

    public static Uri getImageCaiUri(File temporaryFile) {
        imageCaiUri = Uri.fromFile(temporaryFile);
        return imageCaiUri;
    }

    //	得到系统当前时间并转化为字符串
    @SuppressLint("SimpleDateFormat")
    public static String getCharacterAndNumber() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        return rel;
    }

    //	压缩图片(第二次)
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 200) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //	压缩图片(第一次)
    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        if (baos.toByteArray().length / 1024 > 200) {//判断如果图片大于200KB,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 160f;//这里设置高度为800f
        float ww = 130f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public static void saveMyBitmap(Bitmap mBitmap, File file) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
            byte[] bitmapData = baos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }


    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        if (path == null || path.contentEquals(""))
            return;

        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }


    /**
     * 根据路径删除图片
     *
     * @param file
     */
    public static void deleteTempFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}