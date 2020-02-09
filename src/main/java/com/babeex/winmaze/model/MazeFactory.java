package com.babeex.winmaze.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.babeex.winmaze.model.exceptions.OutofBoundsException;
import com.babeex.winmaze.model.solver.MazeSolver;

public class MazeFactory {

    private static Logger logger = LogManager.getLogger();

    private static MazeFactory instance = null;

    private MazeFactory() {
    }

    public static MazeFactory getInstance() {

        if (instance == null) {
            synchronized (MazeFactory.class) {
                if (instance == null) {
                    instance = new MazeFactory();
                }
            }
        }
        return instance;
    }

    public Maze createEmpty(int width, int height) {

        Maze newMaze = null;

        try {
            newMaze = new Maze(width, height);
            initialiseSquares(newMaze);
        } catch (OutofBoundsException e) {
            logger.error(e);
        }

        return newMaze;
    }

    private void initialiseSquares(Maze maze) {
        // Initialise all squares
        for (int x = 0; x < maze.getWidth(); x++) {
            maze.getMazeGrid().add(new ArrayList<MazeSquare>(maze.getWidth()));
            for (int y = 0; y < maze.getHeight(); y++) {
                MazeSquare newSquare = new MazeSquare();
                newSquare.setX(x);
                newSquare.setY(y);
                newSquare.setIsOnPath(Boolean.FALSE);

                // For the boundary squares set a wall
                if (x == 0)
                    newSquare.setSide(Side.LEFT, SideType.WALL);

                if (y == 0)
                    newSquare.setSide(Side.TOP, SideType.WALL);

                if (x == maze.getWidth() - 1)
                    newSquare.setSide(Side.RIGHT, SideType.WALL);

                if (y == maze.getHeight() - 1)
                    newSquare.setSide(Side.BOTTOM, SideType.WALL);

                maze.getMazeGrid().get(x).add(newSquare);
            }
        }
    }

    /**
     * Set the start and exit points of the maze
     * 
     * @param width
     * @param height
     */
    private void setStartAndExit(Maze maze) {

        try {

            int x = -1;
            int y = -1;

            Side startSide = Side.getRandomSide();

            // set start square
            switch (startSide) {
            case TOP:
                x = getRandom(maze.getWidth());
                y = 0;
                break;
            case BOTTOM:
                x = getRandom(maze.getWidth());
                y = maze.getHeight() - 1;
                break;
            case LEFT:
                x = 0;
                y = getRandom(maze.getHeight());
                break;
            case RIGHT:
                x = maze.getWidth() - 1;
                y = getRandom(maze.getHeight());
                break;
            }

            maze.setStartSquare(maze.getSquare(x, y));
            maze.getStartSquare().setSide(startSide, SideType.DOOR_IN);

            Side endSide = Side.getOpposite(startSide);

            // set end square
            switch (endSide) {
            case TOP:
                x = getRandom(maze.getWidth());
                y = 0;
                break;
            case BOTTOM:
                x = getRandom(maze.getWidth());
                y = maze.getHeight() - 1;
                break;
            case LEFT:
                x = 0;
                y = getRandom(maze.getHeight());
                break;
            case RIGHT:
                x = maze.getWidth() - 1;
                y = getRandom(maze.getHeight());
                break;
            }

            maze.setEndSquare(maze.getSquare(x, y));
            maze.getEndSquare().setSide(endSide, SideType.DOOR_OUT);

        } catch (OutofBoundsException e) {
            logger.error(e);
        }
    }

    /**
     * Create a maze layout given the initialised maze
     */
    private void createMaze(Maze maze) {

        ArrayList<MazeSquare> moveList = new ArrayList<>(maze.getWidth() * maze.getHeight());
        moveList.add(maze.getStartSquare());

        int sqnum = 0;
        try {
            while (sqnum >= 0) {

                try {
                    Thread.sleep(0, 100);
                } catch (InterruptedException e) {
                    return;
                }

                maze.setCurrentSquare(moveList.get(sqnum));
                maze.getCurrentSquare().setIsOnPath(Boolean.TRUE);
                Side doorSide = chooseDoor(maze, maze.getCurrentSquare());

                if (doorSide == null) {
                    maze.getCurrentSquare().setIsOnPath(Boolean.FALSE);
                    moveList.remove(sqnum);
                    sqnum--;
                } else {
                    // mark the out door
                    maze.getCurrentSquare().setSide(doorSide, SideType.DOOR_OUT);

                    // Move through out door to next square
                    switch (doorSide) {
                    case TOP:
                        maze.setCurrentSquare(
                                maze.getSquare(maze.getCurrentSquare().getX(), maze.getCurrentSquare().getY() - 1));
                        break;
                    case RIGHT:
                        maze.setCurrentSquare(
                                maze.getSquare(maze.getCurrentSquare().getX() + 1, maze.getCurrentSquare().getY()));
                        break;
                    case BOTTOM:
                        maze.setCurrentSquare(
                                maze.getSquare(maze.getCurrentSquare().getX(), maze.getCurrentSquare().getY() + 1));
                        break;
                    case LEFT:
                        maze.setCurrentSquare(
                                maze.getSquare(maze.getCurrentSquare().getX() - 1, maze.getCurrentSquare().getY()));
                        break;
                    }

                    // mark the in door
                    maze.getCurrentSquare().setSide(Side.getOpposite(doorSide), SideType.DOOR_IN);
                    moveList.add(maze.getCurrentSquare());
                    sqnum++;
                }
            }
        } catch (OutofBoundsException e) {
            logger.error(e);
        }
    }

    private Side chooseDoor(Maze maze, MazeSquare sqr) {

        List<Side> candidates = new ArrayList<>();

        try {

            if (sqr.getTop() == null) {
                MazeSquare sqrAbove = maze.getSquare(sqr.getX(), sqr.getY() - 1);

                if (sqrAbove.hasInDoor()) {
                    sqr.setSide(Side.TOP, SideType.WALL);
                    sqrAbove.setSide(Side.BOTTOM, SideType.WALL);
                } else {
                    candidates.add(Side.TOP);
                }
            }

            if (sqr.getRight() == null) {
                MazeSquare sqrRight = maze.getSquare(sqr.getX() + 1, sqr.getY());

                if (sqrRight.hasInDoor()) {
                    sqr.setSide(Side.RIGHT, SideType.WALL);
                    sqrRight.setSide(Side.LEFT, SideType.WALL);
                } else {
                    candidates.add(Side.RIGHT);
                }
            }

            if (sqr.getBottom() == null) {
                MazeSquare sqrBelow = maze.getSquare(sqr.getX(), sqr.getY() + 1);

                if (sqrBelow.hasInDoor()) {
                    sqr.setSide(Side.BOTTOM, SideType.WALL);
                    sqrBelow.setSide(Side.TOP, SideType.WALL);
                } else {
                    candidates.add(Side.BOTTOM);
                }
            }

            if (sqr.getLeft() == null) {
                MazeSquare sqrLeft = maze.getSquare(sqr.getX() - 1, sqr.getY());

                if (sqrLeft.hasInDoor()) {
                    sqr.setSide(Side.LEFT, SideType.WALL);
                    sqrLeft.setSide(Side.RIGHT, SideType.WALL);
                } else {
                    candidates.add(Side.LEFT);
                }
            }

        } catch (OutofBoundsException e) {
            logger.error(e);
        }

        if (candidates.isEmpty())
            return null;

        return candidates.get(getRandom(candidates.size()));
    }

    /**
     * Generate a maze
     */
    public void generateMaze(Maze maze) {
        setStartAndExit(maze);
        createMaze(maze);
    }

    /**
     * Solve a given maze
     */
    public void solveMaze(MazeSolver solver, Maze maze) {
        solver.solve(maze);
    }

    private static int getRandom(int x) {
        Random rand = new Random();
        return rand.nextInt(x);
    }
}
