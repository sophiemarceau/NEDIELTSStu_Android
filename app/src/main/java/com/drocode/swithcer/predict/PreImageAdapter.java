package com.drocode.swithcer.predict;

import java.util.List;

import com.example.strudentlelts.R;
import com.lels.student.testpredictions.activity.TestPredictionsActivity;
import com.lidroid.xutils.BitmapUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class PreImageAdapter extends BaseAdapter {
	private List<String> imageUrls; // 图片地址list
	private Context context;
	private PreImageAdapter self;
	Uri uri;
	Intent intent;
	ImageView imageView;

//	public static String[] myuri = {
//			"http://www.apkbus.com/template/devc/style/logo.png",
//			"http://www.apkbus.com/template/devc/style/logo.png",
//			"http://www.apkbus.com/template/devc/style/logo.png",
//			"http://www.apkbus.com/template/devc/style/logo.png" };
	
	public static String[] myuri;

	private BitmapUtils bitmaputils;

	public PreImageAdapter(Context context,String[] str_im) {
		this.context = context;
		this.self = this;
		this.myuri = str_im;
		bitmaputils = new BitmapUtils(context);
	}

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	public Object getItem(int position) {
		return imageUrls.get(position % myuri.length);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case 0: {
					self.notifyDataSetChanged();
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
			}
		}
	};

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {

		// Bitmap image;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item,
					null); // 实例化convertView
			Gallery.LayoutParams params = new Gallery.LayoutParams(
					Gallery.LayoutParams.WRAP_CONTENT,
					Gallery.LayoutParams.WRAP_CONTENT);
			convertView.setLayoutParams(params);
			convertView.setTag(myuri);

		} else {
			convertView = (View) convertView.getTag();
		}
		imageView = (ImageView) convertView.findViewById(R.id.gallery_image);

		position = position % myuri.length;

		bitmaputils.display(imageView, myuri[position]);
		// 设置缩放比例：保持原样
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		((TestPredictionsActivity) context)
				.changePointView(position % myuri.length);
		return convertView;

	}
}
