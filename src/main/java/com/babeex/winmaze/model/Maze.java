package com.babeex.winmaze.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.SwingPropertyChangeSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.babeex.winmaze.model.exceptions.OutofBoundsException;

public class Maze {

    private static Logger logger = LogManager.getLogger();

    public static final String MAZE_CHANGE_EVENT = "MazeChange";
    public static final String MAZE_SOLVING_EVENT = "MazeSolving";

    private int width;
    private int height;

    private List<List<MazeSquare>> mazeGrid;
    private MazeSquare startSquare;
    private MazeSquare endSquare;
    private MazeSquare currentSquare;

    private SwingPropertyChangeSupport pcs;

    /**
     * Hide default constructor
     */
    private Maze() {
    }

    public Maze(int width, int height) throws OutofBoundsException {

        if (width <= 0 || height <= 0) {
            throw new OutofBoundsException();
        }

        setWidth(width);
        setHeight(height);

        pcs = new SwingPropertyChangeSupport(this);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    MazeSquare getStartSquare() {
        return startSquare;
    }

    void setStartSquare(MazeSquare startSquare) {
        MazeSquare oldValue = getStartSquare();
        this.startSquare = startSquare;
        pcs.firePropertyChange(MAZE_CHANGE_EVENT, oldValue, startSquare);
    }

    MazeSquare getEndSquare() {
        return endSquare;
    }

    void setEndSquare(MazeSquare endSquare) {
        MazeSquare oldValue = getEndSquare();
        this.endSquare = endSquare;
        pcs.firePropertyChange(MAZE_CHANGE_EVENT, oldValue, endSquare);
    }

    MazeSquare getCurrentSquare() {
        return currentSquare;
    }

    void setCurrentSquare(MazeSquare currentSquare) {
        MazeSquare oldValue = getCurrentSquare();
        this.currentSquare = currentSquare;
        pcs.firePropertyChange(MAZE_CHANGE_EVENT, oldValue, currentSquare);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }

    /**
     * get the main maze data structure.
     * 
     * @return
     */
    List<List<MazeSquare>> getMazeGrid() {
        if (mazeGrid == null) {
            mazeGrid = new ArrayList<>(height);
        }

        return mazeGrid;
    }

    /**
     * return the MazeSquare at a specific x,y location. Note 0,0 is the top
     * left square.
     * 
     * @param x
     * @param y
     * @return
     * @throws Exception
     */
    public MazeSquare getSquare(int x, int y) throws OutofBoundsException {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new OutofBoundsException();
        }

        return getMazeGrid().get(x).get(y);
    }

}
