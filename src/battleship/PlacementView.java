package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class PlacementView extends JPanel {
    private JPanel gridPanel;
    private JPanel[][] gridCells;
    private Map<Point, Ship> currentPlayerShips;
    private Map<String, Integer> currentPlayerShipCounts;
    private boolean isPlayer1Turn = true;
    private CardLayout cardLayout;
    private JPanel parentPanel;
    private Map<Point, Ship> player1Ships; 
    private Map<Point, Ship> player2Ships;


    private static final int TOTAL_SHIPS = 4; 
    private static final Map<String, Integer> SHIP_LIMITS = new HashMap<>();

    static {
        SHIP_LIMITS.put("Schlachtschiff (5)", 1);
        SHIP_LIMITS.put("Kreuzer (4)", 2);
        SHIP_LIMITS.put("Zerstörer (3)", 3);
        SHIP_LIMITS.put("U-Boot (2)", 4);
    }

    public PlacementView(CardLayout cardLayout, JPanel parentPanel, Map<Point, Ship> player1Ships, Map<Point, Ship> player2Ships) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        this.player1Ships = player1Ships;
        this.player2Ships = player2Ships;

        currentPlayerShips = new HashMap<>();
        currentPlayerShipCounts = new HashMap<>(); 
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
            if (currentPlayerShips.size() >= TOTAL_SHIPS) {
                if (isPlayer1Turn) {
                    // Spieler 1 ist fertig, Spieler 2 ist dran
                    isPlayer1Turn = false;
                    currentPlayerShips = new HashMap<>(); // Neues Schiff-Map für Spieler 2
                    currentPlayerShipCounts = new HashMap<>(); // Neue Zählung für Spieler 2
                    clearGrid(); 
                    JOptionPane.showMessageDialog(PlacementView.this, "Spieler 1 hat alle Schiffe platziert. Nun ist Spieler 2 drann.");
                } else {
                    // Beide Spieler sind fertig
                    JOptionPane.showMessageDialog(PlacementView.this, "Schiffsplatzierung abgeschlossen.");
                    cardLayout.show(parentPanel, "ShootingView"); // Übergang zur ShootingView
                }
                return; 
            }

            JPanel panel = new JPanel(new GridLayout(2, 2));
            JLabel sizeLabel = new JLabel("Wähle ein Schiff aus:");
            String[] shipOptions = { "Schlachtschiff (5)", "Kreuzer (4)", "Zerstörer (3)", "U-Boot (2)" };
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
                    JOptionPane.showMessageDialog(PlacementView.this, "Du hast bereits das Maximum dieses Typs platziert.");
                }
            }
        }
    }

    private int getShipSize(String shipType) {
        return switch (shipType) {
            case "Schlachtschiff (5)" -> 5;
            case "Kreuzer (4)" -> 4;
            case "Zerstörer (3)" -> 3;
            case "U-Boot (2)" -> 2;
            default -> 0;
        };
    }

    private boolean canPlaceShip(String shipType) {
        int currentCount = currentPlayerShipCounts.getOrDefault(shipType, 0);
        int maxCount = SHIP_LIMITS.get(shipType);
        return currentCount < maxCount;
    }

    private void placeShip(int row, int col, int size, boolean isHorizontal, String shipType) {
        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || currentPlayerShips.containsKey(new Point(r, c))
                    || isAdjacentToShip(r, c, currentPlayerShips)) {
                JOptionPane.showMessageDialog(this, "Platzierung ungültig.");
                return;
            }
        }

        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;
            currentPlayerShips.put(new Point(r, c), new Ship(size, isHorizontal));
            gridCells[r][c].setBackground(Color.GRAY);
        }

        currentPlayerShipCounts.put(shipType, currentPlayerShipCounts.getOrDefault(shipType, 0) + 1);
    }

    private boolean isAdjacentToShip(int row, int col, Map<Point, Ship> ships) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) { 
                    continue; 
                }
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < 10 && c >= 0 && c < 10 && ships.containsKey(new Point(r, c))) {
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