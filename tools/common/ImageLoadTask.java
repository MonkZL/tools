package com.doctoryun.common;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * @Description 调用系统拍照或进入图库中选择照片,再进行裁剪,压缩.
 * @author 疯尘丶
 */
public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {
	@Override
	protected Bitmap doInBackground(Void... params) {
		return null;
	}

//	private String url;
//	private ImageView imageView;
//
//	public ImageLoadTask(String url, ImageView imageView) {
//		this.url = url;
//		this.imageView = imageView;
//	}
//
//	@Override
//	protected Bitmap doInBackground(Void... params) {
//		try {
//			if(url==null || url.isEmpty())
//				return null;
//
//			URL urlConnection = new URL(url);
//			HttpURLConnection connection = (HttpURLConnection) urlConnection
//					.openConnection();
//			connection.setDoInput(true);
//			connection.connect();
//			InputStream input = connection.getInputStream();
//			Bitmap myBitmap = BitmapFactory.decodeStream(input);
//			return myBitmap;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	@Override
//	protected void onPostExecute(Bitmap result) {
//		super.onPostExecute(result);
//		imageView.setImageBitmap(result);
//	}

}