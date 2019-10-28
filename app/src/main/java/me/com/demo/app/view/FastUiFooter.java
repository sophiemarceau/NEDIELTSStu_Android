package me.com.demo.app.view;

import com.example.strudentlelts.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FastUiFooter extends FrameLayout
{

	final static String	PULL_DOWN_FRESH	= "上拉加载更多";

	final static String	RELEASE_2_FRESH	= "松手立即加载";

	final static String	IS_REFRESH_DATA	= "正在加载下页";

	public final String	LOAD_SUCCESS	= "加载数据成功";

	public final String	LOAD_FAILED		= "加载数据失败";

	public final String	LOAD_FINISH		= "数据已经全部加载";

	final static int	STATE_DONED		= 0x00;

	final static int	STATE_WAITE		= 0X01;

	final static int	STATE_FRESH		= 0x02;

	final static int	STATE_FETCH		= 0x03;

	int					viewStatus		= STATE_DONED;

	boolean				is_load			= false;

	boolean				is_done			= false;

	ImageView			footerIcon;
	TextView			footerText;

	public FastUiFooter(Context context)
	{
		this(context, null);
	}

	public FastUiFooter(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public FastUiFooter(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		LayoutInflater inflater = LayoutInflater.from(context);
		View content = inflater.inflate(R.layout.fresh_footer, null);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);

		addView(content, params);

		footerIcon = (ImageView) findViewById(R.id.footerIcon);
		footerText = (TextView) findViewById(R.id.footerText);
		footerText.setText(PULL_DOWN_FRESH);

	}

	public boolean allowShow()
	{
		return is_load && !is_done;
	}

	public boolean allowLoad()
	{
		return viewStatus == STATE_FRESH;
	}

	public void setWaiteState()
	{
		if (!is_load && !is_done && (viewStatus == STATE_FRESH || viewStatus == STATE_DONED))
		{
			viewStatus = STATE_WAITE;

			Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.ui_arrow_down);
			animation.setFillBefore(true);
			animation.setFillAfter(true);
			footerIcon.startAnimation(animation);
			footerText.setText(PULL_DOWN_FRESH);
		}
	}

	public void setFreshState()
	{
		if (!is_load && !is_done && (viewStatus == STATE_WAITE || viewStatus == STATE_DONED))
		{
			viewStatus = STATE_FRESH;

			Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.ui_arrow_up);
			animation.setFillBefore(true);
			animation.setFillAfter(true);
			footerIcon.startAnimation(animation);
			footerText.setText(RELEASE_2_FRESH);
		}
	}

	public void setFetchState(String freshContent)
	{
		viewStatus = STATE_FETCH;
		if (null == freshContent) {
			footerText.setText(IS_REFRESH_DATA);
		} else {
			footerText.setText(freshContent);
		}
		//footerText.setText(IS_REFRESH_DATA);
		AnimationDrawable drawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.ui_loading);
		footerIcon.setImageDrawable(drawable);
		drawable.start();

		is_load = true;
	}

	public void setDonedState(boolean success, boolean dataDone)
	{
		if (viewStatus == STATE_FETCH)
		{
			viewStatus = STATE_DONED;
			String message = success ? (dataDone ? LOAD_FINISH : LOAD_SUCCESS) : LOAD_FAILED;

			footerIcon.setImageDrawable(null);
			footerText.setText(message);
			is_load = false;
		}

		is_done = success && dataDone;
	}
}
