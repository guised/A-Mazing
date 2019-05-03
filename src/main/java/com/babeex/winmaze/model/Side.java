package com.babeex.winmaze.model;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Side {

    TOP, RIGHT, BOTTOM, LEFT;

    private static Logger logger = LogManager.getLogger();

    private static Random rand = new Random();

    public static Side getOpposite(Side side) {
        if (side == TOP)
            return BOTTOM;
        else if (side == BOTTOM)
            return TOP;
        else if (side == LEFT)
            return RIGHT;
        else if (side == RIGHT)
            return LEFT;
        else
            return null;
    }

    public static Side getRandomSide() {
        int side = rand.nextInt(4); // Four sides to choose from 0-3
        return Side.values()[side];
    }

    public Side getPrevious() {
        return Side.values()[(Side.values().length + this.ordinal() - 1) % Side.values().length];
    }

    public Side getNext() {
        return Side.values()[(this.ordinal() + 1) % Side.values().length];
    }
}
