package com.example.ec_327_project;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.util.Log;



public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, SensorEventListener {
    private GameThread gameThread;
    private long touchStartTime;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;
    private Player player;
    private long touchDuration;
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SurfaceView gameSurfaceView = findViewById(R.id.gameSurfaceView);
        gameSurfaceView.getHolder().addCallback(this);

        player = new Player(this);
        gameThread = new GameThread(gameSurfaceView.getHolder(), player);
        gameThread.start();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
            }
        }
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        // Handle surface creation
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // Handle surface changes if needed
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        gameThread.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                player.startChargingJump();
                touchStartTime = System.currentTimeMillis();
                startUpdatingTime();
                break;
            case MotionEvent.ACTION_UP:
                stopUpdatingTime();
                player.releaseJump();
                break;
        }
        return true;
    }

    private void startUpdatingTime() {
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                // Calculate the duration continuously
                long currentTime = System.currentTimeMillis();
                touchDuration = currentTime - touchStartTime;

                // Do something with the continuously updating duration (e.g., display it)
                //Log.d("Touch Duration", "Duration: " + touchDuration + " milliseconds");
                if(touchDuration>0 && touchDuration<500){
                    player.currentId=1;
                }
                if(touchDuration>=500 && touchDuration<1000){
                    player.currentId=2;
                }
                if(touchDuration>=1000){
                    player.currentId=3;
                }

                // Continue updating time every second (adjust the delay as needed)
                handler.postDelayed(this, 10);
            }
        };

        // Start updating time immediately
        handler.post(updateTimeRunnable);
    }

    private void stopUpdatingTime() {
        // Remove the runnable to stop updating time
        handler.removeCallbacks(updateTimeRunnable);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float jumpY = event.values[1];
            float jumpX = event.values[0] / (float)9.809989;

            float testjump = event.values[2];

            Log.d("test", "onSensorChanged: " +testjump);
            Log.d("test", "Y val " +jumpY);
            Log.d("test", "X val " +jumpX);



            if (jumpX > .9){
                jumpX = (float) .9;
                jumpY = (float) .19;
            }
            else if (jumpX < -.9){
                jumpX = (float) -.9;
                jumpY = (float) .19;
            }

            player.setJumpDirection(jumpX, jumpY);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }
}









