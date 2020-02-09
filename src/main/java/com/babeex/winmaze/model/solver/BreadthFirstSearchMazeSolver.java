package com.babeex.winmaze.model.solver;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.babeex.winmaze.model.Maze;
import com.babeex.winmaze.model.MazeSquare;
import com.babeex.winmaze.model.Side;
import com.babeex.winmaze.model.exceptions.OutofBoundsException;

public class BreadthFirstSearchMazeSolver implements MazeSolver {

    private static Logger logger = LogManager.getLogger();

    @Override
    public void solve(Maze maze) {

        Deque<MazeSquare> path = new ArrayDeque<>();
        path.push(maze.getStartSquare());

        Map<MazeSquare, Set<Side>> sqrDoorsAttempted = new HashMap<>();

        ArrayList<MazeSquare> moveList = new ArrayList<>(maze.getWidth() * maze.getHeight());
        moveList.add(maze.getStartSquare());

        Set<MazeSquare> deadEndSqrs = new HashSet<>();
        int sqnum = 0;

        try {
            while (maze.getCurrentSquare() != maze.getEndSquare()) {

                Thread.sleep(0, 200);

                maze.setCurrentSquare(moveList.get(sqnum));
                maze.getCurrentSquare().setIsOnPath(Boolean.TRUE);

                Set<Side> doorsTried = sqrDoorsAttempted.get(maze.getCurrentSquare());

                if (doorsTried == null) {
                    doorsTried = new HashSet<>();
                    sqrDoorsAttempted.put(maze.getCurrentSquare(), doorsTried);
                }

                Side outDoorSide = maze.getCurrentSquare()
                        .getNextOutDoorStartingFrom(maze.getCurrentSquare().getInDoor(), doorsTried);

                if (outDoorSide == null || deadEndSqrs.contains(maze.getCurrentSquare())) {
                    deadEndSqrs.add(maze.getCurrentSquare());
                    maze.getCurrentSquare().setIsOnPath(Boolean.FALSE);
                    path.pop();
                    moveList.remove(sqnum);
                    sqnum--;
                } else {

                    doorsTried.add(outDoorSide);

                    MazeSquare nextSquare = null;

                    // Move through out door to next square
                    switch (outDoorSide) {
                    case TOP:
                        nextSquare = maze.getSquare(maze.getCurrentSquare().getX(), maze.getCurrentSquare().getY() - 1);
                        break;
                    case RIGHT:
                        nextSquare = maze.getSquare(maze.getCurrentSquare().getX() + 1, maze.getCurrentSquare().getY());
                        break;
                    case BOTTOM:
                        nextSquare = maze.getSquare(maze.getCurrentSquare().getX(), maze.getCurrentSquare().getY() + 1);
                        break;
                    case LEFT:
                        nextSquare = maze.getSquare(maze.getCurrentSquare().getX() - 1, maze.getCurrentSquare().getY());
                        break;
                    }

                    maze.setCurrentSquare(nextSquare);

                    // mark the in door
                    path.push(maze.getCurrentSquare());
                    moveList.add(maze.getCurrentSquare());
                    sqnum++;
                }
            }
        } catch (OutofBoundsException | InterruptedException e) {
            logger.error(e);
        }

    }

}
