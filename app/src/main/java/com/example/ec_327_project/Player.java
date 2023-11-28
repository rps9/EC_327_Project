package com.example.ec_327_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

public class Player {
    private float x, y, jumpX, jumpY;  // Adjust the position as needed
    private float width = 40;
    private float height = 80;
    private float jumpSpeedX = 0;  // Adjust the initial jump speed as needed
    private float jumpSpeedY = 0;
    private float gravity = 1;  // Adjust the gravity as needed
    private boolean isJumping = false;

    private float groundLevel = 1000;  // Adjust the ground level as needed
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
                // Collision with the left side of the platform
                jumpSpeedX = 0; // Stop horizontal movement
                x = platform.leftX() - width / 2; // Adjust player position to prevent overlap
            } else if (isCollidingWithRightSide(platform)) {
                // Collision with the right side of the platform
                jumpSpeedX = 0; // Stop horizontal movement
                x = platform.rightX() + width / 2; // Adjust player position to prevent overlap
            }

            if (isCollidingWithTopSide(platform)) {
                // Collision with the top side of the platform
                jumpSpeedY = 0; // Stop vertical movement
                jumpSpeedX = 0;
                isJumping = false;
                y = platform.topY() - height / 2; // Adjust player position to prevent overlap
            } else if (isCollidingWithBottomSide(platform)) {
                // Collision with the bottom side of the platform
                jumpSpeedY = 0; // Stop vertical movement
                jumpSpeedX = 0;
                y = platform.bottomY() + height / 2; // Adjust player position to prevent overlap
            }

        }
    }

    private boolean isCollidingWithLeftSide(Platform platform) {
        return rightX() > platform.leftX() && leftX() < platform.leftX() &&
                topY() < platform.bottomY() && bottomY() > platform.topY();
    }

    private boolean isCollidingWithRightSide(Platform platform) {
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

            // Adjust jump height based on the charge duration and charge factor
            float jumpHeight = Math.min(chargeDuration * chargeFactor, maxHeight);

            // Set the jump speed based on the calculated jump height
            jumpSpeedX = -jumpHeight;
            jumpSpeedY = -jumpHeight;

            // Start jumping
            isJumping = true;
        }
    }
}













