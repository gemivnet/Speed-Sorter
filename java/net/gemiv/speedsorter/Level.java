package net.gemiv.speedsorter;

/**
 * Created by GEMIV on 5/22/2016.
 */
public class Level {

    int id, scoreNeeded, scoreGot, bonusTime, timerType, timer, beltAmount, beltItems, colors, shapes, visibleCombos, comboAmount, comboItems, scroll, neededPercent;
    boolean completed = false;
    Message message;
    Bin[] binsArray;
    PowerUp[] powerUpsArray;

    public Level(int id, int scoreNeeded, int bonusTime, int timerType, int timer, int beltAmount, int beltItems, int colors, int shapes, int visibleCombos,
                 int comboAmount, int comboItems, int scroll, int neededPercent, Message message, Bin[] binsArray, PowerUp[] powerUpsArray) {
        this.id = id;
        this.scoreNeeded = scoreNeeded;
        this.timerType = timerType;
        this.timer = timer;
        this.beltAmount = beltAmount;
        this.beltItems = beltItems;
        this.colors = colors;
        this.shapes = shapes;
        this.visibleCombos = visibleCombos;
        this.comboAmount = comboAmount;
        this.comboItems = comboItems;
        this.scroll = scroll;
        this.message = message;
        this.binsArray = binsArray;
        this.powerUpsArray = powerUpsArray;
        this.neededPercent = neededPercent;
        this.bonusTime = bonusTime;
    }

    public int getScoreNeeded() {
        return scoreNeeded;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setScoreGot(int score) {
        scoreGot = score;
    }

    public int getScoreGot() {
        return scoreGot;
    }

    public int getBonusTime() {
        return bonusTime;
    }

    public Message getMessage() {
        return message;
    }

    public int getScroll() {
        return scroll;
    }

    public int getTimer() {
        return timer;
    }

    public int getVisibleCombos() {
        return visibleCombos;
    }

    public int getTimerType() {
        return timerType;
    }

    public int getBeltAmount() {
        return beltAmount;
    }

    public int getBeltItems() {
        return beltItems;
    }

    public int getShapes() {
        return shapes;
    }

    public int getColors() {
        return colors;
    }

    public int getComboItems() {
        return comboItems;
    }

    public int getComboAmount() {
        return comboAmount;
    }

    public PowerUp[] getPowerUpsArray() {
        return powerUpsArray;
    }

    public int getID() {
        return id;
    }

    public Bin[] getBinsArray() {
        return binsArray;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getNeededPercent() { return neededPercent; }

}
