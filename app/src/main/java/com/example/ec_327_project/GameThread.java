package com.example.ec_327_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean isRunning;
    private Player player;
    private List<Platform> platforms;
    private List<Platform> barriers;
    private Platform floor;

    private Bitmap backgroundImage;

    public GameThread(SurfaceHolder holder, Player player, Context context) {
        surfaceHolder = holder;
        isRunning = true;
        this.player = player;
        platforms = new ArrayList<>(); // Create a list to hold platforms
        // Add platforms to the list

        int offset = 5200;
        platforms.add(new Platform(300, 1600+offset, 200, 40));//platform1
        platforms.add(new Platform(780, 1600+offset, 200, 40));//platform2
        platforms.add(new Platform(100, 1300+offset, 200, 40));//platform3
        platforms.add(new Platform(980, 1300+offset, 200, 40));//platform4
        platforms.add(new Platform(530, 1000+offset, 200, 40));//platform5
        platforms.add(new Platform(930, 750+offset, 120, 40));//platform6
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
        platforms.add(new Platform(520, -3000+offset, 510, 40));//platform21
        platforms.add(new Platform(135, -3350+offset, 80, 40));//platform22
        platforms.add(new Platform(305, -3700+offset, 80, 40));//platform23
        platforms.add(new Platform(950, -3900+offset, 100, 40));//platform24
        platforms.add(new Platform(725, -4200+offset, 100, 40));//platform25
        platforms.add(new Platform(500, -4400+offset, 40, 150));//platform26
        platforms.add(new Platform(275, -4200+offset, 100, 40));//platform21

        //wall barriers
        barriers = new ArrayList<>();
        barriers.add(new Platform(0, 2000, 20, 10000));//left side
        barriers.add(new Platform(1080, 2000, 20, 10000));//right side

        //floor
        floor = new Platform(0, 1940 + offset, 3000, 160);//floor

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
        player.update(platforms, barriers, floor);
    }

    // Define star positions outside the draw method
    private List<Point> starPositions = new ArrayList<>();
    private boolean starsGenerated = false;

    // Method to generate stars if not already done
    private void generateStars(Canvas canvas) {
        if (!starsGenerated) {
            Random random = new Random();
            int numStars = 100; // Number of stars, you can adjust this value
            for (int i = 0; i < numStars; i++) {
                int x = random.nextInt(canvas.getWidth());
                int y = random.nextInt(canvas.getHeight());
                starPositions.add(new Point(x, y));
            }
            starsGenerated = true;
        }
    }

    // The draw method
    private void draw(Canvas canvas) {
        if (canvas != null) {
            // Draw the night sky background
            Paint backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.BLACK);
            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

            // Draw stars if not already drawn
            generateStars(canvas);

            Paint starPaint = new Paint();
            starPaint.setColor(Color.WHITE);
            starPaint.setStrokeWidth(5);

            // Draw the stars
            for (Point star : starPositions) {
                canvas.drawPoint(star.x, star.y, starPaint);
            }

            // Draw the player
            player.draw(canvas);

            floor.draw(canvas, Color.WHITE);

            // Draw platforms
            for (Platform platform : platforms) {
                platform.draw(canvas, Color.DKGRAY);
            }
        }
    }

}

