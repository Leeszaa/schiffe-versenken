import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class BattleshipGame {
    private JFrame frame;
    private JPanel gridPanel;
    private CardLayout cl;
    private JPanel panelCont;
    private JPanel panelFirst;
    private JPanel panelSecond;
    private JPanel[][] gridCells;
    private Map<Point, Ship> player1Ships;
    private Map<Point, Ship> player2Ships;
    private boolean isPlayer1Turn;
    private int player1ShipCount;
    private int player2ShipCount;
    private static final int TOTAL_SHIPS = 10;
    private static final int[] SHIP_SIZES = {5, 4, 4, 3, 3, 3, 2, 2, 2, 2};
    private static final Map<String, Integer> SHIP_LIMITS = new HashMap<>();
    private Map<String, Integer> player1ShipCounts;
    private Map<String, Integer> player2ShipCounts;

    static {
        SHIP_LIMITS.put("Schlachtschiff (5)", 1);
        SHIP_LIMITS.put("Kreuzer (4)", 2);
        SHIP_LIMITS.put("Zerstörer (3)", 3);
        SHIP_LIMITS.put("U-Boot (2)", 4);
    }

    public BattleshipGame() {
        frame = new JFrame("Battleship Game");
        panelCont = new JPanel();
        panelFirst = new JPanel();
        panelSecond = new JPanel();
        cl = new CardLayout();

        panelCont.setLayout(cl);

        frame.setSize(800, 800);

        createFirstPanel();
        createSecondPanel();

        panelCont.add(panelFirst, "1");
        panelCont.add(panelSecond, "2");
        cl.show(panelCont, "1");

        frame.add(panelCont);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        player1Ships = new HashMap<>();
        player2Ships = new HashMap<>();
        isPlayer1Turn = true;
        player1ShipCount = 0;
        player2ShipCount = 0;
        player1ShipCounts = new HashMap<>();
        player2ShipCounts = new HashMap<>();
    }

    private void createFirstPanel() {
        JButton buttonOne = new JButton("Lokaler Coop");
        buttonOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cl.show(panelCont, "2");
            }
        });
        panelFirst.add(buttonOne);
        panelFirst.setBackground(Color.darkGray);
    }

    private void createSecondPanel() {
        JButton buttonSecond = new JButton("Zurück");
        buttonSecond.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cl.show(panelCont, "1");
            }
        });
        panelSecond.add(buttonSecond);
        panelSecond.setBackground(Color.GREEN);

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
        panelSecond.add(gridPanel);
    }

    private class GridCellClickListener extends MouseAdapter {
        private int row;
        private int col;

        public GridCellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if ((isPlayer1Turn && player1ShipCount >= TOTAL_SHIPS) || (!isPlayer1Turn && player2ShipCount >= TOTAL_SHIPS)) {
                return;
            }

            JPanel panel = new JPanel(new GridLayout(2, 2));
            JLabel sizeLabel = new JLabel("Wähle ein Schiff aus:");
            String[] shipOptions = {"Schlachtschiff (5)", "Kreuzer (4)", "Zerstörer (3)", "U-Boot (2)"};
            JComboBox<String> sizeComboBox = new JComboBox<>(shipOptions);
            JLabel orientationLabel = new JLabel("Ausrichtung auswählen:");
            String[] orientationOptions = {"Horizontal", "Vertikal"};
            JComboBox<String> orientationComboBox = new JComboBox<>(orientationOptions);

            panel.add(sizeLabel);
            panel.add(sizeComboBox);
            panel.add(orientationLabel);
            panel.add(orientationComboBox);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Schiff platzieren", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String selectedShip = (String) sizeComboBox.getSelectedItem();
                int shipSize = getShipSize(selectedShip);
                boolean isHorizontal = orientationComboBox.getSelectedItem().equals("Horizontal");

                if (canPlaceShip(selectedShip)) {
                    placeShip(row, col, shipSize, isHorizontal, selectedShip);
                } else {
                    JOptionPane.showMessageDialog(frame, "Du hast bereits das Maximum dieses Types plaziert.");
                }
            }
        }
    }

    private int getShipSize(String shipType) {
        switch (shipType) {
            case "Schlachtschiff (5)":
                return 5;
            case "Kreuzer (4)":
                return 4;
            case "Zerstörer (3)":
                return 3;
            case "U-Boot (2)":
                return 2;
            default:
                return 0;
        }
    }

    private boolean canPlaceShip(String shipType) {
        Map<String, Integer> currentPlayerShipCounts = isPlayer1Turn ? player1ShipCounts : player2ShipCounts;
        int currentCount = currentPlayerShipCounts.getOrDefault(shipType, 0);
        int maxCount = SHIP_LIMITS.get(shipType);
        return currentCount < maxCount;
    }

    private void placeShip(int row, int col, int size, boolean isHorizontal, String shipType) {
        Map<Point, Ship> currentPlayerShips = isPlayer1Turn ? player1Ships : player2Ships;
        Map<String, Integer> currentPlayerShipCounts = isPlayer1Turn ? player1ShipCounts : player2ShipCounts;

        for (int i = 0; i < size; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || currentPlayerShips.containsKey(new Point(r, c)) || isAdjacentToShip(r, c, currentPlayerShips)) {
                JOptionPane.showMessageDialog(frame, "Plazierung nicht möglich. Versuche es erneut.");
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

        if (isPlayer1Turn) {
            player1ShipCount++;
            if (player1ShipCount >= TOTAL_SHIPS) {
                isPlayer1Turn = false;
                clearGrid();
                JOptionPane.showMessageDialog(frame, "Spieler 1 hat alle Schiffe plaziert. Nun ist Spieler 2 drann.");
            }
        } else {
            player2ShipCount++;
            if (player2ShipCount >= TOTAL_SHIPS) {
                JOptionPane.showMessageDialog(frame, "Spieler 1 hat alle Schiffe plaziert. Spiel Setup vollständig.");
            }
        }
    }

    private boolean isAdjacentToShip(int row, int col, Map<Point, Ship> ships) {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int r = row + dx[i];
            int c = col + dy[i];
            if (r >= 0 && r < 10 && c >= 0 && c < 10 && ships.containsKey(new Point(r, c))) {
                return true;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BattleshipGame();
            }
        });
    }
}

class Ship {
    private int size;
    private boolean isHorizontal;

    public Ship(int size, boolean isHorizontal) {
        this.size = size;
        this.isHorizontal = isHorizontal;
    }

    public int getSize() {
        return size;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }
}