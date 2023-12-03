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

    public GameThread(SurfaceHolder holder, Player player, Context context) {
        surfaceHolder = holder;
        isRunning = true;
        this.player = player;
        platforms = new ArrayList<>(); // Create a list to hold platforms
        // Add platforms to the list

        int offset = +3500;
        platforms.add(new Platform(0, 1900+offset, 3000, 40));//floor
        platforms.add(new Platform(300, 1600+offset, 200, 40));//platform1
        platforms.add(new Platform(780, 1600+offset, 200, 40));//platform2
        platforms.add(new Platform(100, 1300+offset, 200, 40));//platform3
        platforms.add(new Platform(980, 1300+offset, 200, 40));//platform4
        platforms.add(new Platform(530, 1000+offset, 200, 40));//platform5
        platforms.add(new Platform(930, 850+offset, 120, 40));//platform6
        platforms.add(new Platform(270, 500+offset, 200, 40));//platform7
        platforms.add(new Platform(40, 150+offset, 90, 40));//platform8
        platforms.add(new Platform(1000, 100+offset, 200, 40));//platform9
        platforms.add(new Platform(700, -300+offset, 150, 40));//platform10
        platforms.add(new Platform(605, -780+offset, 40, 1000));//wall
        platforms.add(new Platform(1000, -700+offset, 200, 40));//platform11
        platforms.add(new Platform(700, -1100+offset, 150, 40));//platform12
        platforms.add(new Platform(1050, -1600+offset, 100, 40));//platform13
        platforms.add(new Platform(250, -1200+offset, 200, 40));//platform14
        platforms.add(new Platform(20, -1500+offset, 100, 40));//platform15
        platforms.add(new Platform(600, -1900+offset, 200, 40));//platform16
        platforms.add(new Platform(1000, -2300+offset, 200, 40));//platform17
        platforms.add(new Platform(700, -2700+offset, 150, 40));//platform18
        platforms.add(new Platform(400, -2700+offset, 150, 40));//platform19
        platforms.add(new Platform(75, -2700+offset, 200, 40));//platform20
        platforms.add(new Platform(475, -3000+offset, 600, 40));//platform14

        //wall barriers
        barriers = new ArrayList<>();
        barriers.add(new Platform(0, 2000, 20, 10000));//left side
        barriers.add(new Platform(1080,2000, 20,10000));//right side

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
            // Clear the canvas
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
