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

public class FastUiHeader extends FrameLayout
{

	final static String	PULL_DOWN_FRESH	= "下拉刷新数据";

	final static String	RELEASE_2_FRESH	= "松手立即刷新";

	final static String	IS_REFRESH_DATA	= "正在刷新数据";

	public final String	FRESH_SUCCESS	= "刷新数据成功";

	public final String	FRESH_FAILED	= "刷新数据失败";

	final static int	STATE_DONED		= 0x00;

	final static int	STATE_WAITE		= 0X01;

	final static int	STATE_FRESH		= 0x02;

	final static int	STATE_FETCH		= 0x03;

	boolean				isFresh			= false;

	int					viewStatus		= STATE_DONED;

	ImageView			headerIcon;

	TextView			headerText;

	public FastUiHeader(Context context)
	{
		this(context, null);
	}

	public FastUiHeader(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public FastUiHeader(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		LayoutInflater inflater = LayoutInflater.from(context);
		View content = inflater.inflate(R.layout.fresh_header, null);
		LayoutParams params = new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		addView(content, params);

		headerIcon = (ImageView) findViewById(R.id.headerIcon);
		headerText = (TextView) findViewById(R.id.headerText);
	}

	public boolean allowFetch()
	{
		return viewStatus == STATE_FRESH;
	}

	public void setWaiteState()
	{
		if (!isFresh && (viewStatus == STATE_FRESH || viewStatus == STATE_DONED))
		{
			viewStatus = STATE_WAITE;

			Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.ui_arrow_down);
			animation.setFillBefore(true);
			animation.setFillAfter(true);

			headerIcon.setImageResource(R.drawable.ui_head_arrow);
			headerIcon.startAnimation(animation);
			headerText.setText(PULL_DOWN_FRESH);
		}
	}

	public void setFreshState()
	{
		if (!isFresh && (viewStatus == STATE_WAITE || viewStatus == STATE_DONED))
		{
			viewStatus = STATE_FRESH;

			Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.ui_arrow_up);
			animation.setFillBefore(true);
			animation.setFillAfter(true);

			headerIcon.setImageResource(R.drawable.ui_head_arrow);
			headerIcon.startAnimation(animation);
			headerText.setText(RELEASE_2_FRESH);
		}
	}

	public void setFetchState()
	{
		viewStatus = STATE_FETCH;

		headerText.setText(IS_REFRESH_DATA);
		AnimationDrawable drawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.ui_loading);
		headerIcon.setImageDrawable(drawable);
		drawable.start();

		isFresh = true;
	}

	public void setDonedState(boolean success)
	{
		if (viewStatus == STATE_FETCH)
		{
			viewStatus = STATE_DONED;
			headerText.setText(success ? FRESH_SUCCESS : FRESH_FAILED);
			headerIcon.setImageDrawable(null);
			isFresh = false;
		}
	}
}
