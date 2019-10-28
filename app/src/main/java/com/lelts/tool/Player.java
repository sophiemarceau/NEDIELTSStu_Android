package com.lelts.tool;

import java.util.Timer;
import java.util.TimerTask;

import com.example.strudentlelts.R;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

public class Player {
	private ImageView imageplay;
	boolean flag = false;
	public MediaPlayer mediaPlayer;
	private SeekBar skbProgress;
	private Timer mTimer = new Timer();
	

	public Player(SeekBar skbProgress, ImageView imageplay)	{
	
		this.skbProgress = skbProgress;
		this.imageplay = imageplay;

		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
		mTimer.schedule(mTimerTask, 0, 100);
	}

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
				int pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress(pos);
			}
		};
	};

	public void play() {
		mediaPlayer.start();
	}

	public void playUrl(String videoUrl) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(videoUrl);
			skbProgress.setProgress(0);
			mediaPlayer.prepare();
			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					skbProgress.setProgress(0);
					imageplay.setBackgroundResource(R.drawable.bofang);
				}
			});

			System.out.println("mplayer---" + mediaPlayer.getDuration() + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pause() {
		mediaPlayer.pause();
	}

	public void stop() {

		if (mediaPlayer != null) {
			handleProgress.removeCallbacks(mTimerTask);
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public void onPrepared(MediaPlayer arg0) {
		arg0.start();
		Log.e("mediaPlayer", "onPrepared");
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		Log.e(currentProgress + "% play", percent + "% buffer");
	}

}
