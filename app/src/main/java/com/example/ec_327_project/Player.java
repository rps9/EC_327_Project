package com.example.ec_327_project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;
import android.content.Context;

import java.util.List;

public class Player {
    private float x, y, jumpX, jumpY, platformY;  // Adjust the position as needed
    private int width = 80;   //hit box player width
    private int height = 160;   //hit box player height
    private float jumpSpeedX = 0;  // Adjust the initial jump speed as needed
    private float jumpSpeedY = 0;
    private float gravity = 0.5f;  // Adjust the gravity as needed
    private boolean isJumping = false;
    private float terminalVelocity = 50;

    //private boolean hold;

    private float groundLevel = 1920-3*height/4;  // Adjust the ground level as needed
    private float maxHeight = 20;  // Adjust the max height as needed
    private float chargeFactor = 0.01f;  // Adjust the charge factor for slower charging

    public long jumpStartTime;  // To track when the jump button is pressed

    //private long hold_time;

    private List<Platform> platformList;

    private List<Platform> barrierList;
    private Platform floor;
    private MainActivity context;



    private Bitmap[] playerBitmaps;
    public int currentId = 0;

    public static final int  player_1=R.mipmap.player_1;
    public static final int  player_2=R.mipmap.player_2;
    public static final int  player_3=R.mipmap.player_3;
    public static final int  player_4=R.mipmap.player_4;

    public Player(MainActivity context) {    //
    //public Player() {
        // Initialize player properties
        this.context = context;
        playerBitmaps = new Bitmap[4];     //
        playerBitmaps[0]=BitmapFactory.decodeResource(context.getResources(),player_1);  //
        playerBitmaps[1]=BitmapFactory.decodeResource(context.getResources(),player_2);  //
        playerBitmaps[2]=BitmapFactory.decodeResource(context.getResources(),player_3);  //
        playerBitmaps[3]=BitmapFactory.decodeResource(context.getResources(),player_4);  //
        playerBitmaps[0] = Bitmap.createScaledBitmap(playerBitmaps[0], width, height, false);
        playerBitmaps[1] = Bitmap.createScaledBitmap(playerBitmaps[0], width, height-20, false);
        playerBitmaps[2] = Bitmap.createScaledBitmap(playerBitmaps[0], width, height-40, false);
        playerBitmaps[3] = Bitmap.createScaledBitmap(playerBitmaps[0], width, height-60, false);

        x = 750;
        y = 300;
        //y = groundLevel;// Start at the ground level
        //platformY = 0; //set this the same as the offset added in the platform class for changing player start position

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
        Platform floor = this.floor;
        floor.platformUpdate(platformY+floor.getOriginal_y());
        if (jumpSpeedY > 0 && isCollidingWithBottomSide(floor)) {
            Log.d("CollisionDetection", "Bottom side collision detected!");

            jumpSpeedY = 0;
            jumpSpeedX = 0;
            isJumping = false;

            floor.platformUpdate(y + floor.getHeight() / 2 + (height / 2));
        }
        for (Platform platform : platformList){
            platform.platformUpdate(platformY+platform.getOriginal_y());
            if (jumpSpeedY <= 0 && isCollidingWithTopSide(platform) && !isCollidingWithRightSide(platform) && !isCollidingWithLeftSide(platform)){
                Log.d("CollisionDetection", "Top side collision detected!");
                jumpSpeedY = 0;
                platform.platformUpdate(y - platform.getHeight() / 2 - (height / 2));
                //y = platform.bottomY() + height / 2; // Adjust the position to prevent overlap

            } else if (jumpSpeedY > 0 && isCollidingWithBottomSide(platform) && !isCollidingWithRightSide(platform) && !isCollidingWithLeftSide(platform)) {
                Log.d("CollisionDetection", "Bottom side collision detected!");

                jumpSpeedY = 0;
                jumpSpeedX = 0;
                isJumping = false;
                //y = platform.topY() - height / 2; // Adjust the position to prevent overlap

                platform.platformUpdate(y + platform.getHeight() / 2 + (height / 2));
            }

            if (isCollidingWithLeftSide(platform) && jumpSpeedY !=0) {
                Log.d("CollisionDetection", "Left side collision detected!");
                jumpSpeedX = 0;
                x = platform.rightX() + width / 2;

            } else if (isCollidingWithRightSide(platform) && jumpSpeedY !=0) {
                Log.d("CollisionDetection", "Right side collision detected!");
                jumpSpeedX = 0;
                x = platform.leftX() - width / 2;

            }
        }
        for (Platform barrier : barrierList) {
            if (isCollidingWithLeftSide(barrier)) {
                Log.d("CollisionDetection", "Left side barrier collision detected!");
                jumpSpeedX = 0;
                x = barrier.rightX() + width / 2;

            } else if (isCollidingWithRightSide(barrier)) {
                Log.d("CollisionDetection", "Right side barrier collision detected!");
                jumpSpeedX = 0;
                x = barrier.leftX() - width / 2;
            }
        }
    }

    private boolean isCollidingWithLeftSide(Platform platform) {
        return leftX() < platform.rightX() && rightX() > platform.rightX() &&
                bottomY() > platform.topY() && topY() < platform.bottomY() &&
                platform.rightX() < leftX() + width/4;

    }

    private boolean isCollidingWithRightSide(Platform platform) {
        return rightX() > platform.leftX() && leftX() < platform.leftX() &&
                bottomY() > platform.topY() && topY() < platform.bottomY() &&
                platform.leftX() > rightX() - width/4;

    }


    private boolean isCollidingWithBottomSide(Platform platform)
    {
        return bottomY() > platform.topY() && topY()< platform.topY() &&
                rightX() > platform.leftX() && leftX() < platform.rightX()
                &&platform.bottomY() > bottomY();
    }

    private boolean isCollidingWithTopSide(Platform platform) {
        return topY() < platform.bottomY() && bottomY() > platform.bottomY() - platform.getHeight() &&
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
    public void update(List<Platform> platforms, List<Platform> barriers, Platform floor) {
        this.platformList = platforms;
        this.barrierList = barriers;
        this.floor = floor;
        if (platformY > 300)
        {
            launchEndScreen(context);
            return;
        }
        if (isJumping) {
            // If jumping, update the position accordingly
            platformY -= jumpSpeedY*jumpY;

            Log.d("Platform Y", "Y value" + platformY);


            x += jumpSpeedX*jumpX;
            if (jumpSpeedY + gravity < terminalVelocity) {
                jumpSpeedY += gravity;
            }
            else{
                jumpSpeedY = terminalVelocity;
            }
            currentId = 0;  //

            //Log.d("Speed Value", "jumpSpeedY: " + jumpSpeedY);

            checkPlatformCollisions();
        }
    }
    private void launchEndScreen(Context context) {
        // Code to transition to the EndScreen, for example:
        Intent intent = new Intent(context, EndScreen.class);
        context.startActivity(intent);
        // Make sure to finish the current activity or handle the transition appropriately
        // depending on your application flow
        if (context instanceof MainActivity) {
            ((MainActivity) context).finish();
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













