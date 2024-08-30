/**
 * @file PlacementView.java
 *   Represents the ship placement view in the Battleship game.
 */

package battleship.views;

import battleship.BattleshipGUI;
import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.player.IPlayer;
import battleship.managers.BattleshipAI;
import battleship.factorys.ships.IShip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @class PlacementView
 *        Represents the ship placement view in the Battleship game.
 *        Extends {@link JPanel} to create a custom panel for ship placement.
 */
public class ComputerDebugView extends JPanel {
    private JPanel gridPanel;
    /** < Panel for the grid layout */
    private JPanel[][] gridCells;
    /** < 2D array of grid cells */
    private CardLayout cardLayout;
    /** < Card layout for switching views */
    private JPanel parentPanel;
    /** < Parent panel containing this view */
    private BattleshipAI ai;
    /** < Manager for ship placement */
    private IPlayer computer;
    /** < The second player */

    /** < Total number of ships to be placed */

    /**
     * Constructor for PlacementView.
     * 
     * @param cardLayout    The card layout for switching views.
     * @param parentPanel   The parent panel containing this view.
     * @param player1Board  The game board for player 1.
     * @param player2Board  The game board for player 2.
     * @param player1       The first player.
     * @param computer      The second player.
     * @param battleshipGUI The main GUI.
     */
    public ComputerDebugView(CardLayout cardLayout, JPanel parentPanel, IGameBoard player1Board,
            IGameBoard player2Board,
            IPlayer player1, IPlayer computer, BattleshipGUI battleshipGUI) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.ai = new BattleshipAI(player1, computer);
        this.computer = computer;

        initComponents();
    }

    /**
     * Initializes the components of the view.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JButton backButton = new JButton("Zurück");
        backButton.addActionListener(e -> cardLayout.show(parentPanel, "MainMenuView"));
        add(backButton, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(10, 10));
        gridPanel.setPreferredSize(new Dimension(600, 600));
        gridCells = new JPanel[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                cell.addMouseListener(new GridCellClickListener(i, j));
                gridCells[i][j] = cell;
                gridPanel.add(cell);
            }
        }

        JPanel placementWithLabels = new JPanel(new BorderLayout());
        placementWithLabels.add(createColumnLabels(), BorderLayout.NORTH);
        placementWithLabels.add(createRowLabels(), BorderLayout.WEST);
        placementWithLabels.add(gridPanel, BorderLayout.CENTER);
        placementWithLabels.setBackground(Color.darkGray);

        JButton rulesButton = new JButton("Computer Schiffe platzieren");
        rulesButton.addActionListener(e -> {
            clearComputerGameboard();
            clearGrid();
            ai.placeAllShips();
            drawShips();
        });

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.setBackground(Color.darkGray);

        gbc.gridy = 2;
        mainPanel.add(placementWithLabels, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(rulesButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
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
     * @class GridCellClickListener
     *        Listener for grid cell clicks.
     *        Extends {@link MouseAdapter} to handle mouse click events on grid
     *        cells.
     */
    private class GridCellClickListener extends MouseAdapter {


        /** < The column of the grid cell */

        /**
         * Constructor for GridCellClickListener.
         * 
         * @param row The row of the grid cell.
         * @param col The column of the grid cell.
         */
        public GridCellClickListener(int row, int col) {
        }

        /**
         * Handles mouse click events on grid cells.
         * 
         * @param e The mouse event.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
        }
    }

    /**
     * Shows the ship locations for both players.
     */
    private void drawShips() {
        Map<Point, IShip> ships = computer.getGameBoard().getShipLocations();
        // Log the ship locations

        for (Map.Entry<Point, IShip> entry : ships.entrySet()) {
            Point point = entry.getKey();
            int r = point.x;
            int c = point.y;
            gridCells[r][c].setBackground(Color.GRAY);
        }
    }

    /**
     * Clears the grid by resetting the background color of all cells.
     */
    public void clearGrid() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridCells[i][j].setBackground(null);
            }
        }
    }

    private void clearComputerGameboard() {
        Map<Point, IShip> shipLocations = computer.getGameBoard().getShipLocations();

        if (shipLocations.isEmpty()) {
            return;
        }

        Set<IShip> uniqueShips = new HashSet<>(shipLocations.values());

        for (IShip ship : uniqueShips) {
            computer.getGameBoard().removeShip(ship);
        }
    }
}