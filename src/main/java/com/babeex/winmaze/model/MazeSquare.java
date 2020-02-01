package com.babeex.winmaze.model;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class to represent a single cell in a maze
 * 
 * The cell has four sides, top, bottom, left & right. Each side is either a
 * wall or a door.
 * 
 * 
 * @author Daniel
 *
 */

public class MazeSquare {

    private int x;
    private int y;

    private Map<Side, SideType> sides;

    private Boolean isOnPath;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Map<Side, SideType> getSides() {
        if (sides == null) {
            setSides(new EnumMap<Side, SideType>(Side.class));
        }
        return sides;
    }

    private void setSides(Map<Side, SideType> sides) {
        this.sides = sides;
    }

    public SideType getTop() {
        return getSides().get(Side.TOP);
    }

    private void setTop(SideType top) {
        getSides().put(Side.TOP, top);
    }

    public SideType getBottom() {
        return getSides().get(Side.BOTTOM);
    }

    private void setBottom(SideType bottom) {
        getSides().put(Side.BOTTOM, bottom);
    }

    public SideType getLeft() {
        return getSides().get(Side.LEFT);
    }

    private void setLeft(SideType left) {
        getSides().put(Side.LEFT, left);
    }

    public SideType getRight() {
        return getSides().get(Side.RIGHT);
    }

    private void setRight(SideType right) {
        getSides().put(Side.RIGHT, right);
    }

    public Boolean isOnPath() {
        return isOnPath == null ? Boolean.FALSE : isOnPath;
    }

    public void setIsOnPath(Boolean isOnPath) {
        this.isOnPath = isOnPath;
    }

    public void setSide(Side side, SideType type) {
        if (side == Side.TOP) {
            setTop(type);
        } else if (side == Side.BOTTOM) {
            setBottom(type);
        } else if (side == Side.LEFT) {
            setLeft(type);
        } else if (side == Side.RIGHT) {
            setRight(type);
        }
    }

    public boolean hasInDoor() {
        return getSides().containsValue(SideType.DOOR_IN);
    }

    public boolean hasOutDoor() {
        return getSides().containsValue(SideType.DOOR_OUT);
    }

    public boolean hasDoor() {
        return hasInDoor() || hasOutDoor();
    }

    public Side getInDoor() {
        if (getTop() == SideType.DOOR_IN)
            return Side.TOP;
        if (getBottom() == SideType.DOOR_IN)
            return Side.BOTTOM;
        if (getLeft() == SideType.DOOR_IN)
            return Side.LEFT;
        if (getRight() == SideType.DOOR_IN)
            return Side.RIGHT;

        return null;
    }

    public Side getNextOutDoorStartingFrom(Side start) {
        return getNextOutDoorStartingFrom(start, new HashSet<Side>());
    }

    public Side getNextOutDoorStartingFrom(Side start, Set<Side> doorsTried) {

        Side x = start.getNext();

        while (x != start) {
            if (x == Side.TOP && getTop() == SideType.DOOR_OUT && !doorsTried.contains(Side.TOP)) {
                return Side.TOP;
            } else if (x == Side.RIGHT && getRight() == SideType.DOOR_OUT && !doorsTried.contains(Side.RIGHT)) {
                return Side.RIGHT;
            } else if (x == Side.BOTTOM && getBottom() == SideType.DOOR_OUT && !doorsTried.contains(Side.BOTTOM)) {
                return Side.BOTTOM;
            } else if (x == Side.LEFT && getLeft() == SideType.DOOR_OUT && !doorsTried.contains(Side.LEFT)) {
                return Side.LEFT;
            }

            x = x.getNext();
        }

        return null;

    }

    @Override
    public String toString() {
        return "MazeSquare [x=" + x + ", y=" + y + ", sides=" + sides + ", isOnPath=" + isOnPath + "]";
    }

}
