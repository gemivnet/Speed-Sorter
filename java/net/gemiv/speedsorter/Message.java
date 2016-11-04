package net.gemiv.speedsorter;

public class Message {

    long time;
    String text;
    int type;

    public Message(String text, long time, int type) {
        this.time = time;
        this.text = text;
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public int getType() {
        return type;
    }

}
