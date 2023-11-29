package com.example.ec_327_project;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.graphics.Color;
import java.util.ArrayList;
import java.util.List;

public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private Player player;
    private List<Platform> platforms;

    public GameThread(SurfaceHolder holder, Player player) {
        surfaceHolder = holder;
        isRunning = true;
        this.player = player;
        platforms = new ArrayList<>(); // Create a list to hold platforms
        // Add platforms to the list
        platforms.add(new Platform(10, 2000, 20, 5000));//left side
        platforms.add(new Platform(1070,2000, 20,5000));//right side
        platforms.add(new Platform(0, 1900, 3000, 40));//floor
        platforms.add(new Platform(300, 1600, 200, 40));//testing platform
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
            canvas.drawColor(Color.BLACK);

            // Draw the player
            player.draw(canvas);

            // Draw platforms
            for (Platform platform : platforms) {
                platform.draw(canvas);
            }
        }
    }


}





