package com.babeex.winmaze.model;

import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.babeex.winmaze.model.exceptions.OutofBoundsException;

public class TestMaze {

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
    public void testMaze() {

        try {
            Maze testValidMaze = new Maze(4, 4);
        } catch (OutofBoundsException e) {
            fail("Valid parameters");
        }

        try {
            Maze testInvalidMaze = new Maze(0, 0);
            fail("Invalid parameters");
        } catch (OutofBoundsException e) {

        }

    }

    @Test
    public void testGetSquare() {

        Maze testValidMaze;

        try {
            testValidMaze = new Maze(4, 4);

            MazeSquare x0y3 = testValidMaze.getSquare(0, 3);

            logger.info(x0y3);

        } catch (OutofBoundsException e) {
            fail("Valid parameters");
        }

    }

    @Test
    public void testReportMaze() {

        Maze testValidMaze;

        try {
            testValidMaze = new Maze(3, 3);

            logger.info("All squares in maze.");
            for (int x = 0; x < testValidMaze.getWidth(); x++) {
                for (int y = 0; y < testValidMaze.getHeight(); y++) {
                    logger.info(testValidMaze.getSquare(x, y));
                }
            }
            logger.info("");

        } catch (OutofBoundsException e) {
            fail("Valid parameters");
        }

    }

}
