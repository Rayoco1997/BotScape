package mx.itesm.jrcti.botscape;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;



public class SplashScreen extends Activity implements MediaPlayer.OnCompletionListener
{
    private VideoView videoView;
    public static Activity mActivityObj;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        mActivityObj = this;
        setContentView(R.layout.splash);
        String fileName = "android.resource://"+  getPackageName() +"/raw/video";

        videoView = (VideoView) this.findViewById(R.id.surface);
        videoView.setVideoURI(Uri.parse(fileName));
        videoView.setOnCompletionListener(this);
        videoView.start();
        videoView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                videoView.stopPlayback();
                Intent intent = new Intent(SplashScreen.this, AndroidLauncher.class);
                startActivity(intent);
                finish();
                return true;
            }
        });

    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {

        Intent intent = new Intent(this, AndroidLauncher.class);
        startActivity(intent);
        finish();
    }
}
