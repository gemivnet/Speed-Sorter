package net.gemiv.speedsorter;

import android.content.Context;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Calendar;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    Context context;

  public GamePanel(Context context) {
      super(context);

      this.context = context;

      // Add Callback to Surface Holder to Capture Event
      getHolder().addCallback(this);

      mainThread = new MainThread(getHolder(), this);
      setFocusable(true);
     }

    // Game variables
    private MainThread mainThread;
    int width = 0, height = 0;
    boolean hasInit = false;
    boolean soundEnabled = true, musicEnabled = false, lefty = false;
    SoundPool sounds;
    int selectSound, wrongColorSound, completedSound, comboBreakSound, addedSound;
    MediaPlayer backgroundMusic;
    ArrayList<Level> levels = new ArrayList<Level>();
    int state = 0;
    boolean started = false;
    boolean touched = true;
    int maxTitleDY;

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            boolean retry = true;

            while (retry) {
                try {
                    mainThread.setRunning(false);
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                retry = false;
                if (backgroundMusic != null) {
                    backgroundMusic.stop();
                }
                if (!mainThread.isAlive()) {
                    mainThread = new MainThread(getHolder(), this);
                }
            }
    }

    @Override
        public void surfaceCreated(SurfaceHolder holder) {

        mainThread.setRunning(true);

        mainThread.start();

        if (!started) {

            // Call the function which populates the levels ArrayList
            createLevels();

            // Create sounds needed for the game
            sounds = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            selectSound = sounds.load(context, R.raw.select, 1);
            comboBreakSound = sounds.load(context, R.raw.combobreak, 1);
            wrongColorSound = sounds.load(context, R.raw.wrongcolor, 1);
            completedSound = sounds.load(context, R.raw.completed, 1);
            addedSound = sounds.load(context, R.raw.added, 1);
            started = true;
        } else {
            state = -2;
        }

    }

    // Update Variables
    int previousTime = 0;
    long previousNano, currentTime;
    int dx, beltDX = 0;
    long messageTime = -1;
    int titleDX = 0, levelDY = 0, oldY = 0, startY = 0;
    int previousState = 5;

    public void update() {

        // Play or change background music
        if (musicEnabled) {
            if ((previousState < 1 & state > 0) || (previousState > 0 && state < 1)) {
                previousState = state;
                if (state > 0) {
                    if (backgroundMusic != null) {
                        backgroundMusic.stop();
                    }
                    backgroundMusic = MediaPlayer.create(context, R.raw.game);
                    backgroundMusic.setLooping(true);
                    backgroundMusic.setVolume(.35f, .35f);
                    backgroundMusic.start();
                } else {
                    if (backgroundMusic != null) {
                        backgroundMusic.stop();
                    }
                    backgroundMusic = MediaPlayer.create(context, R.raw.title);
                    backgroundMusic.setLooping(true);
                    backgroundMusic.setVolume(.5f, .5f);
                    backgroundMusic.start();
                }
            }
        }


        switch (state) {
            // Title Screen
            case 0:
                 currentTime = System.nanoTime();

                    if (currentTime - previousNano > 10000000) {
                        previousNano = currentTime;
                        titleDX += 3;

                        if (titleDX >= width) {
                            titleDX = 0;
                        }
                    }
                break;
            // In Game - Singleplayer
            case 1:
                // TIMER - Check if a second has gone by, if so increase the timer by 1
                Calendar c = Calendar.getInstance();
                if (previousTime != c.get(Calendar.SECOND)) {
                    if (timerType == 0) {
                        timer++;
                    } else {
                        timer--;
                    }
                    previousTime = c.get(Calendar.SECOND);
                }

                // SCROLLING - dx = offset for current scroll cycle, if enough time has gone by increase dx to the belt appears to scroll
                int maxDX = (int) (width * .85) / beltAmount;

                currentTime = System.nanoTime();

                if (currentTime - previousNano > 10000000) {
                    previousNano = currentTime;

                    // Scroll the belt if scroll is enabled

                    beltDX += scroll;
                    if (beltDX >= (width * .85)) {
                        beltDX = 0;
                    }

                    dx += scroll;

                    if (dx >= maxDX & belt.size() == beltAmount + 1) {
                        dx = 0;

                        // If an item was not removed from the conveyor belt, end the game
                        // TODO: Make it so they have 3 lives and gain lives back
                        if (!belt.get(0).isDone()) {
                            state = 0;
                            levelDY = 0;
                        }

                        belt.remove(0);
                        addBelt();
                        if (selected != -1) {
                            selected -= 1;
                        }

                    }

                }

                // Check if a message has been init'ed, if so set the start time of the message to the current millis time
                if (message != null & messageTime == -1)  {
                    messageTime = System.currentTimeMillis();
                }

                // Check if the message expires
                if (messageTime != -1) {
                    if (System.currentTimeMillis() - messageTime >= message.time) {
                        message = null;
                        messageTime = -1;
                    }
                }

                // Check to see if the game is over
                boolean end = true;

                for (int i = 0; i < belt.size(); i++) {
                    if (!belt.get(i).isDone()) {
                        end = false;
                    }
                }

                if (end || belt.size() == 0 || combos.size() == 0) {
                    state = 0;
                    levelDY = 0;
                }

                // Check if another item should be added to the belt

                int itemsOnBelt = 0;

                for (int i = 0; i < belt.size(); i++) {
                    if (!belt.get(i).isDone()) {
                        itemsOnBelt++;
                    }
                }

                break;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (canvas != null) {

            width = canvas.getWidth();
            height = canvas.getHeight();


            switch (state) {
                case 0:
                    if (!hasInit) {
                        GamePaint.getInstance().loadBitmaps(context, width, height);
                        hasInit = true;
                    }
                    GamePaint.getInstance().draw(canvas, width, height, titleDX, soundEnabled, musicEnabled, lefty);
                    break;
                case -1:
                    GamePaint.getInstance().draw(canvas, score, scoreNeeded, timeBonus);
                    break;
                case 1:
                    GamePaint.getInstance().draw(context,canvas, width, height, bins, combos, levels.get(state - 1).getComboItems(), visibleCombos, belt, beltAmount, selected, timer, score, dx, powerUps, message, beltDX, state, beltItemsLeft);
                    break;
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // Get the X and Y Position of the Touch
        int x = (int)(event.getX());
        int y = (int)(event.getY());


        // Switch which state the game is in and respond to touch accordingly
        switch (state) {
            // Title Screen
            case 0:
                // Check if the singleplayer button was pressed
                if (validTouch(x, y, 301 * width / 480, 441 * width / 480, 16 * height / 270, 47 * height / 270)) {
                    state = 1;
                    playLevel(1);
                    playSound(selectSound);
                }
                // Check if the multiplayer button was pressed

                // Check if the sound boolean button was pressed
                if (validTouch(x, y, 301 * width / 480, 337 * width / 480, 100 * height / 270, 131 * height / 270)) {
                    // Change the state of soundEnabled
                    soundEnabled ^= true;
                    playSound(selectSound);
                }

                // Check if the music boolean button was pressed
                if (validTouch(x, y, 353 * width / 480, 389 * width / 480, 100 * height / 270, 131 * height / 270)) {
                    // Change the state of soundEnabled
                    musicEnabled ^= true;

                    if (backgroundMusic != null) {
                        if (musicEnabled) {
                            if (state > 0) {
                                backgroundMusic.setVolume(.35f, .35f);
                            } else {
                                backgroundMusic.setVolume(.5f, .5f);
                            }
                        } else {
                            backgroundMusic.setVolume(0f, 0f);
                        }
                    }

                    playSound(selectSound);
                }

                // Check if the lefty boolean button was pressed
                if (validTouch(x, y, 406 * width / 480, 442 * width / 480, 100 * height / 270, 131 * height / 270)) {
                    // Change the state of soundEnabled
                    lefty ^= true;
                    playSound(selectSound);
                }
                break;
            // In Game - Singleplayer
            case 1:

                // Check if the touch was in the bins or the belt
                if (y < (int) (height / 2)) {

                    // Touch was in the belt
                    int oldSelected = selected;
                    int region = (int) (width * .85 / beltAmount);

                    // Determine which one was selected
                    for (int i = -1; i < beltAmount; i++) {
                        int fillDXTouch = 0;
                        if (beltAmount - i - 1 <= belt.size()) {

                            int d = beltAmount - i - 2;
                            if (d > belt.size()) {
                                d = belt.size();
                            }

                            for (int z = d; z >= 0; z--) {
                                if (belt.get(z).isDone()) {

                                }
                            }
                        }

                        if (x >= dx + fillDXTouch +  (i * region) + ((region - (width * .85 / beltAmount / 2)) / 2) & x <= fillDXTouch + dx + ((i + 1) * region) - ((region - (width * .85 / beltAmount / 2)) / 2) & y >= ((int)(height *.5) - (int)(width * .85 / beltAmount / 2)) / 2 + 50 & y <= ((int)(height *.5) - (int)(width * .85 / beltAmount / 2)) / 2 + (int)(width * .85 / beltAmount / 2) + 50) {

                            if (!belt.get(beltAmount - i - 1).isDone()) {
                                selected = beltAmount - i - 1;
                                playSound(selectSound);
                                break;
                            }
                        }
                    }

                    // Check if the user unselected the item
                    if (selected == oldSelected) {
                        selected = -1;
                    }

                } else {

                    // Touch was in the bins
                    if (selected != -1) {

                        // Determine which bin was pressed
                        int region = (int) (width * .85 / bins.size());

                        for (int i = 0; i < bins.size(); i++) {
                            // Region found - see if bin matches the item
                            if (x <= (i + 1) * region) {

                                // Check if selected color = bin color and selected shape = bin shape
                                if (selected <= belt.size()) {
                                    if ((belt.get(selected).getColor() == bins.get(i).getColor() || bins.get(i).getColor() == 0) & (belt.get(selected).getShape() == bins.get(i).getShape() || bins.get(i).getShape() == 0)) {

                                        int color = belt.get(selected).getColor(), powerup = belt.get(selected).getPowerUp(), shape = belt.get(selected).getShape();

                                        belt.get(selected).setDone();

                                        boolean shouldReset = true;

                                        // Check if the item matches a current combo item
                                        for (int a = 0; a < levels.get(currentLevel).getComboItems(); a++) {
                                            if (combos.get(0).getColor(a) == color & combos.get(0).getShape(a) == shape & !combos.get(0).isDone(a)) {
                                                combos.get(0).setDone(a);
                                                score += combos.get(0).getPoints(a);

                                                if (powerup != 0) {

                                                    for (int b = 0; b < powerUps.size(); b++) {
                                                        if (powerUps.get(b).getID() == powerup) {
                                                            message = new Message(powerUps.get(b).getMessage(), 1500, 1);
                                                        }
                                                    }

                                                }

                                                // Check if there was a powerup on the used item
                                                switch (powerup) {
                                                    case 1:
                                                        timer += 5;
                                                        break;
                                                    case 2:
                                                        if (timer > 5) {
                                                            timer -= 5;
                                                        } else {
                                                            timer = 0;
                                                        }
                                                        break;
                                                    case 3:
                                                        if (scroll < 10) {
                                                            scroll++;
                                                        }
                                                        break;
                                                    case 4:
                                                        if (scroll > 0) {
                                                            scroll--;
                                                        }
                                                        break;
                                                    case 5:
                                                        score += 100;
                                                        break;
                                                    case 6:
                                                        if (score >= 100) {
                                                            score -= 110;
                                                        }
                                                        break;
                                                    case 7:
                                                        for (int z = 0; z < beltAmount + 1; z++) {
                                                            belt.remove(0);
                                                        }
                                                        break;
                                                }

                                                shouldReset = false;
                                                break;
                                            }
                                        }

                                        if (shouldReset) {
                                            combos.get(0).reset();
                                            playSound(comboBreakSound);
                                        }

                                        boolean completed = true;

                                        for (int a = 0; a < levels.get(currentLevel).getComboItems(); a++) {
                                            if (!combos.get(0).isDone(a)) {
                                                completed = false;
                                            }
                                        }

                                        if (completed) {

                                            combos.remove(0);
                                            playSound(completedSound);


                                        }

                                        if (!shouldReset & !completed) {
                                            playSound(addedSound);
                                        }

                                        selected = -1;

                                    } else {
                                        selected = -1;
                                        playSound(wrongColorSound);
                                    }
                                }

                                break;
                            }
                        }
                    }

                }

                break;
        }

        return super.onTouchEvent(event);
    }



    public boolean chance(int percentage) {

        int num = (int)(100 * Math.random() + 1);

        if (num <= percentage) {
            return true;
        }
       return false;

    }

    // Game Variables
    ArrayList<Combo> combos;
    ArrayList<Shape> belt;
    ArrayList<Bin> bins;
    ArrayList<PowerUp> powerUps;

    int beltItemsLeft;
    int beltAmount;
    int visibleCombos;
    int selected = -1;
    int timerType;
    int timer;
    int score;
    int scroll;
    Message message;
    int neededPercent;

    int currentLevel;

    public void playLevel(int level) {

        state = level;

        level -= 1;

        currentLevel = level;

        combos = new ArrayList<Combo>();
        belt = new ArrayList<Shape>();
        bins = new ArrayList<Bin>();
        powerUps = new ArrayList<PowerUp>();

        score = 0;
        beltAmount = levels.get(level).getBeltAmount();
        beltItemsLeft = levels.get(level).getBeltItems();
        visibleCombos = levels.get(level).getVisibleCombos();
        timerType = levels.get(level).getTimerType();
        timer = levels.get(level).getTimer();
        scroll = levels.get(level).getScroll();
        message = levels.get(level).getMessage();
        neededPercent = levels.get(level).getNeededPercent();
        beltDX = 0;
        dx = 0;
        selected = -1;

        // Create Bins
        Bin[] binsArray = levels.get(level).getBinsArray();


        for (int i = 0; i < binsArray.length; i++) {
            bins.add(binsArray[i]);
        }


        // Create PowerUps

        PowerUp[] powerUpsArray = levels.get(level).getPowerUpsArray();

        for (int i = 0; i < powerUpsArray.length; i++) {
            powerUps.add(powerUpsArray[i]);
        }

        // Create Combos
        for (int i = 0; i < levels.get(level).getComboAmount(); i++) {

            ArrayList<Shape> items = new ArrayList<Shape>();

            for (int x = 0; x < levels.get(level).getComboItems(); x++) {
                items.add(new Shape((int)(levels.get(level).getColors() * Math.random() + 1), (int)(levels.get(level).getShapes() * Math.random() + 1), 10));
            }

            combos.add(new Combo(items));

        }

        // Create Belt
        for (int i = 0; i < beltAmount + 1; i++) {
            beltItemsLeft--;
            belt.add(new Shape((int) (levels.get(level).getColors() * Math.random() + 1), (int)(levels.get(level).getShapes() * Math.random() + 1)));
        }

        for (int i = 0; i < belt.size(); i++) {
            for (int x = 0; x < powerUps.size(); x++) {
                if (belt.get(i).getPowerUp() == 0) {
                    if (chance(powerUps.get(x).getChance())) {
                        belt.get(i).setPowerUp(powerUps.get(x).getID());
                    }
                }
            }
        }

        Calendar c = Calendar.getInstance();
        previousTime = c.get(Calendar.SECOND);

        GamePaint.getInstance().updateScales(((int) (width * .85 / beltAmount / 2)),((int) (width * .85 / beltAmount )) / 2, ((int) (width * .15) - (int) (2 * width * .15 / 10) - (int) ((levels.get(level).getComboItems() - 1) * width * .15 / 10)) / levels.get(level).getComboItems(), (int) (width * .85 / bins.size()), height / 2);


    }

    int scoreNeeded;
    int timeBonus = 0;

    public void createLevels() {
           levels.add(new Level(1, 100, 100, 0, 0, 5, 20, 2, 1, -1, 10, 2, 8, 100, new Message("Welcome! Match squares to colored bins to make combos of two.", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0)}, new PowerUp[]{}));
        levels.add(new Level(2, 150, 100, 0, 0, 6, 30, 3, 1, -1, 10, 2, 0, 100, new Message("Adding another color and more items to the belt.", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{}));
        levels.add(new Level(3, 150, 100, 0, 0, 6, 30, 3, 1, -1, 10, 2, 8, 100, new Message("The conveyor belt is starting to move!", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{}));
        levels.add(new Level(4, 150, 100, 0, 0, 6, 30, 3, 1, -1, 10, 2, 1, 100, new Message("The >>> powerup speeds up the belt, you don't want that!", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 20, ">>>", "Belt Speed Increased")}));
        levels.add(new Level(5, 150, 100, 0, 0, 6, 30, 3, 1, -1, 10, 2, 1, 100, new Message("The <<< powerup slows down the belt.", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 10, ">>>", "Belt Speed Increased"), new PowerUp(3, 10, ">>>", "Belt Speed Decreased")}));
        levels.add(new Level(6, 150, 100, 0, 0, 6, 30, 3, 2, -1, 10, 2, 2, 100, new Message("Now we are adding a new shape.", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 5, ">>>", "Belt Speed Increased"), new PowerUp(3, 5, ">>>", "Belt Speed Decreased")}));
        levels.add(new Level(7, 150, 100, 0, 0, 6, 30, 3, 2, -1, 10, 3, 2, 100, new Message("Combos of three now", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 5, ">>>", "Belt Speed Increased"), new PowerUp(3, 5, ">>>", "Belt Speed Decreased")}));
        levels.add(new Level(8, 150, 100, 0, 0, 6, 30, 3, 2, -1, 10, 3, 2, 100, new Message("No longer can you see the next combo", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 5, ">>>", "Belt Speed Increased"), new PowerUp(3, 5, ">>>", "Belt Speed Decreased")}));
        levels.add(new Level(9, 150, 100, 0, 0, 7, 500, 3, 3, -1,50, 2, 4, 100, new Message("Later Level Example", 5000, 2), new Bin[]{new Bin(1, 2), new Bin(2, 2), new Bin(3, 2), new Bin(1,1), new Bin(2, 1), new Bin(3, 1)}, new PowerUp[]{new PowerUp(3, 3, ">>>", "Belt Speed Increased"), new PowerUp(4, 3, "<<<", "Belt Speed Decreased"), new PowerUp(7, 1, "CLR", "Belt Cleared"), new PowerUp(5, 6, "+P", "100 Bonus Points")}));
        levels.add(new Level(1, 100, 100, 0, 0, 5, 20, 2, 1, -1, 10, 2, 8, 100, new Message("Welcome! Match squares to colored bins to make combos of two.", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0)}, new PowerUp[]{}));
        levels.add(new Level(2, 150, 100, 0, 0, 6, 30, 3, 1, -1, 10, 2, 0, 100, new Message("Adding another color and more items to the belt.", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{}));
        levels.add(new Level(3, 150, 100, 0, 0, 6, 30, 3, 1, -1, 10, 2, 8, 100, new Message("The conveyor belt is starting to move!", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{}));
        levels.add(new Level(4, 150, 100, 0, 0, 6, 30, 3, 1, -1, 10, 2, 1, 100, new Message("The >>> powerup speeds up the belt, you don't want that!", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 20, ">>>", "Belt Speed Increased")}));
        levels.add(new Level(5, 150, 100, 0, 0, 6, 30, 3, 1, -1, 10, 2, 1, 100, new Message("The <<< powerup slows down the belt.", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 10, ">>>", "Belt Speed Increased"), new PowerUp(3, 10, ">>>", "Belt Speed Decreased")}));
        levels.add(new Level(6, 150, 100, 0, 0, 6, 30, 3, 2, -1, 10, 2, 2, 100, new Message("Now we are adding a new shape.", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 5, ">>>", "Belt Speed Increased"), new PowerUp(3, 5, ">>>", "Belt Speed Decreased")}));
        levels.add(new Level(7, 150, 100, 0, 0, 6, 30, 3, 2, -1, 10, 3, 2, 100, new Message("Combos of three now", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 5, ">>>", "Belt Speed Increased"), new PowerUp(3, 5, ">>>", "Belt Speed Decreased")}));
        levels.add(new Level(8, 150, 100, 0, 0, 6, 30, 3, 2, -1, 10, 3, 2, 100, new Message("No longer can you see the next combo", 5000, 2), new Bin[]{new Bin(1, 0), new Bin(2, 0), new Bin(3, 0)}, new PowerUp[]{new PowerUp(3, 5, ">>>", "Belt Speed Increased"), new PowerUp(3, 5, ">>>", "Belt Speed Decreased")}));

        levels.get(0).setCompleted(true);
        levels.get(1).setCompleted(true);
        levels.get(2).setCompleted(true);
        levels.get(3).setCompleted(true);
        levels.get(4).setCompleted(true);
        levels.get(5).setCompleted(true);
        levels.get(6).setCompleted(true);
        levels.get(7).setCompleted(true);
        levels.get(8).setCompleted(true);

    }

    public boolean validTouch(int x, int y, int xMin, int xMax, int yMin, int yMax) {
        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            return true;
        }
        return false;
    }

    public void playSound(int id) {
        if (soundEnabled) {
            sounds.play(id, 1, 1, 1, 0, 1);
        }
    }

    public void addBelt() {

        // Check if more items need to be added to the belt
        if (beltItemsLeft > 0) {

            // Make it so there is a chance that the colors/shapes added to the belt up are ones that are needed
            int newColor = 0, newShape = 0;
            boolean happened = false;

            // Generate a random number, compare it to percentage to see if it will be a needed one
            if ((int) (100 * Math.random() + 1) <= neededPercent) {
                // Loop through current combo to see what is needed
                for (int i = 0; i < levels.get(state - 1).getComboItems(); i++) {
                    // If that part of the combo is not done, make that the next item
                    if (!combos.get(0).isDone(i)) {
                        newColor = combos.get(0).getColor(i);
                        newShape = combos.get(0).getShape(i);
                        happened = true;
                        break;
                    }
                }
            }

            // If the percent was not night enough create a random next color and shape
            if (!happened) {
                newColor = (int) (levels.get(state - 1).getColors() * Math.random() + 1);
                newShape = (int)(levels.get(state - 1).getShapes() * Math.random() + 1);
            }

            // Add the new shap to the belt
            belt.add(new Shape(newColor, newShape));

            // Check if a powerup should be added to the belt item
            for (int x = 0; x < powerUps.size(); x++) {
                if (belt.get(belt.size() - 1).getPowerUp() == 0) {
                    if (chance(powerUps.get(x).getChance())) {
                        belt.get(belt.size() - 1).setPowerUp(powerUps.get(x).getID());
                    }
                }
            }

            beltItemsLeft--;

        }

    }

}
