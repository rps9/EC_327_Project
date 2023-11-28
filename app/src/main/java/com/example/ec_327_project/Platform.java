package com.example.ec_327_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Platform {
    private float x, y;
    private float width, height;

    public Platform(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Canvas canvas) {
        // Draw the obstacle as a rectangle
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, paint);
    }

    public float topY() {
        return y - height / 2;
    }

    public float bottomY() {
        return y + height / 2;
    }

    public float leftX() {
        return x - width / 2;
    }

    public float rightX() {
        return x + width / 2;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

}

