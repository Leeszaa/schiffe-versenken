package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BattleshipGUI extends JPanel {
    private JFrame frame;
    private JPanel gridPanel;
    private JPanel gridPanel2;
    private CardLayout cl;
    private JPanel panelCont;
    private JPanel panelFirst;
    private JPanel panelSecond;
    private JPanel panelThird;
    private JPanel[][] gridCells;
    private Map<Point, Ship> player1Ships;
    private Map<Point, Ship> player2Ships;
    private boolean isPlayer1Turn;
    private int player1ShipCount;
    private int player2ShipCount;
    private static final int TOTAL_SHIPS = 10;
    private static final int[] SHIP_SIZES = { 5, 4, 4, 3, 3, 3, 2, 2, 2, 2 };
    private static final Map<String, Integer> SHIP_LIMITS = new HashMap<>();
    private Map<String, Integer> player1ShipCounts;
    private Map<String, Integer> player2ShipCounts;

    static {
        SHIP_LIMITS.put("Schlachtschiff (5)", 1);
        SHIP_LIMITS.put("Kreuzer (4)", 2);
        SHIP_LIMITS.put("Zerstörer (3)", 3);
        SHIP_LIMITS.put("U-Boot (2)", 4);
    }

    public BattleshipGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        frame = new JFrame("Battleship Game");
        panelCont = new JPanel();
        panelFirst = new JPanel();
        panelSecond = new JPanel();
        panelThird = new JPanel();

        cl = new CardLayout();

        panelCont.setLayout(cl);

        frame.setSize(1400, 1000);

        createEntrancePanel();
        createPlacementPanel();
        createShootingPanel();

        panelCont.add(panelFirst, "1");
        panelCont.add(panelSecond, "2");
        panelCont.add(panelThird, "3");
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

    private void createEntrancePanel() {
        panelFirst.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Schiffeversenken 0.0.1");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 64));

        JButton buttonOne = new JButton("Lokaler Coop");
        JButton buttonTwo = new JButton("Online Multiplayer (WIP)");
        JButton buttonThree = new JButton("K.I. Gegner (WIP)");
        JButton buttonFour = new JButton("Debug Mode");
        JButton buttonFive = new JButton("Platzierungsphase");
        JButton buttonSix = new JButton("Schießphase");

        buttonOne.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cl.show(panelCont, "2");
                showConfirmationDialog();
            }
        });

        buttonFour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                buttonFive.setVisible(true);
                buttonSix.setVisible(true);
            }
        });

        buttonFive.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cl.show(panelCont, "2");
            }
        });

        buttonSix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cl.show(panelCont, "3");
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Span across all columns
        panelFirst.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset to default
        panelFirst.add(buttonOne, gbc);

        gbc.gridy = 2;
        panelFirst.add(buttonTwo, gbc);

        gbc.gridy = 3;
        panelFirst.add(buttonThree, gbc);

        gbc.gridy = 5;
        panelFirst.add(buttonFour, gbc);

        gbc.gridy = 6;
        panelFirst.add(buttonFive, gbc);
        buttonFive.setVisible(false);

        gbc.gridy = 7;
        panelFirst.add(buttonSix, gbc);
        buttonSix.setVisible(false);

        String gifPath = "src/battleship/assets/waves.gif";
        File gifFile = new File(gifPath);
        String absoluteGifPath = gifFile.getAbsolutePath();

        ImageIcon gifIcon = new ImageIcon(absoluteGifPath);
        if (gifIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            System.out.println("GIF loaded successfully.");
        } else {
            System.out.println("Failed to load GIF.");
        }

        gbc.gridy = 8;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        JLabel gifLabel = new JLabel(gifIcon);
        panelFirst.add(gifLabel, gbc);

        panelFirst.setBackground(Color.darkGray);
    }

    private void showConfirmationDialog() {
        int response = JOptionPane.showConfirmDialog(frame, "Spieler 1: Platziere deine Schiffe", "Schiffsplatzierung",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.OK_OPTION) {
            System.out.println("User confirmed ship placement.");
        } else {
            cl.show(panelCont, "1");
            System.out.println("User cancelled ship placement.");
        }
    }

    private void createPlacementPanel() {

        JButton buttonSecond = new JButton("Zurück");
        buttonSecond.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cl.show(panelCont, "1");
            }
        });
        panelSecond.add(buttonSecond, BorderLayout.NORTH);
        panelSecond.setBackground(Color.darkGray);
    
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
        placementWithLabels.add(createRowLabels(gridPanel), BorderLayout.WEST);
        placementWithLabels.add(gridPanel, BorderLayout.CENTER);
        placementWithLabels.setBackground(Color.darkGray);
    
        JLabel titleLabel = new JLabel("Schiffe platzieren. Klicke dafür auf eine Zelle und wähle aus welches Schiff du platzieren möchtest", SwingConstants.CENTER);
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
    
        panelSecond.setLayout(new BorderLayout());
        panelSecond.add(mainPanel, BorderLayout.CENTER);
    
        panelSecond.revalidate();
        panelSecond.repaint();
    }

    private void createShootingPanel() {
        gridPanel = new JPanel(new GridLayout(10, 10));
        gridPanel2 = new JPanel(new GridLayout(10, 10));

        gridPanel.setPreferredSize(new Dimension(600, 600));
        gridPanel2.setPreferredSize(new Dimension(600, 600));

        gridCells = new JPanel[10][10];
        JPanel[][] gridCells2 = new JPanel[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JPanel cell = new JPanel();
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                // cell.addMouseListener(new GridCellClickListener(i, j));
                gridCells[i][j] = cell;
                gridPanel.add(cell);

                JPanel cell2 = new JPanel();
                cell2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                // cell2.addMouseListener(new GridCellClickListener(i, j));
                gridCells2[i][j] = cell2;
                gridPanel2.add(cell2);
            }
        }

        JPanel gridWithLabels1 = new JPanel(new BorderLayout());
        gridWithLabels1.add(createColumnLabels(), BorderLayout.NORTH);
        gridWithLabels1.add(createRowLabels(gridPanel), BorderLayout.WEST);
        gridWithLabels1.add(gridPanel, BorderLayout.CENTER);

        JPanel gridWithLabels2 = new JPanel(new BorderLayout());
        gridWithLabels2.add(createColumnLabels(), BorderLayout.NORTH);
        gridWithLabels2.add(createRowLabels(gridPanel2), BorderLayout.WEST);
        gridWithLabels2.add(gridPanel2, BorderLayout.CENTER);

        panelThird.setLayout(new BoxLayout(panelThird, BoxLayout.X_AXIS));

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        wrapperPanel.add(gridWithLabels1, gbc);

        gbc.gridx = 1;
        wrapperPanel.add(gridWithLabels2, gbc);

        panelThird.add(wrapperPanel);

        panelThird.revalidate();
        panelThird.repaint();
    }

    private JPanel createColumnLabels() {
        JPanel panel = new JPanel(new GridLayout(1, 10));
        for (char c = 'A'; c <= 'J'; c++) {
            JLabel label = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            label.setFont(new Font("Roboto", Font.BOLD, 20));
            panel.add(label);
        }
        return panel;
    }

    private JPanel createRowLabels(JPanel gridPanel) {
        JPanel panel = new JPanel(new GridLayout(10, 1));
        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            label.setFont(new Font("Roboto", Font.BOLD, 20));
            panel.add(label);
        }
        return panel;
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
            if ((isPlayer1Turn && player1ShipCount >= TOTAL_SHIPS)
                    || (!isPlayer1Turn && player2ShipCount >= TOTAL_SHIPS)) {
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

            int result = JOptionPane.showConfirmDialog(frame, panel, "Schiff platzieren", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

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

            if (r >= 10 || c >= 10 || currentPlayerShips.containsKey(new Point(r, c))
                    || isAdjacentToShip(r, c, currentPlayerShips)) {
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
        int[] dx = { -1, -1, -1, 0, 0, 1, 1, 1 };
        int[] dy = { -1, 0, 1, -1, 1, -1, 0, 1 };

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
}