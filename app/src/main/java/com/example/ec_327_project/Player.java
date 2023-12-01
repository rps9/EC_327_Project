package com.example.ec_327_project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.util.List;

public class Player {
    private float x, y, jumpX, jumpY, platformY;  // Adjust the position as needed
    private int width = 80;   //hit box player width
    private int height = 160;   //hit box player height
    private float jumpSpeedX = 0;  // Adjust the initial jump speed as needed
    private float jumpSpeedY = 0;
    private float gravity = 1;  // Adjust the gravity as needed
    private boolean isJumping = false;

    //private boolean hold;

    private float groundLevel = 1920-3*height/4;  // Adjust the ground level as needed
    private float maxHeight = 30;  // Adjust the max height as needed
    private float chargeFactor = 0.02f;  // Adjust the charge factor for slower charging

    public long jumpStartTime;  // To track when the jump button is pressed

    //private long hold_time;

    private List<Platform> platformList;



    private Bitmap[] playerBitmaps;
    public int currentId = 0;

    public static final int  player_1=R.mipmap.player_1;
    public static final int  player_2=R.mipmap.player_2;
    public static final int  player_3=R.mipmap.player_3;
    public static final int  player_4=R.mipmap.player_4;

    public Player(MainActivity context) {    //
    //public Player() {
        // Initialize player properties

        playerBitmaps = new Bitmap[4];     //
        playerBitmaps[0]=BitmapFactory.decodeResource(context.getResources(),player_1);  //
        playerBitmaps[1]=BitmapFactory.decodeResource(context.getResources(),player_2);  //
        playerBitmaps[2]=BitmapFactory.decodeResource(context.getResources(),player_3);  //
        playerBitmaps[3]=BitmapFactory.decodeResource(context.getResources(),player_4);  //
        playerBitmaps[0] = Bitmap.createScaledBitmap(playerBitmaps[0], width, height, false);
        playerBitmaps[1] = Bitmap.createScaledBitmap(playerBitmaps[0], width, height-10, false);
        playerBitmaps[2] = Bitmap.createScaledBitmap(playerBitmaps[0], width, height-20, false);
        playerBitmaps[3] = Bitmap.createScaledBitmap(playerBitmaps[0], width, height-30, false);


        //x = 530;
        x = 90;
        //y = groundLevel;
        y = 1200;// Start at the ground level
        platformY = 0;
    }

    public float leftX() {
        return x - width / 2;
    }

    public float rightX() {
        return x + width / 2;
    }

    public float topY() {
        return y - height / 2;
    }

    public float bottomY() {
        return y + height / 2;
    }

    //public int getWidth() { return }
    private void checkPlatformCollisions() {
        for (Platform platform : platformList){
            platform.platformUpdate(platformY+platform.getOriginal_y());
            if (isCollidingWithLeftSide(platform)) {
                //Log.d("CollisionDetection", "Left side collision detected!");
                jumpSpeedX = 0;
                x = platform.rightX() + width / 2;

            } else if (isCollidingWithRightSide(platform)) {
                //Log.d("CollisionDetection", "Right side collision detected!");
                jumpSpeedX = 0;
                x = platform.leftX() - width / 2;

            }

            if (jumpSpeedY <= 0 && isCollidingWithBottomSide(platform)) {
                //Log.d("CollisionDetection", "Top side collision detected!");
                jumpSpeedY = 0;
                y = platform.bottomY() + height / 2; // Adjust the position to prevent overlap

            } else if (jumpSpeedY > 0 && isCollidingWithTopSide(platform)) {
                //Log.d("CollisionDetection", "Bottom side collision detected!");
                jumpSpeedY = 0;
                jumpSpeedX = 0;
                isJumping = false;
                y = platform.topY() - height / 2; // Adjust the position to prevent overlap

            }
        }
    }



    private boolean isCollidingWithRightSide(Platform platform) { //left side of player
        return rightX() > platform.leftX() && leftX() < platform.leftX() &&
                topY() < platform.bottomY() && bottomY() > platform.topY();
    }

    private boolean isCollidingWithLeftSide(Platform platform) {
        return leftX() < platform.rightX() && rightX() > platform.rightX() &&
                topY() < platform.bottomY() && bottomY() > platform.topY();
    }


    private boolean isCollidingWithTopSide(Platform platform) {
        return bottomY() > platform.topY() && topY() < platform.topY() &&
                rightX() > platform.leftX() && leftX() < platform.rightX();
    }

    private boolean isCollidingWithBottomSide(Platform platform) {
        return topY() < platform.bottomY() && bottomY() > platform.bottomY() &&
                rightX() > platform.leftX() && leftX() < platform.rightX();
    }





    private boolean isCollidingWith(Platform platform) {
        // Basic collision detection logic
        return rightX() > platform.leftX() &&
                leftX() < platform.rightX() &&
                bottomY() > platform.topY() &&
                topY() < platform.bottomY();
    }
    public void setJumpDirection(float jumpX, float jumpY) {
        if (!isJumping) {
            // Set the jump direction only if the player is not already jumping
            this.jumpX = jumpX;
            this.jumpY = jumpY;
        }
    }
    public void update(List<Platform> platforms) {
        this.platformList = platforms;
        if (isJumping) {
            // If jumping, update the position accordingly
            platformY -= jumpSpeedY*jumpY;
            //Log.d("Platform Y", "Y value" + platformY);
            x += jumpSpeedX*jumpX;
            jumpSpeedY += gravity;
            currentId = 0;  //

            //Log.d("Speed Value", "jumpSpeedY: " + jumpSpeedY);


            checkPlatformCollisions();
        }
    }


    public void draw(Canvas canvas) {
        Bitmap currentImage = playerBitmaps[currentId];

        // Set the transformation matrix for flipping the image horizontally
        Matrix matrix = new Matrix();
        if (jumpX < 0) {
            matrix.setScale(-1, 1); // Flip horizontally
            matrix.postTranslate(x + currentImage.getWidth() / 2, y - currentImage.getHeight() + height / 2);
        } else {
            matrix.setTranslate(x - currentImage.getWidth() / 2, y - currentImage.getHeight() + height / 2);
        }

        // Apply the transformation matrix to the canvas
        canvas.drawBitmap(currentImage, matrix, null);
    }


    public void startChargingJump() {
        if (!isJumping) {
            // Called when the jump button is pressed, but only if the player is not already jumping
            // Reset jump state
            isJumping = false;
            jumpStartTime = System.currentTimeMillis();
        }
    }

    public void releaseJump() {
        if (!isJumping) {
            // Called when the jump button is released
            // Calculate charge duration, ensuring it doesn't exceed the maximum charge time
            long chargeDuration = System.currentTimeMillis() - jumpStartTime;
            float jumpHeight = chargeFactor*chargeDuration + maxHeight/2;
            currentId = (int) ((jumpHeight/maxHeight) * (playerBitmaps.length - 1));   //

            if (jumpHeight > maxHeight){
                jumpHeight = maxHeight;
            }

            // Set the jump speed based on the calculated jump height
            jumpSpeedX = -jumpHeight;
            jumpSpeedY = -jumpHeight;

            // Start jumping
            isJumping = true;
        }
    }
}













