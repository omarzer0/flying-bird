package com.azapps.flyingbird;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class GameView extends View {

    // Canvas
    private int canvasWidth;
    private int canvasHeight;

    // bird
    private Bitmap[] bird = new Bitmap[2];
    private int birdX = 10;
    private int birdY;
    private int birdSpeed;

    // blue ball
    private int blueX;
    private int blueY;
    private int blueSpeed = 15;
    private Paint bluePaint = new Paint();

    // black ball
    private int blackX;
    private int blackY;
    private int blackSpeed = 20;
    private Paint blackPaint = new Paint();

    //backgroundImage
    private Bitmap backgroundImage;
    // score
    private int score;
    private Paint scorePaint = new Paint();

    // level
    private Paint levelPaint = new Paint();
    // life
    private int lifeCount;
    private Bitmap[] life = new Bitmap[2];

    // Status Check
    private boolean isTouched = false;

    public GameView(Context context) {
        super(context);
        bird[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bird1);
        bird[1] = BitmapFactory.decodeResource(getResources(), R.drawable.bird2);

        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.bg);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        backgroundImage = Bitmap.createScaledBitmap(backgroundImage, width, height, false);

        bluePaint.setColor(Color.BLUE);
        scorePaint.setAntiAlias(false);

        blackPaint.setColor(Color.BLACK);
        blackPaint.setAntiAlias(false);

        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(60);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        levelPaint.setColor(Color.DKGRAY);
        levelPaint.setTextSize(60);
        levelPaint.setTypeface(Typeface.DEFAULT_BOLD);
        levelPaint.setTextAlign(Paint.Align.CENTER);
        levelPaint.setAntiAlias(true);

        life[0] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
        life[1] = BitmapFactory.decodeResource(getResources(), R.drawable.heart_g);

        // first position
        birdY = 500;
        score = 0;
        lifeCount = 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        canvas.drawBitmap(backgroundImage, 0, 0, null);

        // Bird
        int minBirdY = bird[0].getHeight();
        int maxBirdY = canvasHeight - minBirdY * 3;
        birdY += birdSpeed;
        if (birdY < minBirdY) birdY = minBirdY;

        if (birdY > maxBirdY) birdY = maxBirdY;
        birdSpeed += 2;

        if (isTouched) {
            canvas.drawBitmap(bird[1], birdX, birdY, null);
            isTouched = false;
        } else {
            canvas.drawBitmap(bird[0], birdX, birdY, null);
        }

        // blue ball
        blueX -= blueSpeed;
        if (hitCheck(blueX, blueY)) {
            score += 10;
            blueX = -100;
        }
        if (blueX < 0) {
            blueX = canvasWidth + 20;
            blueY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY) + minBirdY);
        }
        canvas.drawCircle(blueX, blueY, 25, bluePaint);

        // black ball
        blackX -= blackSpeed;
        if (hitCheck(blackX, blackY)){
            blackX = -100;
            lifeCount --;
            if (lifeCount == 0){
                // GameOver
                Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
            }
        }
        if (blackX < 0 ){
            blackX = canvasWidth + 200;
            blackY = (int) Math.floor(Math.random() * (maxBirdY - minBirdY) + minBirdY);
        }
        canvas.drawCircle(blackX,blackY,30,blackPaint);

        // Score
        canvas.drawText("Score : "+score, 24, 90, scorePaint);
        // Level
        canvas.drawText("Lv.1", canvas.getWidth() / 2, 90, levelPaint);

        // Life
        for (int i = 1; i <= 3 ; i++) {
            int x = (getWidth() - (120 * i));
            int y = 40;

            if (i <= lifeCount){
                canvas.drawBitmap(life[0], x,y, null);
            }else{
                canvas.drawBitmap(life[1], x,y, null);
            }
        }

//        canvas.drawBitmap(life[0], getWidth() - 120, 40, null);
//        canvas.drawBitmap(life[0], getWidth() - 240, 40, null);
//        canvas.drawBitmap(life[0], getWidth() - 360, 40, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            isTouched = true;
            birdSpeed = -25;
        }
        return true;
    }

    public boolean hitCheck(int x, int y) {
        if (birdX < x && x < (birdX + bird[0].getWidth()) &&
                birdY < y && y < (birdY + bird[0].getHeight())) {
            return true;
        }
        return false;
    }
}
