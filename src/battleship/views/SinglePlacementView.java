package battleship.views;

import battleship.BattleshipGUI;
import battleship.factorys.player.IPlayer;
import battleship.managers.SinglePlacementManager;
import battleship.factorys.ships.IShip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @class SinglePlacementView
 *        Represents the ship placement view for computer player mode.
 *        Extends {@link JPanel} for custom ship placement.
 */
public class SinglePlacementView extends JPanel {
    /**
     * SerialVersionUID for SinglePlacementView class.
     */
    private static final long serialVersionUID = 4946862325911538781L;
    private JPanel gridPanel;
    private JPanel[][] gridCells;
    private JPanel parentPanel;
    private SinglePlacementManager shipPlacementManager;
    private BattleshipGUI battleshipGUI;
    private JLabel[] shipCountLabels;
    private IPlayer player1;

    /**
     * Constructor for SinglePlacementView.
     *
     * @param parentPanel   The parent panel containing this view.
     * @param player1       The human player.
     * @param computer      The computer player.
     * @param battleshipGUI The main GUI.
     */
    public SinglePlacementView(JPanel parentPanel,
            IPlayer player1, IPlayer computer, BattleshipGUI battleshipGUI) {
        this.parentPanel = parentPanel;
        this.battleshipGUI = battleshipGUI;
        this.shipPlacementManager = new SinglePlacementManager(player1, this, battleshipGUI);
        this.player1 = player1;

        initComponents();
    }

    /**
     * Initializes the components of the view.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.darkGray);

        JButton backButton = new JButton("Spiel beenden");
        backButton.addActionListener(
                e -> ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showMainMenuView());

        initGridPanel();

        JPanel placementWithLabels = new JPanel(new BorderLayout());
        placementWithLabels.add(createColumnLabels(), BorderLayout.NORTH);
        placementWithLabels.add(createRowLabels(), BorderLayout.WEST);
        placementWithLabels.add(gridPanel, BorderLayout.CENTER);
        placementWithLabels.setBackground(Color.darkGray);

        JLabel instructionLabel = new JLabel(player1.getName() + ", platziere deine Schiffe.", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        instructionLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel("Schiffe platzieren. Klicke dafür auf eine Zelle...", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JButton rulesButton = new JButton("Regeln");
        rulesButton.addActionListener(e -> showRulesDialog());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(instructionLabel, gbc);
        mainPanel.setBackground(Color.darkGray);

        gbc.gridy = 1;
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 2;
        mainPanel.add(placementWithLabels, gbc);

        JPanel shipListPanel = createShipListPanel();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(shipListPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(rulesButton, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(backButton, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the grid panel and cells.
     */
    private void initGridPanel() {
        gridPanel = new JPanel(new GridLayout(10, 10));
        gridPanel.setPreferredSize(new Dimension(500, 500));
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
    }

    /**
     * Shows the game rules dialog.
     */
    private void showRulesDialog() {
        JDialog dialog = new JDialog(battleshipGUI, "Regeln", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(battleshipGUI);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setBackground(Color.darkGray);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.darkGray);
        addRulesToDialog(contentPanel);

        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    /**
     * Adds the rules to the dialog.
     * 
     * @param contentPanel The panel to add the rules to.
     */
    private void addRulesToDialog(JPanel contentPanel) {
        String[] rules = {
                "Regeln:",
                "1. Jeder Spieler platziert abwechselnd seine Schiffe auf dem Spielfeld.",
                "2. Die Schiffe dürfen nicht überlappen oder aneinander angrenzen.",
                "3. Die Schiffe dürfen nicht diagonal platziert werden.",
                "4. Der Spieler, der alle Schiffe des Gegners versenkt hat, gewinnt.",
                "5. Klicke auf eine Zelle, um ein Schiff zu platzieren oder zu entfernen.",
                "6. Jeder Spieler darf ein Schlachtschiff, zwei Kreuzer, drei Zerstörer und vier U-Boote platzieren.",
                "7. Viel Spaß!"
        };

        for (String rule : rules) {
            JLabel label = new JLabel(rule);
            label.setFont(new Font("Roboto", Font.BOLD, 16));
            label.setForeground(Color.WHITE);
            contentPanel.add(label);
        }
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
     * Creates the ship list panel showing available ships.
     * 
     * @return The panel containing the ship list.
     */
    private JPanel createShipListPanel() {
        JPanel shipListPanel = new JPanel();
        shipListPanel.setLayout(new BoxLayout(shipListPanel, BoxLayout.Y_AXIS));
        shipListPanel.setBackground(Color.darkGray);

        String[] shipNames = { "Schlachtschiff", "Kreuzer", "Zerstörer", "U-Boot" };
        int[] shipCounts = { 1, 2, 3, 4 };
        shipCountLabels = new JLabel[shipNames.length];

        JLabel titleLabel = new JLabel("Noch zu verteilende Schiffe:");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        shipListPanel.add(titleLabel);

        shipListPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        for (int i = 0; i < shipNames.length; i++) {
            createShipCountLabel(shipNames[i], shipCounts[i], shipListPanel);
        }

        return shipListPanel;
    }

    /**
     * Creates and adds a label for a ship type and its count.
     * 
     * @param shipName      The name of the ship type.
     * @param shipCount     The initial count of the ship type.
     * @param shipListPanel The panel to add the label to.
     */
    private void createShipCountLabel(String shipName, int shipCount, JPanel shipListPanel) {
        JPanel shipPanel = new JPanel(new BorderLayout());
        shipPanel.setBackground(Color.darkGray);

        JLabel nameLabel = new JLabel(shipName + ": ");
        nameLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);

        int index = shipCount - 1;
        shipCountLabels[index] = new JLabel("x" + shipCount);
        shipCountLabels[index].setFont(new Font("Roboto", Font.BOLD, 16));
        shipCountLabels[index].setForeground(Color.WHITE);

        shipPanel.add(nameLabel, BorderLayout.WEST);
        shipPanel.add(shipCountLabels[index], BorderLayout.EAST);

        shipListPanel.add(shipPanel);
        shipListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    /**
     * Updates the displayed count of a ship type.
     * 
     * @param shipName The name of the ship type to update.
     * @param delta    The amount to change the count by.
     */
    private void updateShipCount(String shipName, int delta) {
        String[] shipNames = { "Schlachtschiff", "Kreuzer", "Zerstörer", "U-Boot" };
        for (int i = 0; i < shipNames.length; i++) {
            if (shipNames[i].equals(shipName)) {
                int currentCount = Integer.parseInt(shipCountLabels[i].getText().substring(1));
                shipCountLabels[i].setText("x" + (currentCount + delta));
                break;
            }
        }
    }

    /**
     * @class GridCellClickListener
     *        Listener for grid cell clicks, handling ship placement/removal.
     *        Extends {@link MouseAdapter} for mouse click events.
     */
    private class GridCellClickListener extends MouseAdapter {
        private final int row;
        private final int col;

        /**
         * Constructor for GridCellClickListener.
         *
         * @param row The row of the grid cell.
         * @param col The column of the grid cell.
         */
        public GridCellClickListener(int row, int col) {
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
            handleGridCellClick();
        }

        /**
         * Handles a grid cell click, either placing or removing a ship.
         */
        private void handleGridCellClick() {
            IShip existingShip = shipPlacementManager.getShipAt(row, col);
            if (existingShip != null) {
                handleExistingShipClick(existingShip);
                return;
            }

            showShipPlacementDialog();
        }

        /**
         * Handles click on a cell with an existing ship, prompting for removal.
         * 
         * @param existingShip The ship currently at the clicked cell.
         */
        private void handleExistingShipClick(IShip existingShip) {
            int confirm = JOptionPane.showConfirmDialog(SinglePlacementView.this,
                    "An dieser Stelle befindet sich bereits ein Schiff. Möchtest du es löschen?",
                    "Schiff löschen", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                shipPlacementManager.removeShip(row, col, existingShip, gridCells);
                gridCells[row][col].setBackground(null);
                updateShipCount(existingShip.getShipName(), 1);
            }
        }

        /**
         * Displays a dialog to choose ship type and orientation for placement.
         */
        private void showShipPlacementDialog() {
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JLabel sizeLabel = new JLabel("Wähle ein Schiff aus:");
            String[] shipOptions = { "Schlachtschiff", "Kreuzer", "Zerstörer", "U-Boot" };
            JComboBox<String> sizeComboBox = new JComboBox<>(shipOptions);
            JLabel orientationLabel = new JLabel("Ausrichtung auswählen:");
            String[] orientationOptions = { "Horizontal", "Vertikal" };
            JComboBox<String> orientationComboBox = new JComboBox<>(orientationOptions);

            panel.add(sizeLabel);
            panel.add(sizeComboBox);
            panel.add(orientationLabel);
            panel.add(orientationComboBox);

            int result = JOptionPane.showConfirmDialog(SinglePlacementView.this, panel, "Schiff platzieren",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                handleShipPlacement(sizeComboBox, orientationComboBox);
            }
        }

        /**
         * Handles the ship placement logic based on dialog selections.
         * 
         * @param sizeComboBox        The combo box for selecting ship type.
         * @param orientationComboBox The combo box for selecting orientation.
         */
        private void handleShipPlacement(JComboBox<String> sizeComboBox, JComboBox<String> orientationComboBox) {
            String selectedShip = (String) sizeComboBox.getSelectedItem();
            int shipSize = shipPlacementManager.getShipSize(selectedShip);
            boolean isHorizontal = orientationComboBox.getSelectedItem().equals("Horizontal");

            if (shipPlacementManager.canPlaceShip(selectedShip)) {
                try {
                    shipPlacementManager.placeShip(row, col, shipSize, isHorizontal, selectedShip, gridCells);
                    updateShipCount(selectedShip, -1);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(SinglePlacementView.this, ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(SinglePlacementView.this,
                        "Du hast bereits das Maximum dieses Typs platziert.");
            }
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
}
