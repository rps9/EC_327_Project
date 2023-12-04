package com.example.ec_327_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

public class Platform {
    private float x, y, original_y;
    private float width, height;
    private boolean leftCheck, rightCheck, bottomCheck, topCheck;

    public Platform(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.original_y = y;
    }

    public void draw(Canvas canvas, int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(x - width / 2, y - height / 2, x + width / 2, y + height / 2, paint);
        //Log.d("Platform Draw", "Y Value: " + y);
    }

    public void draw(Canvas canvas, int flagColor, int poleColor, float flagWidth, float flagHeight) {
        float poleWidth = this.width;
        float poleHeight = this.height;
        float x = this.x;
        float y = this.y;

        Paint flagPaint = new Paint();
        flagPaint.setColor(flagColor);

        // Draw pole
        Paint polePaint = new Paint();
        polePaint.setColor(poleColor);
        canvas.drawRect(x - poleWidth / 2, y, x + poleWidth / 2, y + poleHeight, polePaint);

        // Draw flag (triangle)
        Path flagPath = new Path();
        flagPath.moveTo(x - poleWidth / 2, y + poleHeight / 2);
        flagPath.lineTo(x - poleWidth / 2 - flagWidth, y - poleHeight / 2 + 3*flagHeight / 2);
        flagPath.lineTo(x - poleWidth / 2, y - poleHeight / 2 + flagHeight);
        flagPath.close();
        canvas.drawPath(flagPath, flagPaint);
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

    public float getOriginal_y() {return original_y;}

    public void platformUpdate(float y){
        this.y = y ;
    }

    public void checkLeft(boolean leftCheck) {this.leftCheck = leftCheck;}
    public void checkTop(boolean topCheck) {this.topCheck = topCheck;}
    public void checkRight(boolean rightCheck) {this.rightCheck = rightCheck;}
    public void checkBottom(boolean bottomCheck) {this.bottomCheck = bottomCheck;}
}

