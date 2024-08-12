package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BattleshipGUI extends JPanel {
    private Player player1;
    private Player player2;
    //private boolean player1Turn = true;
    private JLabel statusLabel;
    private JPanel player1BoardPanel;
    private JPanel player2BoardPanel;
    private boolean placingShips = true; // Neue Variable für die Schiffplatzierung
    private Player currentPlayer;
    private int shipSizeToPlace = 5; // Beginnen mit dem größten Schiff
    private boolean horizontalPlacement = true; // Schiffsausrichtung (horizontal/vertikal)

    public BattleshipGUI() {
        player1 = new Player();
        player2 = new Player();
        currentPlayer = player1;
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Spieler 1 platziert ein Schiff der Größe 5 (Horizontal)");
        add(statusLabel, BorderLayout.NORTH);

        JPanel boardsPanel = new JPanel(new GridLayout(1, 2));

        player1BoardPanel = createPlayerPanel("Spieler 1");
        player2BoardPanel = createPlayerPanel("Spieler 2");
        boardsPanel.add(player1BoardPanel);
        boardsPanel.add(player2BoardPanel);

        add(boardsPanel, BorderLayout.CENTER);

        JButton switchOrientationButton = new JButton("Ausrichtung wechseln");
        switchOrientationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                horizontalPlacement = !horizontalPlacement;
                statusLabel.setText("Spieler " + (currentPlayer == player1 ? "1" : "2") + 
                                    " platziert ein Schiff der Größe " + shipSizeToPlace +
                                    (horizontalPlacement ? " (Horizontal)" : " (Vertikal)"));
            }
        });
        add(switchOrientationButton, BorderLayout.SOUTH);
    }

    private JPanel createPlayerPanel(String title) {
        JPanel panel = new JPanel(new GridLayout(11, 11)); // 11x11 für Beschriftungen
        panel.setBorder(BorderFactory.createTitledBorder(title));

        // Hinzufügen der Beschriftungen (A-J, 1-10)
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) {
                    panel.add(new JLabel("")); // Obere linke Ecke bleibt leer
                } else if (i == 0) {
                    JLabel label = new JLabel(String.valueOf((char) ('A' + j - 1)), SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    panel.add(label); // Spaltenbeschriftung
                } else if (j == 0) {
                    JLabel label = new JLabel(String.valueOf(i), SwingConstants.CENTER);
                    label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    panel.add(label); // Reihenbeschriftung
                } else {
                    JPanel gridCell = new JPanel();
                    gridCell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    final int x = i - 1;
                    final int y = j - 1;
                    gridCell.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            handleGridClick(x, y, gridCell);
                        }
                    });
                    panel.add(gridCell);
                }
            }
        }
        return panel;
    }

    private void handleGridClick(int x, int y, JPanel gridCell) {
        if (placingShips) {
            if (currentPlayer.placeShip(new Ship(shipSizeToPlace), x, y, horizontalPlacement)) {
                markShipOnBoard(currentPlayer == player1 ? player1BoardPanel : player2BoardPanel, x, y, shipSizeToPlace);
                shipSizeToPlace--;
                if (shipSizeToPlace == 0) {
                    if (currentPlayer == player1) {
                        currentPlayer = player2;
                        shipSizeToPlace = 5;
                        statusLabel.setText("Spieler 2 platziert ein Schiff der Größe 5 (Horizontal)");
                    } else {
                        placingShips = false;
                        currentPlayer = player1;
                        statusLabel.setText("Spieler 1 ist am Zug");
                    }
                } else {
                    statusLabel.setText("Spieler " + (currentPlayer == player1 ? "1" : "2") + " platziert ein Schiff der Größe " + shipSizeToPlace +
                                       (horizontalPlacement ? " (Horizontal)" : " (Vertikal)"));
                }
            }
        } else {
            Player opponent = currentPlayer == player1 ? player2 : player1;
            String result = currentPlayer.attack(opponent, x, y);
            gridCell.setBackground(result.equals("Wasser") ? Color.BLUE : Color.RED);
            gridCell.setEnabled(false);
            statusLabel.setText(result);

            if (opponent.getOwnBoard().allShipsSunk()) {
                statusLabel.setText("Spieler " + (currentPlayer == player1 ? "1" : "2") + " hat gewonnen!");
                disableAllButtons();
            } else {
                currentPlayer = currentPlayer == player1 ? player2 : player1;
                statusLabel.setText("Spieler " + (currentPlayer == player1 ? "1" : "2") + " ist am Zug");
            }
        }
    }

    private void markShipOnBoard(JPanel boardPanel, int x, int y, int size) {
        Component[] components = boardPanel.getComponents();
        for (int i = 0; i < size; i++) {
            int index;
            if (horizontalPlacement) {
                index = (x + 1) * 11 + (y + 1 + i);
            } else {
                index = (x + 1 + i) * 11 + (y + 1);
            }
            JPanel cell = (JPanel) components[index];
            cell.setBackground(Color.GRAY);
        }
    }

    private void disableAllButtons() {
        disableButtonsInPanel(player1BoardPanel);
        disableButtonsInPanel(player2BoardPanel);
    }

    private void disableButtonsInPanel(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setEnabled(false);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Schiffe versenken");
        BattleshipGUI gui = new BattleshipGUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(gui);
        frame.pack();
        frame.setVisible(true);
    }
}

