package com.codewithankit.musicbox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaDescrambler;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mp;
    private ImageView artiseImage;
    private TextView lefttime;
    private TextView righttime;
    private SeekBar seekBar;
    private Button leftbutton , middlebutton , rightbutton;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUp();

        artiseImage.setImageResource(R.drawable.edsheeren);

        seekBar.setMax(mp.getDuration());
        final int duration   = mp.getDuration();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mp.seekTo(progress);
                }

                int currentPos = mp.getCurrentPosition();

                float t1 = currentPos/(60000f);
                float t2 = (duration - currentPos)/(60000f);

                lefttime.setText(String.format("%.2f" , t1));
                righttime.setText(String.format("%.2f" , t2));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setUp(){

        mp = new MediaPlayer();
        mp = MediaPlayer.create(getApplicationContext() , R.raw.givemelove_ed_sheeren);

        artiseImage = (ImageView)findViewById(R.id.imageID);
        artiseImage = (ImageView)findViewById(R.id.imageID);
        lefttime = (TextView) findViewById(R.id.leftTimeID);
        righttime = (TextView) findViewById(R.id.rightTimeID);
        seekBar = (SeekBar) findViewById(R.id.seekBarID);
        leftbutton = (Button) findViewById(R.id.prevBtnId);
        middlebutton = (Button) findViewById(R.id.midBtnID);
        rightbutton = (Button) findViewById(R.id.nextBtnId);

        //registering all the buttons to the click listener..
        leftbutton.setOnClickListener(this);
        middlebutton.setOnClickListener(this);
        rightbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prevBtnId:
                //code
                leftaction();
                break;
            case R.id.midBtnID:
                if(mp.isPlaying()){
                    stopMusic();
                }
                else{
                    startMusic();
                }
                //code
                break;
            case R.id.nextBtnId:
                //code
                rightaction();
                break;
        }
    }

    public void leftaction(){
            mp.seekTo(0);
    }

    public void rightaction(){
            mp.seekTo(mp.getDuration());
            middlebutton.setBackgroundResource(android.R.drawable.ic_media_play);
    }

    public void startMusic(){
        if(mp != null){
            mp.start();
            UpdateThread();
            middlebutton.setBackgroundResource(android.R.drawable.ic_media_pause);
        }
    }

    public void stopMusic(){
        if(mp!=null){
            mp.pause();
            middlebutton.setBackgroundResource(android.R.drawable.ic_media_play);
        }
    }

    public void UpdateThread(){

        thread = new Thread(){
            @Override
            public void run() {
                try{

                    while(mp != null && mp.isPlaying()) {

                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @SuppressLint({"DefaultLocale", "SimpleDateFormat", "SetTextI18n"})
                            @Override
                            public void run() {

                                int newposition = mp.getCurrentPosition();
                                seekBar.setProgress(newposition);

                                //lets update the text now..
                                int t1 = mp.getCurrentPosition()/1000;
                                int t2 = (mp.getDuration() - mp.getCurrentPosition())/1000;

                                int mm1 = t1/(60);
                                int ss1 = t1 - (mm1*60);

                                lefttime.setText(String.valueOf(mm1) + ":" + String.valueOf(ss1));

                                int mm2 = t2/60;
                                int ss2 = t2 - (mm2*60);

                                righttime.setText(String.valueOf(mm2) + ":" + String.valueOf(ss2));

                            }
                        });

                    }
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }


    @Override
    protected void onDestroy() {
        if(mp!=null && mp.isPlaying()){
            mp.stop();
            mp.release();
            mp = null;
        }
        super.onDestroy();
    }
}
