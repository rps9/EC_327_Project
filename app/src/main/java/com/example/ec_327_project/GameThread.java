package com.example.ec_327_project;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.graphics.Color;


public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private Player player;

    public GameThread(SurfaceHolder holder, Player player) {
        surfaceHolder = holder;
        isRunning = true;
        this.player = player;
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
        // Update game logic here
        player.update();
    }

    private void draw(Canvas canvas) {
        if (canvas != null) {
            // Clear the canvas
            canvas.drawColor(Color.BLACK);

            // Draw the player
            player.draw(canvas);

            // Add other game elements drawing logic here
            // ...
        }
    }
}




