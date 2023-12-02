package com.example.ec_327_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;

public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private Player player;
    private List<Platform> platforms;
    private List<Platform> barriers;

    private Bitmap backgroundImage;
    private Context context;

    public GameThread(SurfaceHolder holder, Player player, Context context) {
        surfaceHolder = holder;
        isRunning = true;
        this.player = player;
        platforms = new ArrayList<>(); // Create a list to hold platforms
        // Add platforms to the list
        platforms.add(new Platform(0, 1900, 3000, 40));//floor
        platforms.add(new Platform(300, 1600, 200, 40));//platform1
        platforms.add(new Platform(780, 1600, 200, 40));//platform2
        platforms.add(new Platform(100, 1300, 200, 40));//platform3
        platforms.add(new Platform(980, 1300, 200, 40));//platform4
        platforms.add(new Platform(530, 1000, 200, 40));//platform5
        platforms.add(new Platform(930, 850, 120, 40));//platform6
        platforms.add(new Platform(270, 500, 200, 40));//platform7
        platforms.add(new Platform(60, 150, 80, 40));//platform8

        //wall barriers
        barriers = new ArrayList<>();
        barriers.add(new Platform(0, 2000, 20, 5000));//left side
        barriers.add(new Platform(1080,2000, 20,5000));//right side

        backgroundImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.background_image);

    }

    @Override
    public void run() {
        Canvas canvas;
        while (isRunning) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    update();  // Update game logic
                    draw(canvas);  // Draw game elements
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    private void update() {
        player.update(platforms, barriers);
    }

    private void draw(Canvas canvas) {
        if (canvas != null) {
            // Calculate the scaling factors
            float scaleX = (float) canvas.getWidth() / backgroundImage.getWidth();
            float scaleY = (float) canvas.getHeight() / backgroundImage.getHeight();

            // Create a matrix for the scaling operation
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.postScale(scaleX, scaleY);

            // Apply the matrix to the background image
            Bitmap scaledBackground = Bitmap.createBitmap(backgroundImage, 0, 0, backgroundImage.getWidth(), backgroundImage.getHeight(), matrix, true);

            // Draw the scaled background image
            canvas.drawBitmap(scaledBackground, 0, 0, null);

            // Draw the player
            player.draw(canvas);

            // Draw platforms
            for (Platform platform : platforms) {
                platform.draw(canvas);
            }
        }
    }
}
