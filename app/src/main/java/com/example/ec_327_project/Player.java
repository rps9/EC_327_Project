package com.example.ec_327_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Player {
    private float x, y; // Adjust the position as needed
    private final float radius = 20; // Adjust the radius as needed
    private float jumpSpeed = 0; // Adjust the initial jump speed as needed
    private final float gravity = 1; // Adjust the gravity as needed
    private boolean isJumping = false;

    private final float groundLevel = 500; // Adjust the ground level as needed
    private final float maxHeight = 150; // Adjust the max height as needed
    private final float chargeFactor = 0.03f; // Adjust the charge factor for slower charging
    private final long maxChargeTime = 1000; // Maximum time to charge in milliseconds

    private long jumpStartTime; // To track when the jump button is pressed

    public Player() {
        // Initialize player properties
        x = 100;
        y = groundLevel; // Start at the ground level
    }

    public void update() {
        if (isJumping) {
            // If jumping, update the position accordingly
            y += jumpSpeed;
            jumpSpeed += gravity;

            // Add logic to stop jumping when reaching the ground or a certain height
            if (y >= groundLevel) {
                y = groundLevel;
                isJumping = false;
                jumpSpeed = 0;
            }
        }
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void startChargingJump() {
        // Called when the jump button is pressed
        // Reset jump state
        isJumping = false;
        jumpStartTime = System.currentTimeMillis();
    }

    public void releaseJump() {
        // Called when the jump button is released
        // Calculate charge duration, ensuring it doesn't exceed the maximum charge time
        long chargeDuration = Math.min(System.currentTimeMillis() - jumpStartTime, maxChargeTime);

        // Adjust jump height based on the charge duration and charge factor
        float jumpHeight = Math.min(chargeDuration * chargeFactor, maxHeight);

        // Set the jump speed based on the calculated jump height
        jumpSpeed = -jumpHeight;

        // Start jumping
        isJumping = true;
    }
}











