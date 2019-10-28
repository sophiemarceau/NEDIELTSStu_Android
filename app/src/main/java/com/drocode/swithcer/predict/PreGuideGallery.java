package com.drocode.swithcer.predict;

import java.util.TimerTask;

import com.lels.student.testpredictions.activity.TestPredictionsActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class PreGuideGallery extends Gallery {

	private TestPredictionsActivity m_iact;
	public PreGuideGallery(Context context) {
		super(context);
	}
	
	public PreGuideGallery(Context context,AttributeSet attrs) {
		super(context, attrs);
	}
	
	public PreGuideGallery(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setImageActivity(TestPredictionsActivity iact){
		m_iact = iact;
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		int kEvent;
        if(isScrollingLeft(e1, e2)){ //Check if scrolling left
          kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
          System.out.println("AAAA"+this.getSelectedItemPosition());
        }
        else{ //Otherwise scrolling right
          kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
          System.out.println("BBB"+this.getSelectedItemPosition());
        }
        onKeyDown(kEvent, null); 
        if(this.getSelectedItemPosition()==0)
        	this.setSelection(PreImageAdapter.myuri.length);
        System.out.println("DDD"+this.getSelectedItemPosition());
		new java.util.Timer().schedule(new TimerTask(){
		       public void run() {
		    	   m_iact.timeFlag = false;
		    	   this.cancel();
		       }}, 3000);
        return true;  
		
	}
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2){
		 System.out.println(this.getSelectedItemPosition());
        return e2.getX() > e1.getX();
        
    }
	
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		m_iact.timeTaks.timeCondition = false;
		return super.onScroll(e1, e2, distanceX, distanceY);
	}

}
