/**
 * @file ShootingView.java
 *   Represents the shooting view in the Battleship game.
 */

package battleship.views;

import javax.swing.*;

import battleship.BattleshipGUI;
import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.ships.*;
import battleship.managers.ShootingManager;
import battleship.managers.ShootingManagerObserver;
import battleship.factorys.player.IPlayer;
import battleship.factorys.hits.IHits;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @class ShootingView
 *        Represents the shooting view in the Battleship game.
 *        Extends {@link JPanel} to create a custom panel for shooting actions.
 */
public class ShootingView extends JPanel implements ShootingManagerObserver {
    private JPanel gridPanel1;
    /** < Panel for player 1's ships */
    private JPanel gridPanel2;
    /** < Panel for player 2's ships */
    private JPanel[][] gridCells1;
    /** < 2D array of grid cells for player 1 */
    private JPanel[][] gridCells2;

    private JButton nextPlayerButton;
    /** < 2D array of grid cells for player 2 */
    private IGameBoard player1Board;
    /** < Game board for player 1 */
    private IGameBoard player2Board;
    /** < Game board for player 2 */
    private IGameBoard player1TargetingBoard;
    /** < Targeting board for player 1 */
    private IGameBoard player2TargetingBoard;
    /** < Targeting board for player 2 */
    private boolean isPlayer1Turn = true;
    /** < Flag to indicate if it's player 1's turn */
    private ShootingManager shootingManager;
    /** < Manager for handling shooting actions */
    private IPlayer player1;
    /** < The first player */
    private IPlayer player2;
    /** < The second player */
    private IGameBoard currentGameBoard;
    /** < The current game board */
    private IGameBoard currentTargetingBoard;
    /** < The current targeting board */
    private IGameBoard opponentGameBoard;

    private IPlayer currentPlayer;
    private IPlayer opponentPlayer;

    private JLabel playerName;

    private boolean clicksAllowed = true;

    private boolean isGameOver = false;

    private boolean isOnePlayerDebug = false;

    private BattleshipGUI battleshipGUI;

    /** < The opponent's game board */

    /**
     * Constructor for ShootingView.
     * 
     * @param player1Board          The game board for player 1.
     * @param player1TargetingBoard The targeting board for player 1.
     * @param player2Board          The game board for player 2.
     * @param player2TargetingBoard The targeting board for player 2.
     */
    public ShootingView(IPlayer player1, IPlayer player2, boolean isOnePlayerDebug, BattleshipGUI battleshipGUI) {

        this.shootingManager = new ShootingManager(player1, player2);
        this.shootingManager.addObserver(this);

        this.currentPlayer = shootingManager.currentPlayer;
        this.opponentPlayer = shootingManager.opponentPlayer;
        this.currentGameBoard = currentPlayer.getGameBoard();
        this.currentTargetingBoard = currentPlayer.getTargetingBoard();
        this.opponentGameBoard = opponentPlayer.getGameBoard();

        this.isOnePlayerDebug = isOnePlayerDebug;
        this.battleshipGUI = battleshipGUI;


        initComponents();
    }

    @Override
    public void onPlayerSwitched(IPlayer newPlayer, IPlayer opponentPlayer) {
        System.out.println("Current player (on Switched): " + newPlayer.getName());
        // Update the view accordingly
        this.currentPlayer = newPlayer;
        this.opponentPlayer = opponentPlayer;
        this.currentGameBoard = newPlayer.getGameBoard();
        this.currentTargetingBoard = newPlayer.getTargetingBoard();
        this.opponentGameBoard = opponentPlayer.getGameBoard();

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

        playerName = new JLabel(currentPlayer.getName(), SwingConstants.CENTER);
        playerName.setFont(new Font("Roboto", Font.BOLD, 40));
        playerName.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(playerName, gbc);

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

        gridPanel1 = new JPanel(new GridLayout(10, 10));
        gridPanel2 = new JPanel(new GridLayout(10, 10));
        gridPanel1.setPreferredSize(new Dimension(600, 600));
        gridPanel2.setPreferredSize(new Dimension(600, 600));

        gridCells1 = new JPanel[10][10]; // Eigenes Spielfeld
        gridCells2 = new JPanel[10][10]; // Gegnerisches Spielfeld

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JPanel cell1 = new JPanel();
                cell1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridCells1[i][j] = cell1;
                gridPanel1.add(cell1);

                JPanel cell2 = new JPanel();
                cell2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell2.addMouseListener(new GridClickListener(i, j, 2));
                gridCells2[i][j] = cell2;
                gridPanel2.add(cell2);
            }
        }

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

        nextPlayerButton = new JButton("Nächster Spieler");
        nextPlayerButton.addActionListener(e -> {
            shootingManager.switchPlayers();
            System.out.println("Current player: " + currentPlayer.getName());
            clearGrids();
            drawShipsOnOwnBoard();
            drawTargetBoard();
            revalidate();
            repaint();
            clicksAllowed = true;
            nextPlayerButton.setVisible(false);
        });
        nextPlayerButton.setFont(new Font("Roboto", Font.BOLD, 20));
        nextPlayerButton.setBackground(Color.WHITE);
        nextPlayerButton.setForeground(Color.BLACK);
        nextPlayerButton.setPreferredSize(new Dimension(200, 50));
        nextPlayerButton.setVisible(false);
        

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(gridWithLabels1, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(gridWithLabels2, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(nextPlayerButton, gbc);
    }

    /**
     * Creates the column labels for the grid.
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
     * Creates the row labels for the grid.
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
     * @class GridClickListener
     *        Listener for grid cell clicks.
     *        Extends {@link MouseAdapter} to handle mouse click events on grid
     *        cells.
     */
    private class GridClickListener extends MouseAdapter {
        private final int row;
        /** < The row of the grid cell */
        private final int col;
        /** < The column of the grid cell */
        private final int playerGrid;

        /** < The player grid identifier */

        /**
         * Constructor for GridClickListener.
         * 
         * @param row        The row of the grid cell.
         * @param col        The column of the grid cell.
         * @param playerGrid The player grid identifier.
         */
        public GridClickListener(int row, int col, int playerGrid) {
            this.row = row;
            this.col = col;
            this.playerGrid = playerGrid;
        }

        /**
         * Handles mouse click events on grid cells.
         * 
         * @param e The mouse event.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            // JOptionPane.showMessageDialog(null, "Clicked on row: " + row + ", col: " +
            // col);
            if (!clicksAllowed) {
                return; // If clicks are not allowed, do nothing
            }
            if (shootingManager.isAlreadyHit(col, row)) {
                JOptionPane.showMessageDialog(null, "Bereits auf dieses Feld geschossen!");
                return;
            }
            
            boolean hit = shootingManager.addHitToTargetBoard(col, row);

            if (hit) {
                gridCells2[row][col].setBackground(Color.RED);
            } else {
            try {
                File gifFile = new File("src/battleship/assets/water_tile.gif");
                if (!gifFile.exists()) {
                    System.err.println("GIF file not found: " + gifFile.getAbsolutePath());
                    return;
                }
    
                String absoluteGifPath = gifFile.getAbsolutePath();
                ImageIcon gifIcon = new ImageIcon(absoluteGifPath);
                if (gifIcon.getIconWidth() == -1) {
                    System.err.println("Failed to load GIF: " + absoluteGifPath);
                    return;
                }
    
                JLabel missLabel = new JLabel(gifIcon);
                missLabel.setHorizontalAlignment(JLabel.CENTER);
                missLabel.setVerticalAlignment(JLabel.CENTER);
    
                gridCells2[row][col].setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                gbc.weighty = 1.0;
                gbc.anchor = GridBagConstraints.CENTER;
                gridCells2[row][col].add(missLabel, gbc);
                gridCells2[row][col].revalidate();
                gridCells2[row][col].repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

            Map<Point, IShip> ships = opponentGameBoard.getShipLocations();
            // Log the ship locations
            Set<IShip> uniqueShips = new HashSet<>(ships.values());
            for (IShip remainingShip : uniqueShips) {
                Point point = getShipStartingPoint(remainingShip, ships);
                System.out.println(
                        "Gegner Schiffe: " + remainingShip.getShipName() + " at (" + point.y + ", " + point.x + ")");
            }
            
            if (shootingManager.isGameOver()) {
                isGameOver = true;
                JOptionPane.showMessageDialog(null, "Spiel vorbei! " + currentPlayer.getName() + " hat gewonnen!");
                battleshipGUI.showMainMenuView();

            }

            if (!isOnePlayerDebug) {
                clicksAllowed = false;
                nextPlayerButton.setVisible(true);
            }
            
        }
    }

    private void drawShipsOnOwnBoard() {
        Map<Point, IShip> ships = currentGameBoard.getShipLocations();
        Map<Point, IHits> hits = opponentPlayer.getTargetingBoard().getHits();
        // Log the ship locations
        Set<IShip> uniqueShips = new HashSet<>(ships.values());
        for (IShip remainingShip : uniqueShips) {
            Point point = getShipStartingPoint(remainingShip, ships);
            System.out.println("Ship: " + remainingShip.getShipName() + " at (" + point.y + ", " + point.x + ")");
        }

        for (Map.Entry<Point, IShip> entry : ships.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
            gridCells1[r][c].setBackground(Color.GRAY);
            System.out.println("Updated cell (" + r + ", " + c + ") to GRAY.");
        }

        for (Map.Entry<Point, IHits> entry : hits.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
                gridCells1[r][c].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        }
    }

    private void drawTargetBoard() {
        Map<Point, IHits> hits = currentTargetingBoard.getHits();
        for (Map.Entry<Point, IHits> entry : hits.entrySet()) {
            Point point = entry.getKey();
            int r = point.y;
            int c = point.x;
            if (entry.getValue().isHit()) {
                gridCells2[r][c].setBackground(Color.RED);
            } else {
                try {
                    File gifFile = new File("src/battleship/assets/water_tile.gif");
                    if (!gifFile.exists()) {
                        System.err.println("GIF file not found: " + gifFile.getAbsolutePath());
                        return;
                    }
        
                    String absoluteGifPath = gifFile.getAbsolutePath();
                    ImageIcon gifIcon = new ImageIcon(absoluteGifPath);
                    if (gifIcon.getIconWidth() == -1) {
                        System.err.println("Failed to load GIF: " + absoluteGifPath);
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
        }
    }

    private Point getShipStartingPoint(IShip ship, Map<Point, IShip> ships) {
        for (Map.Entry<Point, IShip> entry : ships.entrySet()) {
            if (entry.getValue().equals(ship)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void clearGrids() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridCells1[i][j].setBackground(null);
                gridCells1[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridCells1[i][j].removeAll();
                gridCells2[i][j].setBackground(null);
                gridCells2[i][j].removeAll();
            }
        }
    }
}