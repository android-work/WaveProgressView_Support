package com.work.android.waveprogressview_support;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.work.android.waveprogressview.WaveProgress;

public class MainActivity extends Activity {
    private float i = 0;
    private WaveProgress waveProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        waveProgress = findViewById(R.id.wp);
        waveProgress.setBG(R.drawable.circle);
        waveProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i+=0.1;
                waveProgress.setProgress(i);
                waveProgress.setWaveSpeed((long) (1000*i));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        waveProgress.stopAnimator();
        waveProgress = null;
    }
}
