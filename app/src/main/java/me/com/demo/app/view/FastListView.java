package me.com.demo.app.view;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import com.example.strudentlelts.R;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public class FastListView extends AdapterView<ListAdapter> {

	public interface FreshOrLoadListener {
		public void onNeedFresh();

		public void onNeedLoad(Feedback feedback);
	}
	
	public interface Feedback {
		public void onfeed(String status);
	}

	class ListDataSetObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			dataChanged = true;
			requestLayout();
		}

		@Override
		public void onInvalidated() {
			dataChanged = true;
			requestLayout();
		}
	}

	boolean supportSwipe = true;

	boolean supportFresh = true;

	boolean support_load = true;

	int headerSize;
	int footerSize;
	FastUiHeader headerView;
	FastUiFooter footerView;

	VelocityTracker evTracker;
	OverScroller mScroller;
	OverScroller iScroller;

	boolean delaycheck = false;

	boolean allow_slop = false;

	int pointerOne;
	int longMillis;
	long downMillis;
	int mTouchSlop;
	int minumSpeed;
	int maxumSpeed;
	int slideFromY;
	float touchFromX;
	float touchFromY;

	int itemHeight;
	int overLength;
	int dividerHeight;
	Rect dividerBounds;
	Drawable dividerDrawable;

	int[] oldLocation;

	ListDataSetObserver dataObserver;
	boolean dataChanged;
	Object asynObject;
	ListAdapter listAdapter;
	LinkedList<View> activesList;
	LinkedList<View> recycleList;
	LinkedList<View> destroyList;
	HashMap<View, String> positionmap;

	Object swipeObject;
	int clickIndex;
	int swipeIndex;
	int swipeWidth;
	boolean allowSwipe = false;

	FreshOrLoadListener mListener;

	public FastListView(Context context) {
		this(context, null);
	}

	public FastListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FastListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		dividerBounds = new Rect();

		float density = context.getResources().getDisplayMetrics().density;

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.FastListView);
		supportSwipe = a.getBoolean(R.styleable.FastListView_supportSwipe,
				false);
		supportFresh = a.getBoolean(R.styleable.FastListView_supportFresh,
				false);
		support_load = a.getBoolean(R.styleable.FastListView_support_load,
				false);
		itemHeight = a.getDimensionPixelSize(
				R.styleable.FastListView_fastItemHeight, (int) (60 * density));
		dividerHeight = a.getDimensionPixelSize(
				R.styleable.FastListView_dividerHeight, 1);
		dividerDrawable = a
				.getDrawable(R.styleable.FastListView_dividerDrawalbe);

		swipeWidth = a.getDimensionPixelSize(
				R.styleable.FastListView_fastSwipeWidth, 0);
		a.recycle();

		swipeIndex = -1;
		overLength = (int) (density * 80);

		oldLocation = new int[6];

		iScroller = new OverScroller(context);
		mScroller = new OverScroller(context);

		asynObject = new Object();
		activesList = new LinkedList<View>();
		recycleList = new LinkedList<View>();
		destroyList = new LinkedList<View>();
		positionmap = new HashMap<View, String>();

		dataObserver = new ListDataSetObserver();

		ViewConfiguration conf = ViewConfiguration.get(context);
		longMillis = ViewConfiguration.getLongPressTimeout();
		mTouchSlop = conf.getScaledTouchSlop();
		minumSpeed = conf.getScaledMinimumFlingVelocity();
		maxumSpeed = conf.getScaledMaximumFlingVelocity();

		if (supportFresh) {
			headerView = new FastUiHeader(context);
			headerSize = (int) (density * 100);

			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					headerSize);
			addViewInLayout(headerView, 0, params);
		}

		if (support_load) {
			footerView = new FastUiFooter(context);
			footerSize = (int) (density * 60);

			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
					footerSize);
			addViewInLayout(footerView, getChildCount(), params);
		}
	}

	public void setFreshOrLoadListener(FreshOrLoadListener listener) {
		mListener = listener;
	}

	public void noticeFreshDone(boolean success, boolean dataDone) {
		if (headerView != null) {
			headerView.setDonedState(success);
		}

		if (footerView != null) {
			footerView.setDonedState(success, dataDone);
		}

		delaycheck = true;

		postDelayed(new Runnable() {
			@Override
			public void run() {
				checkScrollY();
			}
		}, 640);
	}

	public void noticeLoadDone(boolean success, boolean dataDone) {
		if (footerView != null) {
			footerView.setDonedState(success, dataDone);
		}

		delaycheck = true;

		postDelayed(new Runnable() {
			@Override
			public void run() {
				checkScrollY();
			}
		}, 640);
	}

	@Override
	public ListAdapter getAdapter() {
		return listAdapter;
	}

	@Override
	public View getSelectedView() {
		return null;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (listAdapter != null) {
			listAdapter.unregisterDataSetObserver(dataObserver);
		}

		listAdapter = adapter;

		if (adapter != null) {
			adapter.registerDataSetObserver(dataObserver);
		}

		dataChanged = true;

		requestLayout();
	}

	@Override
	public void setSelection(int position) {

	}

	public boolean objectEquals(Object o1, Object o2) {
		if (o1 != null) {
			return o1.equals(o2);
		} else if (o2 != null) {
			return o2.equals(o1);
		} else {
			return true;
		}
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int pl = getPaddingLeft();
		int pt = getPaddingTop();
		int pr = getPaddingRight();
		int pb = getPaddingBottom();

		int measuredWidth = 0, measuredHeight = 0;

		if (widthMode == MeasureSpec.EXACTLY) {
			measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
		} else {
			measuredWidth = 100;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
		} else {
			measuredHeight = pt + pb + getListCount() * itemHeight;
		}

		setMeasuredDimension(measuredWidth, measuredHeight);

		layoutExtras();

		if (pl != oldLocation[0] || pt != oldLocation[1]
				|| pr != oldLocation[2] || pb != oldLocation[3]
				|| measuredWidth != oldLocation[4]
				|| measuredHeight != oldLocation[5]) {
			oldLocation[0] = pl;
			oldLocation[1] = pt;
			oldLocation[2] = pr;
			oldLocation[3] = pb;
			oldLocation[4] = measuredWidth;
			oldLocation[5] = measuredHeight;

			dataChanged = true;
		}

		if (dataChanged) {
			dataChanged = false;
			recycleList();
			installList();
		}

	}

	public void layoutExtras() {
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();

		int drawWidth = getMeasuredWidth() - paddingLeft - paddingRight;

		if (headerView != null) {
			int widthMeasureSpec = MeasureSpec.makeMeasureSpec(drawWidth,
					MeasureSpec.EXACTLY);
			int heightMeasureSpec = MeasureSpec.makeMeasureSpec(headerSize,
					MeasureSpec.EXACTLY);

			headerView.measure(widthMeasureSpec, heightMeasureSpec);
			headerView.layout(paddingLeft, paddingTop - headerSize, paddingLeft
					+ drawWidth, paddingTop);
		}

		if (footerView != null) {
			int widthMeasureSpec = MeasureSpec.makeMeasureSpec(drawWidth,
					MeasureSpec.EXACTLY);
			int heightMeasureSpec = MeasureSpec.makeMeasureSpec(footerSize,
					MeasureSpec.EXACTLY);
			footerView.measure(widthMeasureSpec, heightMeasureSpec);

			int listCount = getListCount();

			int drawHeight = getMeasuredHeight() - paddingTop - paddingBottom;
			int viewHeight = listCount * itemHeight + (listCount - 1)
					* dividerHeight;

			int top = Math.max(drawHeight, viewHeight) + paddingTop;
			footerView.layout(paddingLeft, top, paddingLeft + drawWidth, top
					+ footerSize);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int newY = mScroller.getCurrY();
			int oldY = getScrollY();

			freshchild(newY, newY > oldY);
			scrollTo(0, newY);
			invalidate();
		}
	}

	@Override
	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);

		if (dividerDrawable != null && dividerHeight > 0) {
			drawDividers(canvas);
		}
	}

	public void drawDividers(Canvas canvas) {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();

		int fromX = getPaddingLeft();

		int stopX = width - getPaddingRight();

		int paddingtop = getPaddingTop();

		int from = getScrollY() / (itemHeight + dividerHeight) + 1;
		int stop = (getScrollY() + height) / (itemHeight + dividerHeight) + 1;

		from = Math.max(from, 1);
		stop = Math.min(stop, getListCount());

		for (int i = from; i < stop; i++) {
			int stopY = i * (itemHeight + dividerHeight) + paddingtop;
			int fromY = stopY - dividerHeight;

			dividerBounds.left = fromX;
			dividerBounds.right = stopX;

			dividerBounds.top = fromY;
			dividerBounds.bottom = stopY;

			dividerDrawable.setBounds(dividerBounds);

			int saved = canvas.save();
			dividerDrawable.draw(canvas);
			canvas.restoreToCount(saved);
		}
	}

	public void onPointerUp(MotionEvent ev) {
		final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
		final int id = ev.getPointerId(pointerIndex);

		if (id == pointerOne) {
			if (evTracker != null) {
				evTracker.clear();
			}

			int newIndex = pointerIndex == 0 ? 1 : 0;
			pointerOne = ev.getPointerId(newIndex);
			touchFromX = ev.getX(newIndex);
			touchFromY = ev.getY(newIndex);
			slideFromY = getScrollY();
		}
	}

	public boolean touchSwipeArea(MotionEvent ev) {
		int paddingTop = getPaddingTop();
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int measuredWidth = getMeasuredWidth();

		int pointerIndex = ev.findPointerIndex(pointerOne);

		int ex = (int) ev.getX(pointerIndex);
		int ey = (int) ev.getY(pointerIndex);

		int top = paddingTop + (itemHeight + dividerHeight) * swipeIndex
				- getScrollY();

		if (swipeWidth < 0) {
			return ex >= paddingLeft && ex <= paddingLeft + swipeWidth
					&& ey >= top && ey <= top + itemHeight;
		} else {
			return ex >= measuredWidth - paddingRight - swipeWidth
					&& ex <= measuredWidth - paddingRight && ey >= top
					&& ey <= top + itemHeight;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean intercept = false;

		int action = ev.getActionMasked();

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			downMillis = System.currentTimeMillis();

			if (touchSwipeArea(ev)) {
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}

				requestDisallowInterceptTouchEvent(true);
			} else if (swipeIndex >= 0) {
				intercept = true;
			} else if (!mScroller.isFinished()) {
				intercept = true;
			} else {
				pointerOne = ev.getPointerId(0);
				touchFromX = ev.getX();
				touchFromY = ev.getY();
				slideFromY = getScrollY();
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			int pointerIndex = ev.findPointerIndex(pointerOne);

			int diffX = (int) (touchFromX - ev.getX(pointerIndex));
			int diffY = (int) (touchFromY - ev.getY(pointerIndex));

			if (supportSwipe && Math.abs(diffX) > mTouchSlop) {
				int index = (int) (slideFromY + touchFromY - getPaddingTop())
						/ (itemHeight + dividerHeight);
				int listCount = getListCount();

				if (index >= 0 && index < listCount) {
					if (getParent() != null) {
						getParent().requestDisallowInterceptTouchEvent(true);
					}

					swipeObject = listAdapter.getItem(index);
					swipeIndex = index;
					allow_slop = false;
					allowSwipe = true;
					intercept = true;
				}
			} else if (Math.abs(diffY) > mTouchSlop) {
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}

				allowSwipe = false;
				allow_slop = true;
				intercept = true;
			}
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {
			onPointerUp(ev);
			break;
		}
		}

		return intercept;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (evTracker == null) {
			evTracker = VelocityTracker.obtain();
		}

		evTracker.addMovement(ev);

		int action = ev.getActionMasked();

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			downMillis = System.currentTimeMillis();
			allow_slop = !mScroller.isFinished();
			allowSwipe = false;

			if (allow_slop) {
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}

				mScroller.abortAnimation();
			}

			if (swipeIndex >= 0) {
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}

				allow_slop = false;
			}

			pointerOne = ev.getPointerId(0);
			touchFromX = ev.getX();
			touchFromY = ev.getY();
			slideFromY = getScrollY();

			clickIndex = (int) (slideFromY + touchFromY - getPaddingTop())
					/ (itemHeight + dividerHeight);
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			if (swipeIndex >= 0 && !allowSwipe) {
				return true;
			}

			int pointerIndex = ev.findPointerIndex(pointerOne);
			int diffX = (int) (touchFromX - ev.getX(pointerIndex));
			int diffY = (int) (touchFromY - ev.getY(pointerIndex));

			if (allowSwipe) {
				final View view = queryChild(swipeIndex);

				int swipe = -diffX;

				if (swipeWidth > 0) {
					swipe = Math.max(-swipeWidth, swipe);
					swipe = Math.min(swipe, 0);
				} else {
					swipe = Math.max(0, swipe);
					swipe = Math.min(swipe, -swipeWidth);
				}

				if (view != null) {
					setTranslate(view, swipe);
				}
			} else if (allow_slop) {
				int scrollY = slideFromY + diffY;
				freshchild(scrollY, diffY > 0);
				scrollTo(0, scrollY);
				detectFresh(scrollY);
			} else if (supportSwipe && Math.abs(diffX) > mTouchSlop) {
				int index = (int) (slideFromY + touchFromY)
						/ (itemHeight + dividerHeight);
				int listCount = getListCount();

				if (index >= 0 && index < listCount) {
					if (getParent() != null) {
						getParent().requestDisallowInterceptTouchEvent(true);
					}

					swipeObject = listAdapter.getItem(index);
					swipeIndex = index;
					allowSwipe = true;
				}
			} else if (Math.abs(diffY) > mTouchSlop) {
				if (getParent() != null) {
					getParent().requestDisallowInterceptTouchEvent(true);
				}

				allow_slop = true;
			}
			break;
		}
		case MotionEvent.ACTION_POINTER_UP: {
			onPointerUp(ev);
			break;
		}
		case MotionEvent.ACTION_CANCEL: {
			evTracker.computeCurrentVelocity(1000, maxumSpeed);
			int velocityY = (int) evTracker.getYVelocity();
			evTracker.recycle();
			evTracker = null;

			if (swipeIndex >= 0) {
				swipeListView();
			} else if (allow_slop) {
				flingListView(velocityY);
			} else {
				clickListView();
			}
			break;
		}
		case MotionEvent.ACTION_UP: {
			evTracker.computeCurrentVelocity(1000, maxumSpeed);
			int velocityY = (int) evTracker.getYVelocity();
			evTracker.recycle();
			evTracker = null;

			if (swipeIndex >= 0) {
				swipeListView();
			} else if (allow_slop) {
				flingListView(velocityY);
			} else {
				clickListView();
			}
			break;
		}
		}

		return true;
	}

	void detectFresh(int scrollY) {
		if (headerView != null) {
			if (scrollY < -headerSize / 2) {
				headerView.setFreshState();
			} else {
				headerView.setWaiteState();
			}
		}

		if (footerView != null) {
			int bottom = getBottomSy();

			if (scrollY > bottom) {
				footerView.setFreshState();
			} else {
				footerView.setWaiteState();
			}
		}
	}

	void swipeListView() {
		int finalX = allowSwipe ? -swipeWidth : 0;
		View view = queryChild(swipeIndex);

		if (!allowSwipe) {
			swipeObject = null;
			swipeIndex = -1;
		}

		if (view != null) {
			int startX = getTranslate(view);
			int millis = Math.abs(finalX - startX) * 2;
			millis = Math.min(240, millis);
			swipeAnimate(view, finalX, millis);
		}

		if (!delaycheck) {
			checkScrollY();
		}
	}

	void clickListView() {
		OnItemLongClickListener longListener = getOnItemLongClickListener();
		OnItemClickListener shortListener = getOnItemClickListener();
		View child = queryChild(clickIndex);
		int count = getListCount();

		int duration = (int) (System.currentTimeMillis() - downMillis);

		boolean invokeShortClick = true;

		if (duration > longMillis && clickIndex >= 0 && clickIndex < count
				&& child != null && longListener != null) {
			invokeShortClick = !longListener.onItemLongClick(this, child,
					clickIndex, listAdapter.getItemId(clickIndex));
		}

		if (invokeShortClick && clickIndex >= 0 && clickIndex < count
				&& child != null && shortListener != null) {
			shortListener.onItemClick(this, child, clickIndex,
					listAdapter.getItemId(clickIndex));
		}

		if (!delaycheck) {
			checkScrollY();
		}
	}

	void flingListView(int velocityY) {
		boolean allowFling = true;
		boolean startFresh = false;
		boolean start_load = false;

		int finalY = 0;

		if (headerView != null && headerView.allowFetch()) {
			iScroller.fling(0, getScrollY(), 0, -velocityY, 0, 0, -headerSize,
					getBottomSy() + footerSize, 0, overLength);
			allowFling = iScroller.getFinalY() > -headerSize / 2;

			if (allowFling) {
				headerView.setWaiteState();
			} else {
				headerView.setFetchState();
				finalY = -headerSize;
				startFresh = true;
			}

		} else if (footerView != null && footerView.allowLoad()) {
			iScroller.fling(0, getScrollY(), 0, -velocityY, 0, 0, -headerSize,
					getBottomSy() + footerSize, 0, overLength);
			allowFling = iScroller.getFinalY() < getBottomSy();

			if (allowFling) {
				footerView.setWaiteState();
			} else {
				finalY = getBottomSy() + footerSize;
				footerView.setFetchState("");
				start_load = true;
			}
		}

		if (allowFling) {
			int minY = getMinScroll();
			int maxY = getMaxScroll();
			mScroller.fling(0, getScrollY(), 0, -velocityY, 0, 0, minY, maxY,
					0, overLength);
		} else {
			int startY = getScrollY();
			int millis = Math.abs(finalY - startY) * 2;
			millis = Math.min(millis, 360);
			mScroller.startScroll(0, startY, 0, finalY - startY, millis);
		}

		invalidate();

		if (startFresh && mListener != null) {
			mListener.onNeedFresh();
		}

		if (start_load && mListener != null) {
			mListener.onNeedLoad(new Feedback() {
				
				@Override
				public void onfeed(String status) {
					System.out.println("pageIndex : onNeedLoad : " + status);
					footerView.setFetchState(status);
				}
			});
		}
	}

	int getTranslate(final View view) {
		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;

			if (group.getChildCount() > 0)
				return (int) ViewHelper.getTranslationX(group.getChildAt(0));
		}
		return (int) ViewHelper.getTranslationX(view);
	}

	void setTranslate(final View view, int tx) {
		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;
			int count = group.getChildCount();
			View child = null;

			for (int index = 0; index < count; index++) {
				child = group.getChildAt(index);
				ViewHelper.setTranslationX(child, tx);
			}
		} else {
			ViewHelper.setTranslationX(view, tx);
		}
	}

	void swipeAnimate(final View view, int finalX, int millis) {
		if (view instanceof ViewGroup) {
			ViewGroup group = (ViewGroup) view;

			int count = group.getChildCount();

			View child = null;

			for (int index = 0; index < count; index++) {
				child = group.getChildAt(index);
				ViewPropertyAnimator.animate(child).translationX(finalX)
						.setDuration(millis);
			}
		} else {
			ViewPropertyAnimator.animate(view).translationX(finalX)
					.setDuration(millis);
		}
	}

	int getListCount() {
		return listAdapter != null ? listAdapter.getCount() : 0;
	}

	int getBottomSy() {
		int drawHeight = getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom();
		int listCount = getListCount();
		int listHeight = listCount * itemHeight;
		int lineHeight = (listCount - 1) * dividerHeight;

		return Math.max(0, listHeight + lineHeight - drawHeight);
	}

	int getMinScroll() {
		return headerView != null && headerView.isFresh ? -headerSize : 0;
	}

	int getMaxScroll() {
		return getBottomSy()
				+ (footerView != null && footerView.allowShow() ? footerSize
						: 0);
	}

	View queryChild(int position) {

		View result = null;

		synchronized (asynObject) {
			String value = String.valueOf(position);
			Set<View> keys = positionmap.keySet();

			for (View view : keys) {
				if (positionmap.get(view).equals(value)) {
					result = view;
					break;
				}
			}
		}

		return result;
	}

	View obtainChild(int position) {
		View child = null;

		if (recycleList.size() > 0) {
			recycleList.remove(0);
		}

		child = listAdapter.getView(position, child, this);
		return child;
	}

	void recycleList() {
		synchronized (asynObject) {
			int count = activesList.size();
			View child = null;

			for (int index = count - 1; index >= 0; index--) {
				child = activesList.remove(index);
				positionmap.remove(child);

				cleanupLayoutState(child);
				removeViewInLayout(child);

				recycleList.add(child);
			}
		}
	}

	void installList() {

		int listCount = getListCount();

		if (swipeIndex >= listCount) {
			swipeIndex = -1;
		} else if (swipeIndex >= 0) {
			Object object = listAdapter.getItem(swipeIndex);

			if (!objectEquals(swipeObject, object)) {
				swipeObject = null;
				swipeIndex = -1;
			}
		}

		freshchild(getScrollY(), true);

		if (!delaycheck) {
			checkScrollY();
		}
	}

	void recycleView(View view) {
		activesList.remove(view);
		positionmap.remove(view);

		cleanupLayoutState(view);
		removeViewInLayout(view);

		recycleList.add(view);
	}

	void installView(int index, boolean isFoot) {
		View child = obtainChild(index);

		int addWhere = 0;

		if (isFoot) {
			addWhere = footerView == null ? getChildCount()
					: getChildCount() - 1;
		} else {
			addWhere = headerView == null ? 0 : 1;
		}

		if (index == swipeIndex) {
			setTranslate(child, -swipeWidth);
		}

		addViewInLayout(child, addWhere,
				new LayoutParams(child.getMeasuredWidth(), itemHeight));
		positionmap.put(child, String.valueOf(index));
		activesList.add(child);

		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();

		int drawWidth = getMeasuredWidth() - paddingLeft - paddingRight;
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(drawWidth,
				MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(itemHeight,
				MeasureSpec.EXACTLY);

		child.measure(widthMeasureSpec, heightMeasureSpec);

		int top = paddingTop + index * (itemHeight + dividerHeight);
		child.layout(paddingLeft, top, paddingLeft + drawWidth, top
				+ itemHeight);
	}

	void freshchild(int scrollY, boolean isFoot) {

		int drawHeight = getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom();

		int stop = (scrollY + drawHeight) / (itemHeight + dividerHeight) + 2;
		int from = scrollY / (itemHeight + dividerHeight) - 2;
		int listCount = getListCount();

		from = Math.max(0, from);
		stop = Math.min(stop, listCount - 1);

		synchronized (asynObject) {
			Set<View> set = positionmap.keySet();

			for (View child : set) {
				String string = positionmap.get(child);
				int value = Integer.parseInt(string);

				if (value < from || from > stop) {
					destroyList.add(child);
				}
			}

			for (View child : destroyList) {
				recycleView(child);
			}

			destroyList.clear();

			for (int index = from; index <= stop; index++) {
				String string = String.valueOf(index);

				if (!positionmap.containsValue(string)) {
					installView(index, isFoot);
				}
			}

		}

	}

	void checkScrollY() {
		delaycheck = false;

		int startY = mScroller.isFinished() ? getScrollY() : mScroller
				.getFinalY();

		int min = getMinScroll();
		int max = getMaxScroll();
		int finalY = startY;

		finalY = Math.max(finalY, min);
		finalY = Math.min(finalY, max);

		if (finalY != startY && evTracker == null) {
			mScroller.abortAnimation();
			int millis = Math.abs(finalY - startY);

			mScroller.startScroll(0, startY, 0, finalY - startY, millis);
			invalidate();
		}
	}

}
