package battleship.views;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.player.IPlayer;
import battleship.managers.ShipPlacementManager;
import battleship.factorys.ships.IShip;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class PlacementView extends JPanel {
    private JPanel gridPanel;
    private JPanel[][] gridCells;
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private ShipPlacementManager shipPlacementManager;

    private static final int TOTAL_SHIPS = 10;

    public PlacementView(CardLayout cardLayout, JPanel parentPanel, IGameBoard player1Board, IGameBoard player2Board,
                         IPlayer player1, IPlayer player2) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.shipPlacementManager = new ShipPlacementManager(player1, player2);

        initComponents();
    }

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

        JLabel titleLabel = new JLabel("Schiffe platzieren. Klicke dafür auf eine Zelle...", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(titleLabel, gbc);
        mainPanel.setBackground(Color.darkGray);

        gbc.gridy = 1;
        mainPanel.add(placementWithLabels, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

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

    private class GridCellClickListener extends MouseAdapter {
        private final int row;
        private final int col;

        public GridCellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (shipPlacementManager.getPlacedShipsCount() >= TOTAL_SHIPS) {
                if (shipPlacementManager.getCurrentPlayer() == "player1") {
                    clearGrid();
                    JOptionPane.showMessageDialog(PlacementView.this,
                            "Spieler 1 hat alle Schiffe platziert. Nun ist Spieler 2 drann.");
                } else {
                    JOptionPane.showMessageDialog(PlacementView.this, "Schiffsplatzierung abgeschlossen.");
                    showShipLocations();
                    cardLayout.show(parentPanel, "ShootingView");
                }
                return;
            }

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

            int result = JOptionPane.showConfirmDialog(PlacementView.this, panel, "Schiff platzieren",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String selectedShip = (String) sizeComboBox.getSelectedItem();
                int shipSize = shipPlacementManager.getShipSize(selectedShip);
                boolean isHorizontal = orientationComboBox.getSelectedItem().equals("Horizontal");

                if (shipPlacementManager.canPlaceShip(selectedShip)) {
                    try {
                        shipPlacementManager.placeShip(row, col, shipSize, isHorizontal, selectedShip, gridCells);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(PlacementView.this, ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(PlacementView.this,
                            "Du hast bereits das Maximum dieses Typs platziert.");
                }
            }
        }
    }

    private void showShipLocations() {
        Map<Point, IShip> player1Ships = shipPlacementManager.getPlayer1ShipLocations();
        Map<Point, IShip> player2Ships = shipPlacementManager.getPlayer2ShipLocations();

        // Display or process the ship locations as needed
        System.out.println("Player 1 Ships: " + player1Ships);
        System.out.println("Player 2 Ships: " + player2Ships);
    }

    private void clearGrid() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridCells[i][j].setBackground(null);
            }
        }
    }
}