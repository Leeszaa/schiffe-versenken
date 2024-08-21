package battleship.views;

import javax.swing.*;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.player.IPlayer;
import battleship.factorys.ships.SchlachtschiffFactory;
import battleship.factorys.ships.IShip;
import battleship.factorys.ships.KreuzerFactory;
import battleship.factorys.ships.ShipFactory;
import battleship.factorys.ships.U_BootFactory;
import battleship.factorys.ships.ZerstörerFactory;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class PlacementView extends JPanel {
    private JPanel gridPanel;
    private JPanel[][] gridCells;
    private boolean isPlayer1Turn = true;
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private IGameBoard player1Board;
    private IGameBoard player2Board;
    private Map<String, Integer> currentPlayerShipCounts;
    private Map<String, Integer> shipLimits;

    private ZerstörerFactory zerstörerFactory;
    private SchlachtschiffFactory schlachtschiffFactory;
    private KreuzerFactory kreuzerFactory;
    private U_BootFactory u_BootFactory;
    private IPlayer player1;
    private IPlayer player2;

    private static final int TOTAL_SHIPS = 5;

    public PlacementView(CardLayout cardLayout, JPanel parentPanel, IGameBoard player1Board, IGameBoard player2Board,
            IPlayer player1, IPlayer player2) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.player1Board = player1Board;
        this.player2Board = player2Board;
        this.player1 = player1;
        this.player2 = player2;

        this.zerstörerFactory = new ZerstörerFactory();
        this.schlachtschiffFactory = new SchlachtschiffFactory();
        this.kreuzerFactory = new KreuzerFactory();
        this.u_BootFactory = new U_BootFactory();

        currentPlayerShipCounts = new HashMap<>();
        shipLimits = new HashMap<>();
        initializeShipLimits();
        initComponents();
    }

    private void initializeShipLimits() {
        shipLimits.put("Schlachtschiff", schlachtschiffFactory.getShipLimit());
        shipLimits.put("Kreuzer", kreuzerFactory.getShipLimit());
        shipLimits.put("Zerstörer", zerstörerFactory.getShipLimit());
        shipLimits.put("U-Boot", u_BootFactory.getShipLimit());
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
            if (getPlacedShipsCount() >= TOTAL_SHIPS) {
                if (isPlayer1Turn) {
                    isPlayer1Turn = false;
                    currentPlayerShipCounts = new HashMap<>();
                    clearGrid();
                    JOptionPane.showMessageDialog(PlacementView.this,
                            "Spieler 1 hat alle Schiffe platziert. Nun ist Spieler 2 drann.");
                } else {
                    JOptionPane.showMessageDialog(PlacementView.this, "Schiffsplatzierung abgeschlossen.");
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
                int shipSize = getShipSize(selectedShip);
                boolean isHorizontal = orientationComboBox.getSelectedItem().equals("Horizontal");

                if (canPlaceShip(selectedShip)) {
                    placeShip(row, col, shipSize, isHorizontal, selectedShip);
                } else {
                    JOptionPane.showMessageDialog(PlacementView.this,
                            "Du hast bereits das Maximum dieses Typs platziert.");
                }
            }
        }
    }

    private int getShipSize(String shipType) {
        return switch (shipType) {
            case "Schlachtschiff" -> schlachtschiffFactory.getShipSize();
            case "Kreuzer" -> kreuzerFactory.getShipSize();
            case "Zerstörer" -> zerstörerFactory.getShipSize();
            case "U-Boot" -> u_BootFactory.getShipSize();
            default -> 0;
        };
    }

    private boolean canPlaceShip(String shipType) {
        int currentCount = currentPlayerShipCounts.getOrDefault(shipType, 0);
        int maxCount = shipLimits.get(shipType);
        return currentCount < maxCount;
    }

    private int getPlacedShipsCount() {
        int totalCount = 0;
        for (int count : currentPlayerShipCounts.values()) {
            totalCount += count;
        }
        return totalCount;
    }

    private void placeShip(int row, int col, int size, boolean isHorizontal, String shipType) {
        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || isAdjacentToShip(r, c)) {
                JOptionPane.showMessageDialog(this, "Platzierung ungültig.");
                return;
            }
        }
        try {
            switch (shipType) {
                case "Schlachtschiff" ->
                    player1.getGameBoard().placeShip(col, row, schlachtschiffFactory.createShip(), isHorizontal);
                case "Kreuzer" -> player1.getGameBoard().placeShip(col, row, kreuzerFactory.createShip(), isHorizontal);
                case "Zerstörer" ->
                    player1.getGameBoard().placeShip(col, row, zerstörerFactory.createShip(), isHorizontal);
                case "U-Boot" -> player1.getGameBoard().placeShip(col, row, u_BootFactory.createShip(), isHorizontal);
            }

            for (int i = 0; i < size; i++) {
                int r = isHorizontal ? row : row + i;
                int c = isHorizontal ? col + i : col;
                gridCells[r][c].setBackground(Color.GRAY);
            }

            currentPlayerShipCounts.put(shipType, currentPlayerShipCounts.getOrDefault(shipType, 0) + 1);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private boolean isAdjacentToShip(int row, int col) {
        if (isPlayer1Turn) {
            Map<Point, IShip> ships = player1.getGameBoard().getShipLocations();
            return isAdjacent(row, col, ships);
        } else {
            Map<Point, IShip> ships = player2.getGameBoard().getShipLocations();
            return isAdjacent(row, col, ships);
        }
    }

    private boolean isAdjacent(int row, int col, Map<Point, IShip> ships) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < 10 && c >= 0 && c < 10 && ships.containsKey(new Point(c, r))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clearGrid() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                gridCells[i][j].setBackground(null);
            }
        }
    }
}