package net.gemiv.speedsorter;

/**
 * Created by GEMIV on 5/22/2016.
 */
public class PowerUp {

    int id, chance;
    String label, message;

    public PowerUp(int id, int chance, String label, String message) {
        this.id = id;
        this.chance = chance;
        this.label = label;
        this.message = message;
    }

    public int getID() {
        return id;
    }

    public int getChance() {
        return chance;
    }

    public String getLabel() {
        return label;
    }

    public String getMessage() { return message; }

}
