package com.example.android.escalatorplayer;


import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private int trackId;
    private String trackName;
    private MediaPlayer mediaPlayer;
    private int trackDuration;
    private SeekBar progressSeekBar;
    private SeekBar volumeSeekBar;
    private int maxVolume = 50;
    private int currentVolume = maxVolume;
    private TextView timerTextView;
    private TextView trackDurationTextView;
    private TextView trackNameTextView;
    private Button playPauseButton;
    private int currentDuration;
    private Button muteButton;
    private boolean muteStatue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        prepare MediaPlayer
         */
        setMainActivityParameters();


        /*
        Play the track
         */
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying()) {
                    playPauseButton.setBackgroundResource(R.drawable.pause);
                    mediaPlayer.start();
                } else {
                    playPauseButton.setBackgroundResource(R.drawable.play);
                    mediaPlayer.pause();
                }
            }
        });

        /*
        set progressSeekBar to track the audio playing progress
         */
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                if (mediaPlayer != null) {
                    currentDuration = mediaPlayer.getCurrentPosition();
                    progressSeekBar.setProgress(currentDuration);
                    timerTextView.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) currentDuration),
                            TimeUnit.MILLISECONDS.toSeconds((long) currentDuration) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                            currentDuration)))
                    );

                }
                handler.postDelayed(this, 1000);
            }
        });

        /*
        Make the MediaPlayer respond to the progressSeekBar changes
         */

        progressSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if (input) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        /*
        set volume level
         */
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                float log1=(float)(Math.log(maxVolume-progress)/Math.log(maxVolume));
                if (input){
                    mediaPlayer.setVolume(1-log1, 1-log1);
                }
                if(1-log1 == 0){
                    muteButton.setBackgroundResource(R.drawable.mute);
                    muteStatue = true;
                }else if(muteStatue){
                    muteStatue = false;
                    muteButton.setBackgroundResource(R.drawable.volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        /*
        mute sound
         */
        muteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!muteStatue) {
                    muteStatue = true;
                    mediaPlayer.setVolume(0, 0);
                    muteButton.setBackgroundResource(R.drawable.mute);
                    volumeSeekBar.setProgress(0);
                }else{
                    muteStatue = false;
                    mediaPlayer.setVolume(1, 1);
                    muteButton.setBackgroundResource(R.drawable.volume);
                    volumeSeekBar.setProgress(maxVolume);
                }
            }
        });

        /*
        *on complete listener
        */
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                 currentDuration = 0;//set current time back to zero
                playPauseButton.setBackgroundResource(R.drawable.play);
                // display toaster t
                Toast toast = Toast.makeText(MainActivity.this, "done!", Toast.LENGTH_SHORT);
                toast.show();
                progressSeekBar.setProgress(currentDuration); //set progressSeekBar back to zero
            }
        });


    }

    private void setMainActivityParameters(){

        trackId = R.raw.sob_rashrash;

        mediaPlayer = MediaPlayer.create(MainActivity.this, trackId );
        trackDuration = mediaPlayer.getDuration();

        trackName = "Zoulikha - Sob Rashrash (cover)";
        trackDurationTextView = findViewById(R.id.track_name_text_view);
        trackDurationTextView.setText(trackName);


        progressSeekBar = findViewById(R.id.progress_seek_bar);
        progressSeekBar.setMax(trackDuration);

        volumeSeekBar = findViewById(R.id.volume_seek_bar);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        timerTextView = findViewById(R.id.timer_text_view);
        timerTextView.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) trackDuration),
                TimeUnit.MILLISECONDS.toSeconds((long) trackDuration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                trackDuration)))
        );

        Toast toast = Toast.makeText(MainActivity.this, "trackDuration="+trackDuration, Toast.LENGTH_SHORT);
        toast.show();
        playPauseButton = findViewById(R.id.play_pause_button);

        muteButton = findViewById(R.id.mute_button);

        trackDurationTextView = findViewById(R.id.track_time_text_view);
        trackDurationTextView.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) trackDuration),
                TimeUnit.MILLISECONDS.toSeconds((long) trackDuration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)
                                trackDuration)))
        );
    }
}
