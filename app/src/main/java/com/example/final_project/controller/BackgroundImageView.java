package com.example.final_project.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import com.example.final_project.R;

public class BackgroundImageView extends View {
    int screenWidth, screenHeight,
            newWidth, newHeight;
    int cloudX1 = 0, cloudX2 = 0, cloudX3 = 0,
            rockX1 = 0, rockX2 = 0, rockX3 = 0,
            birdsX = 0, birdsY = 0;
    Bitmap cloud1, cloud2, cloud3,
            rock1, rock2, rock3,
            birds;
    Handler handler;
    Runnable runnable;
    final long UPDATE_MILLIS = 30;

    public BackgroundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        onCreate();
    }

    public BackgroundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
    }

    private void onCreate() {
        rock1 = BitmapFactory.decodeResource(getResources(), R.drawable.rocks_1);
        rock2 = BitmapFactory.decodeResource(getResources(), R.drawable.rocks_2);
        rock3 = BitmapFactory.decodeResource(getResources(), R.drawable.rocks_3);
        cloud1 = BitmapFactory.decodeResource(getResources(), R.drawable.clouds_1);
        cloud2 = BitmapFactory.decodeResource(getResources(), R.drawable.clouds_2);
        cloud3 = BitmapFactory.decodeResource(getResources(), R.drawable.clouds_3);
        birds = BitmapFactory.decodeResource(getResources(), R.drawable.birds);

        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        float height = cloud1.getHeight();
        float width = cloud1.getWidth();
        float ratio = width / height;
        newHeight = screenHeight;
        newWidth = (int) (ratio * screenHeight);
        cloud1 = Bitmap.createScaledBitmap(cloud1, newWidth, newHeight, false);
        cloud2 = Bitmap.createScaledBitmap(cloud2, newWidth, newHeight, false);
        cloud3 = Bitmap.createScaledBitmap(cloud3, newWidth, newHeight, false);
        rock1 = Bitmap.createScaledBitmap(rock1, newWidth, newHeight, false);
        rock2 = Bitmap.createScaledBitmap(rock2, newWidth, newHeight, false);
        rock3 = Bitmap.createScaledBitmap(rock3, newWidth, newHeight, false);
        birds = Bitmap.createScaledBitmap(birds, newWidth, newHeight, false);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }

    private void drawBackground(Canvas canvas) {
        cloudX1 -= 1;
        if (cloudX1 < -newWidth) {
            cloudX1 = 0;
        }
        canvas.drawBitmap(cloud1, cloudX1, 0, null);
        if (cloudX1 < screenWidth - newWidth) {
            canvas.drawBitmap(cloud1, cloudX1 + newWidth, 0, null);
        }
        cloudX2 -= 2;
        if (cloudX2 < -newWidth) {
            cloudX2 = 0;
        }
        canvas.drawBitmap(cloud2, cloudX2, 0, null);
        if (cloudX2 < screenWidth - newWidth) {
            canvas.drawBitmap(cloud2, cloudX2 + newWidth, 0, null);
        }
        cloudX3 -= 3;
        if (cloudX3 < -newWidth) {
            cloudX3 = 0;
        }
        canvas.drawBitmap(cloud3, cloudX3, 0, null);
        if (cloudX3 < screenWidth - newWidth) {
            canvas.drawBitmap(cloud3, cloudX3 + newWidth, 0, null);
        }

        rockX1 -= 4;
        if (rockX1 < -newWidth) {
            rockX1 = 0;
        }
        canvas.drawBitmap(rock1, rockX1, 0, null);
        if (rockX1 < screenWidth - newWidth) {
            canvas.drawBitmap(rock1, rockX1 + newWidth, 0, null);
        }
        rockX2 -= 5;
        if (rockX2 < -newWidth) {
            rockX2 = 0;
        }
        canvas.drawBitmap(rock2, rockX2, 0, null);
        if (rockX2 < screenWidth - newWidth) {
            canvas.drawBitmap(rock2, rockX2 + newWidth, 0, null);
        }
        rockX3 -= 7;
        if (rockX3 < -newWidth) {
            rockX3 = 0;
        }
        canvas.drawBitmap(rock3, rockX3, 0, null);
        if (rockX3 < screenWidth - newWidth) {
            canvas.drawBitmap(rock3, rockX3 + newWidth, 0, null);
        }

        birdsX += 4;
        if (birdsX > screenWidth) {
            birdsX = screenWidth - newWidth;
        }
        birdsY -= 1;
        if (birdsY < -newHeight) {
            birdsY = 0;
        }
        canvas.drawBitmap(birds, birdsX, birdsY, null);
        if (birdsX > screenWidth - newWidth) {
            canvas.drawBitmap(birds, birdsX - newWidth, birdsY + newHeight, null);
        }

        handler.postDelayed(runnable, UPDATE_MILLIS);
    }
}
