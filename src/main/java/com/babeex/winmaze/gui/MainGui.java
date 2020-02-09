package com.babeex.winmaze.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.babeex.winmaze.AppState;
import com.babeex.winmaze.model.Maze;
import com.babeex.winmaze.model.MazeFactory;
import com.babeex.winmaze.model.solver.BreadthFirstSearchMazeSolver;

public class MainGui extends JFrame {

    private static final long serialVersionUID = -653448580033048973L;

    private static Logger logger = LogManager.getLogger();

    private static final String GENERATE_MAZE = "Generate Maze";
    private static final String CANCEL_MAZE = "Cancel Maze";
    private static final String SOLVE_MAZE = "Solve Maze";

    private SwingWorker<Object, Object> generateMazeWorker;

    /*
     * Main GUI components
     */
    JPanel controlsPnl;
    JLabel widthLbl;
    JSpinner widthSpinner;
    JLabel heightLbl;
    JSpinner heightSpinner;
    JLabel squareSizeLbl;
    JSlider squareSizeSldr;
    JButton mazeButton;
    JButton solveButton;
    MazePanel mazePanel;

    private JPanel getControlsPnl() {
        if (controlsPnl == null) {
            controlsPnl = new JPanel();
            controlsPnl.setLayout(new FlowLayout(FlowLayout.LEADING));
            controlsPnl.add(getWidthLbl());
            controlsPnl.add(getWidthSpinner());
            controlsPnl.add(getHeightLbl());
            controlsPnl.add(getHeightSpinner());
            controlsPnl.add(getSquareSizeLbl());
            controlsPnl.add(getSquareSizeSldr());
            controlsPnl.add(getMazeButton());
            controlsPnl.add(getSolveButton());
        }
        return controlsPnl;
    }

    private JLabel getWidthLbl() {
        if (widthLbl == null) {
            widthLbl = new JLabel("Width:");
        }
        return widthLbl;
    }

    private JSpinner getWidthSpinner() {
        if (widthSpinner == null) {
            SpinnerModel widthModel = new SpinnerNumberModel(10, 3, 609, 1);
            widthSpinner = new JSpinner(widthModel);
            widthSpinner.setEditor(new JSpinner.NumberEditor(widthSpinner));
            getWidthLbl().setLabelFor(widthSpinner);
        }
        return widthSpinner;
    }

    private JLabel getHeightLbl() {
        if (heightLbl == null) {
            heightLbl = new JLabel("Height:");
        }
        return heightLbl;
    }

    private JSpinner getHeightSpinner() {
        if (heightSpinner == null) {
            SpinnerModel heightModel = new SpinnerNumberModel(10, 3, 331, 1);
            heightSpinner = new JSpinner(heightModel);
            heightSpinner.setEditor(new JSpinner.NumberEditor(heightSpinner));
            getHeightLbl().setLabelFor(heightSpinner);
        }
        return heightSpinner;
    }

    private JLabel getSquareSizeLbl() {
        if (squareSizeLbl == null) {
            squareSizeLbl = new JLabel("Square Size");
        }
        return squareSizeLbl;
    }

    private JSlider getSquareSizeSldr() {
        if (squareSizeSldr == null) {
            squareSizeSldr = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);
            squareSizeSldr.setMajorTickSpacing(5);
            squareSizeSldr.setMinorTickSpacing(1);
            squareSizeSldr.setPaintTicks(true);
            squareSizeSldr.setPaintLabels(true);

            squareSizeSldr.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent event) {
                    int value = getSquareSizeSldr().getValue();
                    calcMaxVisibleMazeSize();
                }
            });

            getSquareSizeLbl().setLabelFor(squareSizeSldr);
        }
        return squareSizeSldr;
    }

    private JButton getMazeButton() {
        if (mazeButton == null) {
            mazeButton = new JButton(GENERATE_MAZE);
            mazeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    logger.debug(getMazeButton().getText());

                    if (GENERATE_MAZE.equals(getMazeButton().getText())) {

                        Maze maze = MazeFactory.getInstance().createEmpty((int) getWidthSpinner().getValue(),
                                (int) getHeightSpinner().getValue());

                        getMazePanel().setMazeModel(maze);
                        getMazePanel().setSquareSize(getSquareSizeSldr().getValue());
                        getMazePanel().setState(AppState.GENERATING);

                        generateMazeWorker = new SwingWorker<Object, Object>() {

                            @Override
                            protected Object doInBackground() throws Exception {
                                MazeFactory.getInstance().generateMaze(maze);
                                return null;
                            }

                            @Override
                            protected void done() {
                                try {
                                    getMazeButton().setText(GENERATE_MAZE);

                                    if (!isCancelled())
                                        getSolveButton().setEnabled(true);
                                } catch (Exception ignore) {
                                }
                            }
                        };

                        getSolveButton().setEnabled(false);
                        generateMazeWorker.execute();
                        getMazeButton().setText(CANCEL_MAZE);
                    } else {
                        generateMazeWorker.cancel(true);
                        getMazeButton().setText(GENERATE_MAZE);
                    }
                }
            });
        }
        return mazeButton;
    }

    private JButton getSolveButton() {
        if (solveButton == null) {
            solveButton = new JButton(SOLVE_MAZE);
            solveButton.setEnabled(false);
            solveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    logger.debug(getSolveButton().getText());

                    Maze maze = getMazePanel().getMazeModel();
                    getMazePanel().setState(AppState.SOLVING);

                    SwingWorker<Object, Object> solveMazeWorker = new SwingWorker<Object, Object>() {

                        @Override
                        protected Object doInBackground() throws Exception {
                            MazeFactory.getInstance().solveMaze(new BreadthFirstSearchMazeSolver(), maze);
                            return null;
                        }

                        @Override
                        protected void done() {
                            getSolveButton().setEnabled(true);
                        }
                    };

                    getSolveButton().setEnabled(false);
                    solveMazeWorker.execute();
                }
            });
        }
        return solveButton;
    }

    private MazePanel getMazePanel() {
        if (mazePanel == null) {
            mazePanel = new MazePanel();
            mazePanel.setBorder(BorderFactory.createEtchedBorder());
        }
        return mazePanel;
    }

    /**
     * Initialise the contents of the frame.
     */
    public void initialise() {
        setBounds(100, 100, 1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        URL iconURL = getClass().getResource("/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);

        setIconImage(icon.getImage());

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                calcMaxVisibleMazeSize();
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getControlsPnl(), BorderLayout.PAGE_START);
        getContentPane().add(getMazePanel(), BorderLayout.CENTER);
    }

    private void calcMaxVisibleMazeSize() {
        // This is only called when the user releases the mouse button.
        int xOffset = 8;
        int yOffset = 12;
        int mazePanelWidth = getMazePanel().getWidth() - xOffset;
        int mazePanelHeight = getMazePanel().getHeight() - yOffset;
        int sqrSize = getSquareSizeSldr().getValue();

        logger.debug("MazePanel width = " + mazePanelWidth + ", height = " + mazePanelHeight);
        logger.debug("For sqr size " + sqrSize + ": maze width = " + mazePanelWidth / sqrSize + ", maze height = "
                + mazePanelHeight / sqrSize);

        getWidthSpinner().setValue(mazePanelWidth / sqrSize);
        getHeightSpinner().setValue(mazePanelHeight / sqrSize);
    }

}
