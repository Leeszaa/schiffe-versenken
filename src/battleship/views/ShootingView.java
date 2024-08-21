/**
 * @file ShootingView.java
 * @brief Represents the shooting view in the Battleship game.
 */

package battleship.views;

import javax.swing.*;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.ships.*;
import battleship.managers.ShootingManager;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * @class ShootingView
 * @brief Represents the shooting view in the Battleship game.
 * Extends {@link JPanel} to create a custom panel for shooting actions.
 */
public class ShootingView extends JPanel {
    private JPanel gridPanel1; /**< Panel for player 1's ships */
    private JPanel gridPanel2; /**< Panel for player 2's ships */
    private JPanel[][] gridCells1; /**< 2D array of grid cells for player 1 */
    private JPanel[][] gridCells2; /**< 2D array of grid cells for player 2 */
    private IGameBoard player1Board; /**< Game board for player 1 */
    private IGameBoard player2Board; /**< Game board for player 2 */
    private IGameBoard player1TargetingBoard; /**< Targeting board for player 1 */
    private IGameBoard player2TargetingBoard; /**< Targeting board for player 2 */
    private boolean isPlayer1Turn = true; /**< Flag to indicate if it's player 1's turn */
    private ShootingManager shootingManager; /**< Manager for handling shooting actions */

    /**
     * @brief Constructor for ShootingView.
     * @param player1Board The game board for player 1.
     * @param player1TargetingBoard The targeting board for player 1.
     * @param player2Board The game board for player 2.
     * @param player2TargetingBoard The targeting board for player 2.
     */
    public ShootingView(IGameBoard player1Board, IGameBoard player1TargetingBoard, IGameBoard player2Board, IGameBoard player2TargetingBoard) {
        this.player1Board = player1Board;
        this.player1TargetingBoard = player1TargetingBoard;
        this.player2Board = player2Board;
        this.player2TargetingBoard = player2TargetingBoard;
        this.shootingManager = new ShootingManager();

        initComponents();
    }

    /**
     * @brief Initializes the components of the view.
     */
    private void initComponents() {
        setBackground(Color.darkGray);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel label1 = new JLabel("Eigene Schiffe", SwingConstants.CENTER);
        label1.setFont(new Font("Roboto", Font.BOLD, 20));
        label1.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(label1, gbc);

        JLabel label2 = new JLabel("Gegenerische Schiffe", SwingConstants.CENTER);
        label2.setFont(new Font("Roboto", Font.BOLD, 20));
        label2.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(label2, gbc);

        gridPanel1 = new JPanel(new GridLayout(10, 10));
        gridPanel2 = new JPanel(new GridLayout(10, 10));
        gridPanel1.setPreferredSize(new Dimension(600, 600));
        gridPanel2.setPreferredSize(new Dimension(600, 600));

        gridCells1 = new JPanel[10][10];
        gridCells2 = new JPanel[10][10];

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

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(gridWithLabels1, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(gridWithLabels2, gbc);
    }

    /**
     * @brief Creates the column labels for the grid.
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
     * @brief Creates the row labels for the grid.
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
     * @brief Listener for grid cell clicks.
     * Extends {@link MouseAdapter} to handle mouse click events on grid cells.
     */
    private class GridClickListener extends MouseAdapter {
        private final int row; /**< The row of the grid cell */
        private final int col; /**< The column of the grid cell */
        private final int playerGrid; /**< The player grid identifier */

        /**
         * @brief Constructor for GridClickListener.
         * @param row The row of the grid cell.
         * @param col The column of the grid cell.
         * @param playerGrid The player grid identifier.
         */
        public GridClickListener(int row, int col, int playerGrid) {
            this.row = row;
            this.col = col;
            this.playerGrid = playerGrid;
        }

        /**
         * @brief Handles mouse click events on grid cells.
         * @param e The mouse event.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            // Handle click event
        }
    }
}