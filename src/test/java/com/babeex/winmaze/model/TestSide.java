package com.babeex.winmaze.model;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestSide {

    private static Logger logger = LogManager.getLogger();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetRandomSide() {

        boolean topSideSelected = false;
        boolean bottomSideSelected = false;
        boolean leftSideSelected = false;
        boolean rightSideSelected = false;

        int randomTests = 1000;
        for (int x = 0; x < randomTests; x++) {

            Side randomSide = Side.getRandomSide();

            if (randomSide == null)
                fail("A random side can only be one of TOP,BOTTOM,LEFT or RIGHT");

            if (randomSide == Side.TOP)
                topSideSelected = true;
            if (randomSide == Side.BOTTOM)
                bottomSideSelected = true;
            if (randomSide == Side.LEFT)
                leftSideSelected = true;
            if (randomSide == Side.RIGHT)
                rightSideSelected = true;
        }

        if (!(topSideSelected && bottomSideSelected && leftSideSelected && rightSideSelected))
            fail("Improbable random distribution for " + randomTests + " attempts.");
    }

    @Test
    public void testGetPrevious() {

        List<Side> SideValues = Arrays.asList(Side.values());

        Collections.reverse(SideValues);

        for (Side s : SideValues) {
            try {
                if (s.getPrevious() == null)
                    fail("Must return in a cyclical manner the previous enum.");

            } catch (ArrayIndexOutOfBoundsException e) {
                fail("Must return in a cyclical manner the previous enum.");
            }

            logger.info("Side = " + s + ", previous = " + s.getPrevious());
        }
    }

    @Test
    public void testGetNext() {
        for (Side s : Side.values()) {
            try {
                if (s.getNext() == null)
                    fail("Must return in a cyclical manner the next enum.");

            } catch (Exception e) {
                fail("Must return in a cyclical manner the next enum.");
            }

            logger.info("Side = " + s + ", next = " + s.getNext());
        }
    }

}
