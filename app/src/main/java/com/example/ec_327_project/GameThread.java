package com.example.ec_327_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;



public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private Player player;
    private List<Platform> platforms;

    private Bitmap backgroundImage;
    private Context context;

    public GameThread(SurfaceHolder holder, Player player, Context context) {
        surfaceHolder = holder;
        isRunning = true;
        this.player = player;
        platforms = new ArrayList<>(); // Create a list to hold platforms
        // Add platforms to the list
        int offset = +1200;
        platforms.add(new Platform(10, 2000+offset, 20, 10000));//left side
        platforms.add(new Platform(1070,2000+offset, 20,10000));//right side
        platforms.add(new Platform(0, 1900+offset, 3000, 40));//floor
        platforms.add(new Platform(300, 1600+offset, 200, 40));//platform1
        platforms.add(new Platform(780, 1600+offset, 200, 40));//platform2
        platforms.add(new Platform(100, 1300+offset, 200, 40));//platform3
        platforms.add(new Platform(980, 1300+offset, 200, 40));//platform4
        platforms.add(new Platform(530, 1000+offset, 200, 40));//platform5
        platforms.add(new Platform(930, 850+offset, 120, 40));//platform6
        platforms.add(new Platform(270, 500+offset, 200, 40));//platform7
        platforms.add(new Platform(60, 150+offset, 80, 40));//platform8
        platforms.add(new Platform(1000, 100+offset, 200, 40));//platform9
        platforms.add(new Platform(800, -300+offset, 150, 40));//platform10

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
        player.update(platforms);
    }

    private void draw(Canvas canvas) {
        if (canvas != null) {
            // Clear the canvas
            //canvas.drawBitmap(backgroundImage, 0, 0, null);
            canvas.drawColor(Color.GRAY);
            // Draw the player
            player.draw(canvas);

            // Draw platforms
            for (Platform platform : platforms) {
                platform.draw(canvas);
            }
        }
    }


}





