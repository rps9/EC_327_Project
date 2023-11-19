package com.example.ec_327_project;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private GameThread gameThread;
    private final Player player = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SurfaceView gameSurfaceView = findViewById(R.id.gameView);
        gameSurfaceView.getHolder().addCallback(this);

        gameThread = new GameThread(gameSurfaceView.getHolder(), player);
        gameThread.start();
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
                // Add visual feedback or any necessary actions when starting to charge
                break;
            case MotionEvent.ACTION_UP:
                player.releaseJump();
                // Execute the jump based on the charged duration
                // Clear visual feedback or reset any charging indicators
                break;
        }
        return true;
    }

}






