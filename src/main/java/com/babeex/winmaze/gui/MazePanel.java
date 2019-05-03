package com.babeex.winmaze.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.babeex.winmaze.AppState;
import com.babeex.winmaze.model.Maze;
import com.babeex.winmaze.model.MazeSquare;
import com.babeex.winmaze.model.SideType;
import com.babeex.winmaze.model.exceptions.OutofBoundsException;

public class MazePanel extends JPanel {

    private static Logger logger = LogManager.getLogger();

    private Maze mazeModel;
    private int squareSize;
    private AppState state = AppState.GENERATING;

    MazePanel() {
    }

    public Maze getMazeModel() {
        return mazeModel;
    }

    public void setMazeModel(Maze mazeModel) {
        this.mazeModel = mazeModel;
        this.mazeModel.addPropertyChangeListener(new MazePropertyChangeListener());
    }

    public int getSquareSize() {
        return squareSize;
    }

    public void setSquareSize(int squareSize) {
        this.squareSize = squareSize;
    }

    public AppState getState() {
        return state;
    }

    public void setState(AppState state) {
        this.state = state;
    }

    /**
     * 
     */
    private static final long serialVersionUID = 3593493358997171653L;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getMazeModel() != null)
            draw(g);
    }

    public void draw(Graphics g) {

        float wallBrushWidth = 2;
        Color wallColour = Color.BLACK;
        Color pathColour = Color.RED;

        if (getState() == AppState.SOLVING) {
            pathColour = Color.GREEN;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(wallColour);
        g2.setStroke(new BasicStroke(wallBrushWidth));

        int yOffset = 6;
        int wallSize = getSquareSize();

        try {

            for (int y = 0; y < getMazeModel().getHeight(); y++) {
                int xOffset = 5;
                for (int x = 0; x < getMazeModel().getWidth(); x++) {
                    MazeSquare sqr = getMazeModel().getSquare(x, y);
                    logger.debug(sqr);
                    // Top
                    if (sqr.getTop() == SideType.WALL)
                        g2.drawLine(xOffset, yOffset, xOffset + wallSize, yOffset);
                    // Right
                    if (sqr.getRight() == SideType.WALL)
                        g2.drawLine(xOffset + wallSize, yOffset, xOffset + wallSize, yOffset + wallSize);
                    // Bottom
                    if (sqr.getBottom() == SideType.WALL)
                        g2.drawLine(xOffset, yOffset + wallSize, xOffset + wallSize, yOffset + wallSize);
                    // Left
                    if (sqr.getLeft() == SideType.WALL)
                        g2.drawLine(xOffset, yOffset, xOffset, yOffset + wallSize);

                    if (sqr.isOnPath()) {
                        g2.setColor(pathColour);

                        float pathBrushWidth = Math.max(1, wallSize - (2 * wallBrushWidth));

                        g2.setStroke(new BasicStroke(pathBrushWidth));

                        // Top
                        if (sqr.getTop() == SideType.DOOR_IN)
                            g2.drawLine(xOffset + (wallSize / 2), yOffset, xOffset + (wallSize / 2),
                                    yOffset + (wallSize / 2));
                        // Right
                        if (sqr.getRight() == SideType.DOOR_IN)
                            g2.drawLine(xOffset + wallSize, yOffset + (wallSize / 2), xOffset + (wallSize / 2),
                                    yOffset + (wallSize / 2));
                        // Bottom
                        if (sqr.getBottom() == SideType.DOOR_IN)
                            g2.drawLine(xOffset + (wallSize / 2), yOffset + wallSize, xOffset + (wallSize / 2),
                                    yOffset + (wallSize / 2));
                        // Left
                        if (sqr.getLeft() == SideType.DOOR_IN)
                            g2.drawLine(xOffset, yOffset + (wallSize / 2), xOffset + (wallSize / 2),
                                    yOffset + (wallSize / 2));

                        // Top
                        if (sqr.getTop() == SideType.DOOR_OUT)
                            g2.drawLine(xOffset + (wallSize / 2), yOffset + (wallSize / 2), xOffset + (wallSize / 2),
                                    yOffset);
                        // Right
                        if (sqr.getRight() == SideType.DOOR_OUT)
                            g2.drawLine(xOffset + (wallSize / 2), yOffset + (wallSize / 2), xOffset + wallSize,
                                    yOffset + (wallSize / 2));
                        // Bottom
                        if (sqr.getBottom() == SideType.DOOR_OUT)
                            g2.drawLine(xOffset + (wallSize / 2), yOffset + (wallSize / 2), xOffset + (wallSize / 2),
                                    yOffset + wallSize);
                        // Left
                        if (sqr.getLeft() == SideType.DOOR_OUT)
                            g2.drawLine(xOffset + (wallSize / 2), yOffset + (wallSize / 2), xOffset,
                                    yOffset + (wallSize / 2));

                        g2.setColor(wallColour);
                        g2.setStroke(new BasicStroke(wallBrushWidth));
                    }

                    xOffset = xOffset + wallSize;
                }
                yOffset = yOffset + wallSize;
            }

        } catch (OutofBoundsException e) {
            logger.error(e);
        }
    }

    private class MazePropertyChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            logger.debug("Event received " + evt);

            if (evt.getPropertyName().equals(Maze.MAZE_CHANGE_EVENT)) {
                MazePanel.this.repaint();
            }
        }
    }

}
