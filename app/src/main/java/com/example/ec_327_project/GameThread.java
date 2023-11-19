package com.example.ec_327_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private final SurfaceHolder surfaceHolder;
    private volatile boolean isRunning;
    private final Player player;

    public GameThread(SurfaceHolder holder, Player player) {
        surfaceHolder = holder;
        isRunning = true;
        this.player = player;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (isRunning) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                if (canvas != null) {
                    synchronized (surfaceHolder) {
                        update();
                        draw(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void update() {
        player.update();
    }

    private void draw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        player.draw(canvas);
    }
}




