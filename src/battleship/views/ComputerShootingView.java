/**
 * @file ComputerShootingView.java
 *   Represents the shooting view in the Battleship game.
 */

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
import java.io.File;
import java.util.Map;
import java.util.List;

/**
 * @class ComputerShootingView
 *        Represents the shooting view in the Battleship game.
 *        Extends {@link JPanel} to create a custom panel for shooting actions.
 */
public class ComputerShootingView extends JPanel {
    private JPanel gridPanel1;
    /** < Panel for player 1's ships */
    private JPanel gridPanel2;
    /** < Panel for player 2's ships */
    private JPanel[][] gridCells1;
    /** < 2D array of grid cells for player 1 */
    private LinePanel[][] gridCells2;

    private JButton nextPlayerButton;
    private ComputerShootingManager shootingManager;

    private IPlayer player;
    private IPlayer computer;
    private JLabel playerName;

    private boolean clicksAllowed = true;

    private boolean isOnePlayerDebug = false;

    private BattleshipGUI battleshipGUI;
    private IPlayer startplayer;

    /** < The opponent's game board */

    /**
     * Constructor for ComputerShootingView.
     * 
     * @param player1Board          The game board for player 1.
     * @param player1TargetingBoard The targeting board for player 1.
     * @param player2Board          The game board for player 2.
     * @param player2TargetingBoard The targeting board for player 2.
     */
    public ComputerShootingView(IPlayer player1, IPlayer computer, boolean isOnePlayerDebug,
            BattleshipGUI battleshipGUI) {

        this.shootingManager = new ComputerShootingManager(player1, computer);

        this.player = player1;
        this.computer = computer;

        this.isOnePlayerDebug = isOnePlayerDebug;
        this.battleshipGUI = battleshipGUI;

        initComponents();
    }

    class LinePanel extends JPanel {
        private boolean isSunk = false;

        public void setSunk(boolean isSunk) {
            this.isSunk = isSunk;
            repaint();
        }

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

    class TransparentPanel extends JPanel {
        private Color overlayColor;
    
        public TransparentPanel(Color overlayColor) {
            this.overlayColor = overlayColor;
            setOpaque(false);
        }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(overlayColor);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
    }

    /**
     * Initializes the components of the view.
     */
    private void initComponents() {
        setBackground(Color.darkGray);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        playerName = new JLabel(player.getName(), SwingConstants.CENTER);
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

        gridCells1 = new JPanel[10][10];
        gridCells2 = new LinePanel[10][10];

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

        JPanel gridWithLabels1 = new JPanel(new BorderLayout());
        gridWithLabels1.add(createColumnLabels(), BorderLayout.NORTH);
        gridWithLabels1.add(createRowLabels(), BorderLayout.WEST);
        gridWithLabels1.add(gridPanel1, BorderLayout.CENTER);

        JPanel gridWithLabels2 = new JPanel(new BorderLayout());
        gridWithLabels2.add(createColumnLabels(), BorderLayout.NORTH);
        gridWithLabels2.add(createRowLabels(), BorderLayout.WEST);
        gridWithLabels2.add(gridPanel2, BorderLayout.CENTER);

        startplayer = shootingManager.selectRandomPlayer();

        if (startplayer == player) {
            JOptionPane.showMessageDialog(ComputerShootingView.this, "Du beginnst!");
            clicksAllowed = true;
        } else {
            clicksAllowed = false;
            JOptionPane.showMessageDialog(ComputerShootingView.this, "Der Computer beginnt!");
            shootingManager.computerShoot();
            clicksAllowed = true;
        }

        drawShipsOnOwnBoard();
        drawTargetBoard();

        nextPlayerButton = new JButton("Weiter");
        nextPlayerButton.addActionListener(e -> {
            shootingManager.computerShoot();
            clearGrids();
            drawShipsOnOwnBoard();
            drawTargetBoard();
            revalidate();
            repaint();
            if (shootingManager.isGameOver(computer)) {
                JOptionPane.showMessageDialog(null, "Spiel vorbei! " + computer.getName() + " hat gewonnen!");
                battleshipGUI.showMainMenuView();

            }
            clicksAllowed = true;
            if (isOnePlayerDebug) {
                clicksAllowed = false;
                nextPlayerButton.setVisible(true);
            } else {
                clicksAllowed = true;
                nextPlayerButton.setVisible(false);
            }
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
        private  int row;
        /** < The row of the grid cell */
        private int col;
        /** < The column of the grid cell */

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
                List<Point> sunkShipCoordinates = shootingManager.isShipSunk(col, row);
                if (!sunkShipCoordinates.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Schiff versenkt!");
                    drawLineThroughSunkShip(sunkShipCoordinates);
                }
            } else {
                try {
                    ImageIcon gifIcon = new ImageIcon(getClass().getResource("water_tile.gif"));
                    if (gifIcon.getIconWidth() == -1) {
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

            if (shootingManager.isGameOver(player)) {
                JOptionPane.showMessageDialog(null, "Spiel vorbei! " + player.getName() + " hat gewonnen!");
                battleshipGUI.showMainMenuView();

            }
            clicksAllowed = false;
            nextPlayerButton.setVisible(true);

        }
    }

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

    private void drawLineThroughSunkShip(List<Point> coordinates) {
        for (Point point : coordinates) {
            gridCells2[point.y][point.x].setSunk(true);
        }
    }

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
        }
    }


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

}