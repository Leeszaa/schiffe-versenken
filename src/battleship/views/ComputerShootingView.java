package battleship.views;

import javax.swing.*;

import battleship.BattleshipGUI;
import battleship.factorys.ships.*;
import battleship.managers.ComputerShootingManager;
import battleship.factorys.player.IPlayer;
import battleship.factorys.hits.IHits;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

/**
 * @class ComputerShootingView
 *        Represents the shooting view for playing against a computer opponent.
 *        Extends {@link JPanel} to create a custom panel for shooting actions.
 */
public class ComputerShootingView extends JPanel {
    /**
     * SerialVersionUID for the ComputerShootingView class.
     */
    private static final long serialVersionUID = -3677038397678972024L;
    private JPanel gridPanel1;
    private JPanel gridPanel2;
    private JPanel[][] gridCells1;
    private LinePanel[][] gridCells2;
    private JButton nextPlayerButton;
    private ComputerShootingManager shootingManager;
    private IPlayer player;
    private IPlayer computer;
    private JLabel playerName;
    private boolean clicksAllowed = true;
    private boolean isOnePlayerDebug = false;
    private BattleshipGUI battleshipGUI;

    /**
     * Constructor for ComputerShootingView.
     *
     * @param player           The human player.
     * @param computer         The computer player.
     * @param isOnePlayerDebug Flag for single-player debug mode.
     * @param battleshipGUI    Reference to the main GUI.
     */
    public ComputerShootingView(IPlayer player, IPlayer computer, boolean isOnePlayerDebug,
            BattleshipGUI battleshipGUI) {
        this.shootingManager = new ComputerShootingManager(player, computer);
        this.player = player;
        this.computer = computer;
        this.isOnePlayerDebug = isOnePlayerDebug;
        this.battleshipGUI = battleshipGUI;

        initComponents();
    }

    /**
     * Initializes the components of the view, including grids, labels, buttons,
     * and sets up the starting player.
     */
    private void initComponents() {
        setBackground(Color.darkGray);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        initPlayerNameLabel(gbc);
        initGridLabels(gbc);
        initGridPanels(gbc);
        initNextPlayerButton(gbc);

        determineStartingPlayer();
        drawShipsOnOwnBoard();
        drawTargetBoard();

        JButton backButton = new JButton("Spiel beenden");
        backButton.addActionListener(e -> battleshipGUI.showMainMenuView());
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(backButton, gbc);
    }

    /**
     * Initializes the label that displays the current player's name.
     * 
     * @param gbc The GridBagConstraints for positioning the label.
     */
    private void initPlayerNameLabel(GridBagConstraints gbc) {
        playerName = new JLabel(player.getName(), SwingConstants.CENTER);
        playerName.setFont(new Font("Roboto", Font.BOLD, 40));
        playerName.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(playerName, gbc);
    }

    /**
     * Initializes the labels that describe the two game board grids.
     * 
     * @param gbc The GridBagContraints for positioning the labels.
     */
    private void initGridLabels(GridBagConstraints gbc) {
        JLabel label1 = new JLabel("Eigene Schiffe", SwingConstants.CENTER);
        label1.setFont(new Font("Roboto", Font.BOLD, 20));
        label1.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(label1, gbc);

        JLabel label2 = new JLabel("Gegenerische Schiffe", SwingConstants.CENTER);
        label2.setFont(new Font("Roboto", Font.BOLD, 20));
        label2.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(label2, gbc);
    }

    /**
     * Initializes the two game board grid panels, one for the player and one for
     * the computer.
     * 
     * @param gbc The GridBagConstraints for positioning the panels.
     */
    private void initGridPanels(GridBagConstraints gbc) {
        gridPanel1 = new JPanel(new GridLayout(10, 10));
        gridPanel2 = new JPanel(new GridLayout(10, 10));
        gridPanel1.setPreferredSize(new Dimension(500, 500));
        gridPanel2.setPreferredSize(new Dimension(500, 500));

        gridCells1 = new JPanel[10][10];
        gridCells2 = new LinePanel[10][10];

        initGridCells();

        JPanel gridWithLabels1 = new JPanel(new BorderLayout());
        gridWithLabels1.add(createColumnLabels(), BorderLayout.NORTH);
        gridWithLabels1.add(createRowLabels(), BorderLayout.WEST);
        gridWithLabels1.add(gridPanel1, BorderLayout.CENTER);

        JPanel gridWithLabels2 = new JPanel(new BorderLayout());
        gridWithLabels2.add(createColumnLabels(), BorderLayout.NORTH);
        gridWithLabels2.add(createRowLabels(), BorderLayout.WEST);
        gridWithLabels2.add(gridPanel2, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(gridWithLabels1, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(gridWithLabels2, gbc);
    }

    /**
     * Initializes the individual cells within the grid panels.
     * Adds mouse listeners to the cells on the computer's grid to handle player
     * shots.
     */
    private void initGridCells() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JPanel cell1 = new JPanel();
                cell1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridCells1[i][j] = cell1;
                gridPanel1.add(cell1);

                LinePanel cell2 = new LinePanel();
                cell2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell2.addMouseListener(new GridClickListener(i, j));
                gridCells2[i][j] = cell2;
                gridPanel2.add(cell2);
            }
        }
    }

    /**
     * Initializes the "Next Player" button (or "Weiter" in German).
     * This button is used to trigger the computer's turn after the player has made
     * their shot.
     * 
     * @param gbc The GridBagConstraints for positioning the button.
     */
    private void initNextPlayerButton(GridBagConstraints gbc) {
        nextPlayerButton = new JButton("Weiter");
        nextPlayerButton.addActionListener(e -> handleNextPlayerClick());
        nextPlayerButton.setFont(new Font("Roboto", Font.BOLD, 20));
        nextPlayerButton.setBackground(Color.WHITE);
        nextPlayerButton.setForeground(Color.BLACK);
        nextPlayerButton.setPreferredSize(new Dimension(200, 50));
        nextPlayerButton.setVisible(false);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(nextPlayerButton, gbc);
    }

    /**
     * Handles the click on the "Next Player" button, executing the computer's turn.
     * Redraws the grids and checks for game over conditions.
     */
    private void handleNextPlayerClick() {
        shootingManager.computerShoot();
        clearGrids();
        drawShipsOnOwnBoard();
        drawTargetBoard();
        revalidate();
        repaint();

        if (shootingManager.isGameOver(computer)) {
            handleComputerWins();
        } else {
            handlePlayerTurn();
        }
    }

    /**
     * Handles the scenario where the computer wins the game.
     */
    private void handleComputerWins() {
        JOptionPane.showMessageDialog(null, "Spiel vorbei! " + computer.getName() + " hat gewonnen!");
        battleshipGUI.showMainMenuView();
    }

    /**
     * Manages the player's turn, enabling or disabling clicks based on debug mode
     * settings.
     */
    private void handlePlayerTurn() {
        clicksAllowed = true;
        if (isOnePlayerDebug) {
            clicksAllowed = false;
            nextPlayerButton.setVisible(true);
        } else {
            nextPlayerButton.setVisible(false);
        }
    }

    /**
     * Randomly selects the starting player (human or computer) and
     * displays a message indicating who goes first.
     */
    private void determineStartingPlayer() {
        IPlayer startPlayer = shootingManager.selectRandomPlayer();
        if (startPlayer == player) {
            JOptionPane.showMessageDialog(ComputerShootingView.this, "Du beginnst!");
            clicksAllowed = true;
        } else {
            handleComputerStarts();
        }
    }

    /**
     * Handles the case where the computer starts the game,
     * executing the computer's first shot.
     */
    private void handleComputerStarts() {
        clicksAllowed = false;
        JOptionPane.showMessageDialog(ComputerShootingView.this, "Der Computer beginnt!");
        shootingManager.computerShoot();
        clicksAllowed = true;
    }

    /**
     * Creates the column labels (A-J) for the game board grids.
     * 
     * @return A JPanel containing the column labels.
     */
    private JPanel createColumnLabels() {
        JPanel panel = new JPanel(new GridLayout(1, 10));
        for (char c = 'A'; c <= 'J'; c++) {
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            label.setFont(new Font("Roboto", Font.BOLD, 20));
            label.setForeground(Color.WHITE);
            label.setBackground(Color.darkGray);
            label.setOpaque(true);
            panel.add(label);
        }
        return panel;
    }

    /**
     * Creates the row labels (1-10) for the game board grids.
     * 
     * @return A JPanel containing the row labels.
     */
    private JPanel createRowLabels() {
        JPanel panel = new JPanel(new GridLayout(10, 1));
        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setFont(new Font("Roboto", Font.BOLD, 20));
            label.setForeground(Color.WHITE);
            label.setBackground(Color.darkGray);
            label.setOpaque(true);
            panel.add(label);
        }
        return panel;
    }

    /**
     * Draws the player's ships on their own game board, including any hits made by
     * the computer.
     */
    private void drawShipsOnOwnBoard() {
        Map<Point, IShip> ships = player.getGameBoard().getShipLocations();
        Map<Point, IHits> hits = computer.getTargetingBoard().getHits();

        for (Map.Entry<Point, IShip> entry : ships.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
            gridCells1[r][c].setBackground(Color.GRAY);
        }

        for (Map.Entry<Point, IHits> entry : hits.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
            gridCells1[r][c].setBorder(BorderFactory.createLineBorder(Color.RED, 4));

            TransparentPanel overlay = new TransparentPanel(new Color(255, 0, 0, 50));
            overlay.setBounds(0, 0, gridCells1[r][c].getWidth(), gridCells1[r][c].getHeight());
            gridCells1[r][c].setLayout(new BorderLayout());
            gridCells1[r][c].add(overlay, BorderLayout.CENTER);
            gridCells1[r][c].revalidate();
            gridCells1[r][c].repaint();
        }
    }

    /**
     * Draws the player's target board, showing hits, misses, and sunk ships on the
     * computer's grid.
     */
    private void drawTargetBoard() {
        Map<Point, IHits> hits = player.getTargetingBoard().getHits();
        for (Map.Entry<Point, IHits> entry : hits.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
            if (entry.getValue().isHit()) {
                gridCells2[r][c].setBackground(Color.RED);
                List<Point> sunkShipCoordinates = shootingManager.isShipSunk(c, r);
                if (!sunkShipCoordinates.isEmpty()) {
                    drawLineThroughSunkShip(sunkShipCoordinates);
                }
            } else {
                drawMissMarker(r, c);
            }
        }
    }

    /**
     * Draws a miss marker (animated water) on the target board at the given
     * coordinates.
     * 
     * @param r The row coordinate.
     * @param c The column coordinate.
     */
    private void drawMissMarker(int r, int c) {
        try {
            ImageIcon gifIcon = new ImageIcon(getClass().getResource("water_tile.gif"));
            if (gifIcon.getIconWidth() == -1) {
                return;
            }

            JLabel missLabel = new JLabel(gifIcon);
            missLabel.setHorizontalAlignment(JLabel.CENTER);
            missLabel.setVerticalAlignment(JLabel.CENTER);

            gridCells2[r][c].setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;
            gbc.anchor = GridBagConstraints.CENTER;
            gridCells2[r][c].add(missLabel, gbc);
            gridCells2[r][c].revalidate();
            gridCells2[r][c].repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Draws a cross through the cells that represent a sunk ship on the target
     * board.
     * 
     * @param coordinates A List of Point objects, each representing a cell of the
     *                    sunk ship.
     */
    private void drawLineThroughSunkShip(List<Point> coordinates) {
        for (Point point : coordinates) {
            gridCells2[point.y][point.x].setSunk(true);
        }
    }

    /**
     * Clears the contents of both game board grids, resetting their visual state.
     */
    public void clearGrids() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridCells1[i][j].setBackground(null);
                gridCells1[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridCells1[i][j].removeAll();
                gridCells2[i][j].setBackground(null);
                gridCells2[i][j].setSunk(false);
                gridCells2[i][j].removeAll();
            }
        }
    }

    /**
     * @class GridClickListener
     *        A MouseAdapter that listens for clicks on the grid cells
     *        and handles player shots on the computer's board.
     */
    private class GridClickListener extends MouseAdapter {
        private final int row;
        private final int col;

        /**
         * Constructor for GridClickListener.
         *
         * @param row The row coordinate of the grid cell.
         * @param col The column coordinate of the grid cell.
         */
        public GridClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        /**
         * Handles mouse click events on the grid cells.
         *
         * @param e The MouseEvent triggered by the click.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            handleGridCellClick();
        }

        /**
         * Handles the logic for a grid cell click, representing a player shot.
         * Checks for valid shots, updates the game state, and manages game over
         * conditions.
         */
        private void handleGridCellClick() {
            if (!clicksAllowed) {
                return;
            }
            if (shootingManager.isAlreadyHit(col, row)) {
                JOptionPane.showMessageDialog(null, "Bereits auf dieses Feld geschossen!");
                return;
            }

            boolean hit = shootingManager.addHitToTargetBoard(col, row);

            if (hit) {
                handleHit();
            } else {
                handleMiss();
            }

            if (shootingManager.isGameOver(player)) {
                handlePlayerWins();
            } else {
                prepareComputerTurn();
            }
        }

        /**
         * Handles a successful hit, updating the display and checking for sunk ships.
         */
        private void handleHit() {
            gridCells2[row][col].setBackground(Color.RED);
            List<Point> sunkShipCoordinates = shootingManager.isShipSunk(col, row);
            if (!sunkShipCoordinates.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Schiff versenkt!");
                drawLineThroughSunkShip(sunkShipCoordinates);
            }
        }

        /**
         * Handles a miss, updating the display with a miss marker.
         */
        private void handleMiss() {
            drawMissMarker(row, col);
        }

        /**
         * Handles the case where the player wins the game.
         */
        private void handlePlayerWins() {
            JOptionPane.showMessageDialog(null, "Spiel vorbei! " + player.getName() + " hat gewonnen!");
            battleshipGUI.showMainMenuView();
        }

        /**
         * Prepares the game for the computer's turn by disabling player input and
         * enabling the "Next Player" button.
         */
        private void prepareComputerTurn() {
            clicksAllowed = false;
            nextPlayerButton.setVisible(true);
        }
    }

    /**
     * @class LinePanel
     *        A custom JPanel that can draw a line through it to visually indicate a
     *        sunk ship.
     */
    class LinePanel extends JPanel {
        /**
         * SerialVersionUID for the LinePanel class.
         */
        private static final long serialVersionUID = 790697028496269236L;
        private boolean isSunk = false;

        /**
         * Sets the sunk status of the panel.
         * 
         * @param isSunk True if the panel should represent a sunk ship cell, false
         *               otherwise.
         */
        public void setSunk(boolean isSunk) {
            this.isSunk = isSunk;
            repaint();
        }

        /**
         * Overrides the paintComponent method to draw a diagonal line through the panel
         * if the `isSunk` flag is set to true.
         * 
         * @param g The Graphics object to use for drawing.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (isSunk) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(0, 0, getWidth(), getHeight());
                g2d.drawLine(0, getHeight(), getWidth(), 0);
            }
        }
    }

    /**
     * @class TransparentPanel
     *        A custom JPanel that draws a semi-transparent overlay color.
     *        Used to visually indicate hits on the player's own board.
     */
    class TransparentPanel extends JPanel {
        /**
         * SerialVersionUID for the TransparentPanel class.
         */
        private static final long serialVersionUID = 383926842048906531L;
        private final Color overlayColor;

        /**
         * Constructor for TransparentPanel.
         * 
         * @param overlayColor The Color to use for the semi-transparent overlay.
         */
        public TransparentPanel(Color overlayColor) {
            this.overlayColor = overlayColor;
            setOpaque(false);
        }

        /**
         * Overrides the paintComponent method to draw the semi-transparent color
         * overlay.
         * 
         * @param g The Graphics object used for drawing.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(overlayColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }
}
