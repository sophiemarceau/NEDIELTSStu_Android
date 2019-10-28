package com.lelts.student.myself.adapter;

import java.util.List;

import me.maxwin.view.MessageItem;

import com.example.strudentlelts.R;
import com.lels.constants.Constants;
import com.lelts.student.myself.MystudentMessageTestActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyselfMessageAdapter extends BaseAdapter {

	Context mContext;
	List<MessageItem> mList;
	private String token;
	// private HashMap<String, String> status;
	private LayoutInflater mInflater;

	public MyselfMessageAdapter(Context context, List<MessageItem> mMessageItems, String token) {
		super();
		this.mContext = context;
		this.mList = mMessageItems;
		this.token = token;
		mInflater = LayoutInflater.from(mContext);
	}

	public void updataadapter(List<MessageItem> mMessageItems) {
		this.mList = mMessageItems;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mList.size() > 0) {
			return mList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_myself_message_test, null);
			holder.textview_message_title = (TextView) convertView.findViewById(R.id.textview_message_title);
			holder.textview_message_time = (TextView) convertView.findViewById(R.id.textview_message_time);
			holder.btn_del = (Button) convertView.findViewById(R.id.btn_del);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (mList.size() > 0) {
			holder.textview_message_title.setText(mList.get(position).getTitle());
			holder.textview_message_time.setText(mList.get(position).getCreateTime());
			if (mList.get(position).getIsRead().equalsIgnoreCase("0")) {
				System.out.println("变色啊 ");
				holder.textview_message_title.setTextColor(Color.RED);
				holder.textview_message_time.setTextColor(Color.RED);
			} else {
				holder.textview_message_title.setTextColor(Color.BLACK);
				holder.textview_message_time.setTextColor(Color.BLACK);
			}
			holder.btn_del.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
//					Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
					System.out.println("删除的item的索引值   = " + position);
					deleteTest(position);
//					mList.remove(position);
//					notifyDataSetChanged();
				}
			});

//			OnDeteleListener mOnDeteleListener = new OnDeteleListener(position);
//			holder.btn_del.setOnClickListener(mOnDeteleListener);
		}
		return convertView;
	}

//	class OnDeteleListener implements OnClickListener {
//		private int mCurrPosition;
//
//		public OnDeteleListener(int position) {
//			this.mCurrPosition = position;
//		}
//
//		@Override
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			deleteTest(mCurrPosition);
//			mList.clear();
//			mList.remove(mCurrPosition);
//			MyselfMessageAdapter.this.notifyDataSetChanged();
//		}
//	}

	class ViewHolder {
		TextView textview_message_title;
		TextView textview_message_time;
		Button btn_del;
	}

	/**
	 * 方法说明：删除 messageId=[消息ID，不可为空] type=[消息类型，0是系统消息、1是教师提醒消息，不可为空]
	 * optType=[操作类型,read:阅读 del:删除,不可为空]
	 */
	private void deleteTest(final int position) {
		RequestParams params = new RequestParams();
		HttpUtils utils = new HttpUtils();
		// xutil网络解析有延迟
		utils.configCurrentHttpCacheExpiry(0);
		utils.configDefaultHttpCacheExpiry(0);

		String id = mList.get(position).getID();
		String type = mList.get(position).getType();
		String optType = "del";
		System.out.println("123456id:+" + id + ",type:" + type + ",optType" + optType);
		String url = Constants.URL_MYSELF_MESSAGE_DELECT + "?messageId=" + id + "&type=" + type + "&optType=" + optType;
		params.addHeader("Authentication", token);
		utils.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				System.out.println("========aaaaaaa失败=======");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				String result = arg0.result;
				mList.remove(position);
				notifyDataSetChanged();
				Toast.makeText(mContext, "删除", Toast.LENGTH_SHORT).show();
				System.out.println("deleteresult=====================" + result);
//				mSwipeListView.dismiss(position);
			}
		});
	}
}
