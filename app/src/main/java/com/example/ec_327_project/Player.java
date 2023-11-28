package com.example.ec_327_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.List;

public class Player {
    private float x, y, jumpX, jumpY;  // Adjust the position as needed
    private float width = 40;
    private float height = 80;
    private float jumpSpeedX = 0;  // Adjust the initial jump speed as needed
    private float jumpSpeedY = 0;
    private float gravity = 1;  // Adjust the gravity as needed
    private boolean isJumping = false;

    private float groundLevel = 1850;  // Adjust the ground level as needed
    private float maxHeight = 30;  // Adjust the max height as needed
    private float chargeFactor = 0.02f;  // Adjust the charge factor for slower charging

    private long jumpStartTime;  // To track when the jump button is pressed
    private List<Platform> platformList;

    public Player() {
        // Initialize player properties
        x = 300;
        y = groundLevel;  // Start at the ground level
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
    private void checkPlatformCollisions() {
        for (Platform platform : platformList) {
            if (isCollidingWithLeftSide(platform)) {
                //Log.d("CollisionDetection", "Left side collision detected!");
                jumpSpeedX = 0;
                x = platform.rightX() + width / 2;
            } else if (isCollidingWithRightSide(platform)) {
                //Log.d("CollisionDetection", "Right side collision detected!");
                jumpSpeedX = 0;
                x = platform.leftX() - width / 2;
            }

            if (jumpSpeedY >= 0 && isCollidingWithBottomSide(platform)) {
                //Log.d("CollisionDetection", "Bottom side collision detected!");
                jumpSpeedY = 0;
                jumpSpeedX = 0;
                isJumping = false;
                y = platform.topY() - height / 2; // Adjust the position to prevent overlap
            } else if (jumpSpeedY < 0 && isCollidingWithTopSide(platform)) {
                //Log.d("CollisionDetection", "Top side collision detected!");
                jumpSpeedY = 0;
                y = platform.bottomY() + height / 2; // Adjust the position to prevent overlap
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
            y += jumpSpeedY*jumpY;
            x += jumpSpeedX*jumpX;
            jumpSpeedY += gravity;

            Log.d("Speed Value", "jumpSpeedY: " + jumpSpeedY);


            checkPlatformCollisions();
        }
    }


    public float getY(){
        return y;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, paint);
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













