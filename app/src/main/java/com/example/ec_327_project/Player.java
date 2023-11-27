package com.example.ec_327_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

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

    public Player() {
        // Initialize player properties
        x = 100;
        y = groundLevel;  // Start at the ground level
    }

    public void setJumpDirection(float jumpX, float jumpY) {
        if (!isJumping) {
            // Set the jump direction only if the player is not already jumping
            this.jumpX = jumpX;
            this.jumpY = jumpY;
        }
    }
    public void update() {
        if (isJumping) {
            // If jumping, update the position accordingly
            y += jumpSpeedY*jumpY;
            x += jumpSpeedX*jumpX;
            jumpSpeedY += gravity;

            // Add logic to stop jumping when reaching the ground or a certain height
            if (y >= groundLevel) {
                y = groundLevel;
                isJumping = false;
                jumpSpeedY = 0;
                jumpSpeedX = 0;
            }
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













