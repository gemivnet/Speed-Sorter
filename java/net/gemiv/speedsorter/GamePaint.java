package net.gemiv.speedsorter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;

public class GamePaint {

    int x = 0, y = 0;

    private static GamePaint instance;
    public static GamePaint getInstance() {
        if (instance == null) {
            instance = new GamePaint();
        }
        return instance;
    }

    Bitmap combobg, greensquare, greencircle, greentriangle, orangesquare, orangecircle, orangetriangle, bluesquare, bluecircle, bluetriangle, bg, blt, bluebin, orangebin, greenbin, squareSelected, circleSelected, triangleSelected;

    // Bitmaps for Title Screen
    Bitmap titleBackground, titleBelt, noSound, noMusic, lefty;

    // Bitmaps for Level Selection
    Bitmap levelBackground, levelUnlocked, levelLocked, back;

    public void loadBitmaps(Context context, int width, int height) {
        // Load Bitmaps for Title Screen
        titleBackground = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.titlebackground), width, height, false).copy(Bitmap.Config.ARGB_8888, true);
        titleBelt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.titlebelt), width, (int)(height * .3), false).copy(Bitmap.Config.ARGB_8888, true);
        noSound = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.nosound), (int)(37 * width / 480), (int)(32 * height / 270), false).copy(Bitmap.Config.ARGB_8888, true);
        noMusic = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.nomusic), (int)(37 * width / 480), (int)(32 * height / 270), false).copy(Bitmap.Config.ARGB_8888, true);
        lefty = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.lefty), (int)(37 * width / 480), (int)(32 * height / 270), false).copy(Bitmap.Config.ARGB_8888, true);

        // Load Bitmaps for Level Selection
        levelBackground = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.levelbackground), width, height, false).copy(Bitmap.Config.ARGB_8888, true);
        levelUnlocked = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.levelunlocked), width / 11, width / 11, false).copy(Bitmap.Config.ARGB_8888, true);
        levelLocked = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.levellocked), width / 11, width / 11, false).copy(Bitmap.Config.ARGB_8888, true);
        back = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.back), 65 * width / 480, 28 * height / 270, false).copy(Bitmap.Config.ARGB_8888, true);

        // Load Bitmaps for Game
        combobg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.combobg), (int)(width * .15), height, false).copy(Bitmap.Config.ARGB_8888, true);
        bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.background), (int)(width * .85), height, false).copy(Bitmap.Config.ARGB_8888, true);
        blt = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.conveyor), (int)(width * .85), (int)(height / 2 *.6), false).copy(Bitmap.Config.ARGB_8888, true);
        greensquare = BitmapFactory.decodeResource(context.getResources(), R.drawable.greensquare).copy(Bitmap.Config.ARGB_8888, true);
        greentriangle = BitmapFactory.decodeResource(context.getResources(), R.drawable.greentriangle).copy(Bitmap.Config.ARGB_8888, true);
        greencircle = BitmapFactory.decodeResource(context.getResources(), R.drawable.greencircle).copy(Bitmap.Config.ARGB_8888, true);
        orangesquare = BitmapFactory.decodeResource(context.getResources(), R.drawable.orangesquare).copy(Bitmap.Config.ARGB_8888, true);
        orangecircle = BitmapFactory.decodeResource(context.getResources(), R.drawable.orangecircle).copy(Bitmap.Config.ARGB_8888, true);
        orangetriangle = BitmapFactory.decodeResource(context.getResources(), R.drawable.orangetriangle).copy(Bitmap.Config.ARGB_8888, true);
        bluesquare = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluesquare).copy(Bitmap.Config.ARGB_8888, true);
        bluecircle = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluecircle).copy(Bitmap.Config.ARGB_8888, true);
        bluetriangle = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluetriangle).copy(Bitmap.Config.ARGB_8888, true);
        bluebin = BitmapFactory.decodeResource(context.getResources(), R.drawable.bluebin).copy(Bitmap.Config.ARGB_8888, true);
        orangebin = BitmapFactory.decodeResource(context.getResources(), R.drawable.orangebin).copy(Bitmap.Config.ARGB_8888, true);
        greenbin = BitmapFactory.decodeResource(context.getResources(), R.drawable.greenbin).copy(Bitmap.Config.ARGB_8888, true);
        squareSelected = BitmapFactory.decodeResource(context.getResources(), R.drawable.squareselected).copy(Bitmap.Config.ARGB_8888, true);
        circleSelected = BitmapFactory.decodeResource(context.getResources(), R.drawable.circleselected).copy(Bitmap.Config.ARGB_8888, true);
        triangleSelected = BitmapFactory.decodeResource(context.getResources(), R.drawable.triangleselected).copy(Bitmap.Config.ARGB_8888, true);
    }

    Bitmap[][] s;
    Bitmap[] b;
    public void updateScales(int width, int height, int w, int binW, int binH) {
        s = new Bitmap[9][4];
        b = new Bitmap[4];
        s[1][1] = Bitmap.createScaledBitmap(greensquare, width, height, false);
        s[1][2] = Bitmap.createScaledBitmap(greencircle, width, height, false);
        s[1][3] = Bitmap.createScaledBitmap(greentriangle, width, height, false);
        s[2][1] = Bitmap.createScaledBitmap(orangesquare, width, height, false);
        s[2][2] = Bitmap.createScaledBitmap(orangecircle, width, height, false);
        s[2][3] = Bitmap.createScaledBitmap(orangetriangle, width, height, false);
        s[3][1] = Bitmap.createScaledBitmap(bluesquare, width, height, false);
        s[3][2] = Bitmap.createScaledBitmap(bluecircle, width, height, false);
        s[3][3] = Bitmap.createScaledBitmap(bluetriangle, width, height, false);
        s[7][1] = Bitmap.createScaledBitmap(squareSelected, width, height, false);
        s[7][2] = Bitmap.createScaledBitmap(circleSelected, width, height, false);
        s[7][3] = Bitmap.createScaledBitmap(triangleSelected, width, height, false);
        s[4][1] = Bitmap.createScaledBitmap(greensquare, w, w, false);
        s[4][2] = Bitmap.createScaledBitmap(greencircle, w, w, false);
        s[4][3] = Bitmap.createScaledBitmap(greentriangle, w, w, false);
        s[5][1] = Bitmap.createScaledBitmap(orangesquare, w, w, false);
        s[5][2] = Bitmap.createScaledBitmap(orangecircle, w, w, false);
        s[5][3] = Bitmap.createScaledBitmap(orangetriangle, w, w, false);
        s[6][1] = Bitmap.createScaledBitmap(bluesquare, w, w, false);
        s[6][2] = Bitmap.createScaledBitmap(bluecircle, w, w, false);
        s[6][3] = Bitmap.createScaledBitmap(bluetriangle, w, w, false);
        s[8][1] = Bitmap.createScaledBitmap(squareSelected, w, w, false);
        s[8][2] = Bitmap.createScaledBitmap(circleSelected, w, w, false);
        s[8][3] = Bitmap.createScaledBitmap(triangleSelected, w, w, false);
        b[1] = Bitmap.createScaledBitmap(greenbin, binW, binH, false);
        b[2] = Bitmap.createScaledBitmap(orangebin, binW, binH, false);
        b[3] = Bitmap.createScaledBitmap(bluebin, binW, binH, false);
    }

    // Title Screen
    public void draw(Canvas canvas, int width, int height, int dx, boolean soundEnabled, boolean musicEnabled, boolean isLefty) {

        Paint paint = new Paint();

        canvas.drawBitmap(titleBackground, 0, 0, paint);
        canvas.drawBitmap(titleBelt, dx, (int)(height * .6), paint);
        canvas.drawBitmap(titleBelt, -width + dx, (int)(height * .6), paint);

        // Display the nosound bitmap if sound is not enabled
        if (!soundEnabled) {
            canvas.drawBitmap(noSound, (int)(301 * width / 480), (int)(100 * height / 270), paint);
        }

        // Display the nomusic bitmap is music is not enabled
        if (!musicEnabled) {
            canvas.drawBitmap(noMusic, (int)(353 * width / 480), (int)(100 * height / 270), paint);
        }

        // Display the lefty bitmap is lefty mode is enabled
        if (isLefty) {
            canvas.drawBitmap(lefty, (int)(406 * width / 480), (int)(100 * height / 270), paint);
        }

    }

    // Game Over Screen
    public void draw(Canvas canvas, int score, int scoreNeeded, int timeBonus) {

        Paint paint = new Paint();

        paint.setColor(Color.rgb(255, 255, 255));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(100);

        canvas.drawText("Level Completed", 10, 110, paint);
        canvas.drawText("Raw Score:     "  + score, 10, 320, paint);
        canvas.drawText("Time Bonus:    " + timeBonus, 10, 430, paint);
        canvas.drawText("Total Score:   " + (timeBonus + score), 10, 640, paint);
        canvas.drawText("Score Needed:  " + scoreNeeded, 10, 750, paint);

        if ((timeBonus + score) > scoreNeeded) {
            canvas.drawText("Congrats! You passed the level!", 10, 960, paint);
        } else {
            canvas.drawText("You did not pass the level.", 10, 960, paint);
        }


    }

    // Draw Level Selection
    public void draw(Canvas canvas, int height, int width, ArrayList<Level> levels, int dy) {
        Paint paint = new Paint();

        canvas.drawBitmap(levelBackground, 0, 0, paint);

        int levelWidth = width / 11;

        int total = 0;

        x = width / 11;
        y = width / 11 + dy;

        for (int i = 0; i < levels.size() / 5 + 1; i++) {

            for (int z = 0; z < 5; z++) {

                if (total == levels.size()) {
                    break;
                }

                if (total == 0) {
                        canvas.drawBitmap(levelUnlocked, x, y, paint);
                } else {
                    if (levels.get(total).isCompleted() || levels.get(total - 1).isCompleted()) {
                        canvas.drawBitmap(levelUnlocked, x, y, paint);
                    } else {
                        canvas.drawBitmap(levelLocked, x, y, paint);
                    }
                }

                paint.setColor(Color.rgb(89, 89, 89));
                paint.setStyle(Paint.Style.FILL);
                paint.setTextSize(levelWidth / 2);
                canvas.drawText("" + (total + 1), x + levelWidth / 3, y + (width / 15), paint);

                x += 2 * levelWidth;
                total++;
            }

                x = width / 11;
                y += 2 * levelWidth;

            }

        // Draw back button
        canvas.drawBitmap(back, 413 * width / 480, 6 * height / 270, paint);

    }

    // Game
    public void draw(Context context, Canvas canvas, int width, int height, ArrayList<Bin> bins, ArrayList<Combo> combos, int comboItems, int visibleCombos, ArrayList<Shape> belt, int beltAmount, int selected, int timer, int score, int dx, ArrayList<PowerUp> powerUps, Message message, int beltDX, int level, int beltItemsLeft) {

        Paint paint = new Paint();

        // http://paletton.com/#uid=7330u0kvgqFl6xCqPuVzplJGVgz

        // Draw Background
        canvas.drawBitmap(bg, 0, 0, paint);

        // Draw Belt
        canvas.drawBitmap(blt, beltDX, 200, paint);
        canvas.drawBitmap(blt,(int)(-(width *.85) + beltDX), 200, paint);

        x = dx + (beltAmount - 1) * (int) (width * .85 / beltAmount) + (int) (width * .85 / beltAmount / 4);

        int beltDrawAmount;
        if (beltAmount + 1 > belt.size()) {
            beltDrawAmount = belt.size();
        } else {
            beltDrawAmount = beltAmount + 1;
        }

        for (int i = 0; i < beltDrawAmount; i++) {
            boolean s = false;
            if (selected == i) {
                s = true;
            }

            // Make sure the current object has not been played
            if (!belt.get(i).isDone()) {

               drawShape(belt.get(i).getColor(), belt.get(i).getShape(), canvas, paint, x, ((int) (height * .5) - (int) (width * .85 / beltAmount / 2)) / 2 + 50, s);

                if (belt.get(i).getPowerUp() != 0) {

                    paint.setColor(Color.rgb(0, 0, 0));
                    paint.setStyle(Paint.Style.FILL);
                    paint.setTextSize(550 / beltAmount);

                    for (int a = 0; a < powerUps.size(); a++) {
                        if (powerUps.get(a).getID() == belt.get(i).getPowerUp()) {
                            canvas.drawText(powerUps.get(a).getLabel(), x + 5, ((int) (height * .5) - (int) (width * .85 / beltAmount / 2)) / 2 + (650 / beltAmount), paint);

                        }
                    }

                }

            }

            x -= (int) (width * .85 / beltAmount);
        }

        // Draw Bins

        x = 0;

        for (int i = 0; i < bins.size(); i++) {

            canvas.drawBitmap(b[bins.get(i).getColor()], x, height / 2, paint);

            // If the bin is a specific shape, draw the shape
            paint.setColor(Color.rgb(0, 0, 0));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3);

            switch (bins.get(i).getShape()) {
                case 1:
                    canvas.drawRect(x + (int)(width * .85 / bins.size() / 5),height - ((int)((width * .85 / bins.size() - (width * .85 / bins.size() / 5)))), x + (int)((width * .85 / bins.size() - (width * .85 / bins.size() / 5))), height - (int)(width * .85 / bins.size() / 5), paint);
                    break;
                case 2:
                    canvas.drawOval(x + (int)(width * .85 / bins.size() / 5),height - ((int)((width * .85 / bins.size() - (width * .85 / bins.size() / 5)))), x + (int)((width * .85 / bins.size() - (width * .85 / bins.size() / 5))), height - (int)(width * .85 / bins.size() / 5), paint);
                    break;
            }

            x += (int) (width * .85 / bins.size());

        }

        // Draw Combos

        canvas.drawBitmap(combobg, (int)(width *.85), 0, paint);

        int w = ((int) (width * .15) - (int) (2 * width * .15 / 10) - (int) ((comboItems - 1) * width * .15 / 10)) / comboItems;
        y = 10;

        int comboDrawAmt = 0;

        if (visibleCombos == -1) {
            int canFit = (int) (height / (w + width * .15 / 10));

            if (canFit < combos.size()) {
                comboDrawAmt = canFit;
            } else {
                comboDrawAmt = combos.size();
            }
        } else {
            comboDrawAmt = visibleCombos;
        }

        for (int i = 0; i < comboDrawAmt; i++) {

            x = (int) (width * .85) + (int) (width * .15 / 10);

            for (int z = 0; z < comboItems; z++) {

                boolean d = false;

                if (combos.get(i).isDone(z)) {
                    d = true;
                }

                drawShape(combos.get(i).getColor(z) + 3, combos.get(i).getShape(z),canvas, paint, x, y, d);

                x += w + (width * .15 / 10);

            }

            y += w + (width * .15 / 10);

        }

        // Draw Timer
        paint.setColor(Color.rgb(0, 0, 0));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(72);
        String time = "" + (timer / 60) + ":";
        if ((timer % 60) < 10) {
            time += "0" + (timer % 60);
        } else {
            time += (timer % 60);
        }
        canvas.drawText("Level " + level + "     Timer: " + time + "     Items: " + beltItemsLeft + "     Combos: " + combos.size(), 130, 75, paint);

        // Draw Score
        paint.setColor(Color.rgb(0,0,0));
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(72);
        String s = score + "";
        int l = s.length();
        for (int i = 0; i < 5 - l; i++) {

            s = "0" + s;
        }
        canvas.drawText(s, (int)(width *.85) - 225, 80, paint);

        // Draw Message
        if (message != null) {
            paint.setColor(Color.rgb(0,0,0));
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(72);
            switch (message.getType()) {
                case 1:
                    canvas.drawText(message.getText(), 500, 80, paint);
                    break;
                case 2:
                    canvas.drawText(message.getText(), 10, (int) (height * .5) - 20, paint);
                    break;
            }
        }

    }

    public void drawShape(int color, int shape, Canvas canvas, Paint paint, int x, int y, boolean selected) {

       canvas.drawBitmap(s[color][shape], x, y, paint);

        if (selected) {
            if (color < 4) {
                canvas.drawBitmap(s[7][shape], x, y, paint);
            } else {
                canvas.drawBitmap(s[8][shape], x, y, paint);
            }
        }

    }


}
