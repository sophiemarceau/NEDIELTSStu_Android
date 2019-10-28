package com.lels.student.studyonline;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.strudentlelts.R;
import com.lels.bean.LodDialogClass;

public class Player implements OnBufferingUpdateListener, OnCompletionListener, SurfaceHolder.Callback {

	private int videoWidth;
	private int videoHeight;
	public MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private SeekBar skbProgress;
	private Timer mTimer = new Timer();
	private ImageView controller;
	private Context context;
	private LodDialogClass lodclass;
	private int position;

	public Player(Context cxt, SurfaceView surfaceView, SeekBar skbProgress, ImageView controller,LodDialogClass lodclass) {
		this.skbProgress = skbProgress;
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mTimer.schedule(mTimerTask, 0, 1000);
		this.controller = controller;
		this.context = cxt;
		this.lodclass = lodclass;
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
	}

	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};

	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();

			if (duration > 0) {
				long pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
		};
	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(position > 0 && this.videoUrl != null){
            play(position);
            position = 0;
        }
		Log.e("mediaPlayer", "surface created");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.e("mediaPlayer", "surface changed");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e("mediaPlayer", "surface destroyed");
		if (isFinish) {
			stop();
			return;
		}
		if (isPlaying()) {
			position = mediaPlayer.getCurrentPosition();
            mediaPlayer.stop();
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.e("mediaPlayer", "surface complete");
		this.controller.setBackgroundResource(R.drawable.play);
		skbProgress.setProgress(skbProgress.getMax());
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
	}

	/*@Override
	public void onPrepared(MediaPlayer mp) {
		Log.e("mediaPlayer", "onPrepared");
		
		videoWidth = mediaPlayer.getVideoWidth();
		videoHeight = mediaPlayer.getVideoHeight();
		if (videoHeight != 0 && videoWidth != 0) {
			mp.start();
			if (position > 0) {
				mediaPlayer.seekTo(position);
			}
		}
		Log.e("mediaPlayer", "onPrepared");
		lodclass.closeCustomCircleProgressDialog();
	}*/
	
	// *****************************************************
	
	private String videoUrl;
	public void playUrl(String videoUrl) {
		this.videoUrl = videoUrl;
		this.controller.setBackgroundResource(R.drawable.pause);
		play(0);
	}

	public void pause() {
		this.controller.setBackgroundResource(R.drawable.play);
		mediaPlayer.pause();
	}
	
	public void start() {
		this.controller.setBackgroundResource(R.drawable.pause);
		mediaPlayer.start();
	}

	public void stop() {
		this.controller.setBackgroundResource(R.drawable.play);
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	boolean isFinish = false;
	public void finish() {
		isFinish = true;
	}
	
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}
	
	private void play(int position) {
		this.controller.setBackgroundResource(R.drawable.pause);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(videoUrl);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.prepare();//缓冲
            mediaPlayer.setOnPreparedListener(new PrepareListener(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	
	private final class PrepareListener implements OnPreparedListener{
        private int position;
         
        public PrepareListener(int position) {
             this.position = position;
        }
 
        public void onPrepared(MediaPlayer mp) {
        	mp.start();
            if(position>0) mediaPlayer.seekTo(position);
            lodclass.closeCustomCircleProgressDialog();
        }
    }

}
