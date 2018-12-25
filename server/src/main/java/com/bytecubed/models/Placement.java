package com.bytecubed;

public class Placement {

    private final int relativeX;
    private final int relativeY;
    private final String pos;
    private final String tag;
    private boolean isCenter;

    public Placement(int relativeX, int relativeY, String pos, String tag) {
        this(relativeX,relativeY,pos,tag,false);
    }

    public boolean isCenter() {
        return isCenter;
    }

    public Placement(int relativeX, int relativeY, String pos, String tag, boolean isCenter) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.pos = pos;
        this.tag = tag;
        this.isCenter = isCenter;
    }

    public int getRelativeX() {
        return relativeX;
    }

    @Override
    public String toString() {
        return "Placement{" +
                "relativeX=" + relativeX +
                ", relativeY=" + relativeY +
                ", pos='" + pos + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    public int getRelativeY() {
        return relativeY;
    }

    public String getPos() {
        return pos;
    }

    public String getTag() {
        return tag;
    }

}
