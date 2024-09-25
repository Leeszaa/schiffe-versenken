package battleship.views;

import javax.swing.*;

import battleship.BattleshipGUI;
import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.ships.IShip;
import battleship.managers.ShootingManager;
import battleship.managers.ShootingManagerObserver;
import battleship.factorys.player.IPlayer;
import battleship.factorys.hits.IHits;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

/**
 * @class ShootingView
 *        Represents the shooting view in the Battleship game.
 *        Extends {@link JPanel} for shooting actions.
 */
public class ShootingView extends JPanel implements ShootingManagerObserver {
    /**
     * SerialVersionUID for the ShootingView class.
     */
    private static final long serialVersionUID = -33964322305217838L;
    private JPanel gridPanel1;
    private JPanel gridPanel2;
    private JPanel[][] gridCells1;
    private LinePanel[][] gridCells2;
    private JButton nextPlayerButton;
    private ShootingManager shootingManager;
    private IGameBoard currentGameBoard;
    private IGameBoard currentTargetingBoard;
    private IPlayer currentPlayer;
    private IPlayer opponentPlayer;
    private JLabel playerName;
    private boolean clicksAllowed = true;
    private boolean isOnePlayerDebug;
    private BattleshipGUI battleshipGUI;

    /**
     * Constructor for ShootingView.
     *
     * @param player1          The first player.
     * @param player2          The second player.
     * @param isOnePlayerDebug Flag for single-player debug mode.
     * @param battleshipGUI    Reference to the main GUI.
     */
    public ShootingView(IPlayer player1, IPlayer player2, boolean isOnePlayerDebug, BattleshipGUI battleshipGUI) {
        this.shootingManager = new ShootingManager(player1, player2);
        this.shootingManager.addObserver(this);

        this.currentPlayer = shootingManager.currentPlayer;
        this.opponentPlayer = shootingManager.opponentPlayer;
        this.currentGameBoard = currentPlayer.getGameBoard();
        this.currentTargetingBoard = currentPlayer.getTargetingBoard();

        this.isOnePlayerDebug = isOnePlayerDebug;
        this.battleshipGUI = battleshipGUI;

        initComponents();
    }

    /**
     * Handles the player switch event.
     * 
     * @param newPlayer      The new current player.
     * @param opponentPlayer The opponent player.
     */
    @Override
    public void onPlayerSwitched(IPlayer newPlayer, IPlayer opponentPlayer) {
        this.currentPlayer = newPlayer;
        this.opponentPlayer = opponentPlayer;
        this.currentGameBoard = newPlayer.getGameBoard();
        this.currentTargetingBoard = newPlayer.getTargetingBoard();

        playerName.setText(newPlayer.getName());
    }

    /**
     * Initializes the components of the view.
     */
    private void initComponents() {
        setBackground(Color.darkGray);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        initPlayerNameLabel(gbc);
        initGridLabels(gbc);
        initGridPanels();
        initButtons(gbc);
    }

    /**
     * Initializes the player name label.
     * 
     * @param gbc The GridBagConstraints for the label.
     */
    private void initPlayerNameLabel(GridBagConstraints gbc) {
        playerName = new JLabel(currentPlayer.getName(), SwingConstants.CENTER);
        playerName.setFont(new Font("Roboto", Font.BOLD, 40));
        playerName.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(playerName, gbc);
    }

    /**
     * Initializes the grid labels.
     * 
     * @param gbc The GridBagConstraints for the labels.
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
     * Initializes the grid panels for both players.
     */
    private void initGridPanels() {
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

        drawShipsOnOwnBoard();
        drawTargetBoard();
    }

    /**
     * Initializes the individual grid cells within the panels.
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
                cell2.addMouseListener(new GridClickListener(i, j, 2));
                gridCells2[i][j] = cell2;
                gridPanel2.add(cell2);
            }
        }
    }

    /**
     * Initializes the buttons for the view.
     * 
     * @param gbc The GridBagConstraints for the buttons.
     */
    private void initButtons(GridBagConstraints gbc) {
        initNextPlayerButton(gbc);

        JButton backButton = new JButton("Spiel beenden");
        backButton.addActionListener(e -> battleshipGUI.showMainMenuView());
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(backButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(gridPanel1, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(gridPanel2, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(nextPlayerButton, gbc);
    }

    /**
     * Initializes the "Next Player" button.
     * 
     * @param gbc The GridBagConstraints for the button.
     */
    private void initNextPlayerButton(GridBagConstraints gbc) {
        nextPlayerButton = new JButton("Nächster Spieler");
        nextPlayerButton.addActionListener(e -> handleNextPlayerClick());
        nextPlayerButton.setFont(new Font("Roboto", Font.BOLD, 20));
        nextPlayerButton.setBackground(Color.WHITE);
        nextPlayerButton.setForeground(Color.BLACK);
        nextPlayerButton.setPreferredSize(new Dimension(200, 50));
        nextPlayerButton.setVisible(false);
    }

    /**
     * Handles the click on the "Next Player" button.
     */
    private void handleNextPlayerClick() {
        shootingManager.switchPlayers();
        clearGrids();
        drawShipsOnOwnBoard();
        drawTargetBoard();
        revalidate();
        repaint();
        clicksAllowed = true;
        nextPlayerButton.setVisible(false);
    }

    /**
     * Creates the column labels (A-J) for the grid.
     * 
     * @return A panel containing the column labels.
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
     * Creates the row labels (1-10) for the grid.
     * 
     * @return A panel containing the row labels.
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
     * Draws the player's own ships on their game board grid.
     */
    private void drawShipsOnOwnBoard() {
        Map<Point, IShip> ships = currentGameBoard.getShipLocations();
        Map<Point, IHits> hits = opponentPlayer.getTargetingBoard().getHits();
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
     * Draws the target board grid, showing hits and misses.
     */
    private void drawTargetBoard() {
        Map<Point, IHits> hits = currentTargetingBoard.getHits();
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
     * Draws a miss marker (animated water) on the target board.
     * 
     * @param r The row of the cell to draw the marker in.
     * @param c The column of the cell to draw the marker in.
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
     * Draws a cross through a sunk ship on the target board.
     * 
     * @param coordinates The list of coordinates that make up the sunk ship.
     */
    private void drawLineThroughSunkShip(List<Point> coordinates) {
        for (Point point : coordinates) {
            gridCells2[point.y][point.x].setSunk(true);
        }
    }

    /**
     * Clears both game board grids, resetting them to their initial states.
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
     *        Listener for grid cell clicks, handling shot attempts.
     *        Extends {@link MouseAdapter} for mouse click events.
     */
    private class GridClickListener extends MouseAdapter {
        private final int row;
        private final int col;

        /**
         * Constructor for GridClickListener.
         *
         * @param row        The row of the grid cell.
         * @param col        The column of the grid cell.
         * @param playerGrid The player grid identifier (not used in this
         *                   implementation).
         */
        public GridClickListener(int row, int col, int playerGrid) {
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
         * Handles the logic for a grid cell click, representing a shot attempt.
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

            if (shootingManager.isGameOver()) {
                handleGameOver();
            }

            if (!isOnePlayerDebug) {
                clicksAllowed = false;
                nextPlayerButton.setVisible(true);
            }
        }

        /**
         * Handles a successful hit event, updating the grid and checking for a sunk
         * ship.
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
         * Handles a miss event, updating the grid with a miss marker.
         */
        private void handleMiss() {
            drawMissMarker(row, col);
        }

        /**
         * Handles the end of the game, declaring the winner and returning to the main
         * menu.
         */
        private void handleGameOver() {
            JOptionPane.showMessageDialog(null, "Spiel vorbei! " + currentPlayer.getName() + " hat gewonnen!");
            battleshipGUI.showMainMenuView();
        }

    }

    /**
     * @class LinePanel
     *        A custom JPanel that can draw a line through it to indicate a sunk
     *        ship.
     */
    class LinePanel extends JPanel {
        /**
         * SerialVersionUID for the LinePanel class.
         */
        private static final long serialVersionUID = 8261635737354447451L;
        private boolean isSunk = false;

        /**
         * Sets the sunk state of the panel.
         * 
         * @param isSunk True if the panel represents a sunk ship cell, false otherwise.
         */
        public void setSunk(boolean isSunk) {
            this.isSunk = isSunk;
            repaint();
        }

        /**
         * Overrides the paintComponent method to draw the line if isSunk is true.
         * 
         * @param g The Graphics object for drawing.
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
     *        A custom JPanel that draws a semi-transparent overlay.
     *        Used to indicate hits on the player's own board.
     */
    class TransparentPanel extends JPanel {
        /**
         * SerialVersionUID for the TransparentPanel class.
         */
        private static final long serialVersionUID = -9012335818565252012L;
        private final Color overlayColor;

        /**
         * Constructor for TransparentPanel.
         * 
         * @param overlayColor The color of the semi-transparent overlay.
         */
        public TransparentPanel(Color overlayColor) {
            this.overlayColor = overlayColor;
            setOpaque(false);
        }

        /**
         * Overrides the paintComponent to draw the semi-transparent overlay.
         * 
         * @param g The Graphics object for drawing.
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
