package com.example.ec_327_project;

import android.os.Bundle;
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
    private long hold_time;

    private boolean hold;
    private Player player;
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

//                hold = true;
//                while(hold){
//                    hold_time=System.currentTimeMillis()-player.jumpStartTime;
//                    if(hold_time>0 && hold_time<=500){
//                        player.currentId=1;
//                        if(event.getAction()==1){
//                            hold = false;
//                        }
//                    }
//                    else if(hold_time>500 && hold_time<=1000){
//                        player.currentId=2;
//                        if(event.getAction()==1){
//                            hold = false;
//                        }
//
//                    }
//                    else if(hold_time>1000){
//                        player.currentId=3;
//                        if(event.getAction()==1){
//                            hold = false;
//                        }
//                    }
//            }
                break;
            case MotionEvent.ACTION_UP:
                player.releaseJump();
                break;
        }
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float jumpY = event.values[1] / (float)9.809989;
            float jumpX = event.values[0] / (float)9.809989;

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









