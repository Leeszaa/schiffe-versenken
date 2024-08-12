package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

public class ShootingView extends JPanel {
    private JPanel gridPanel1; // Spielfeld von Spieler 1
    private JPanel gridPanel2; // Spielfeld von Spieler 2
    private JPanel[][] gridCells1;
    private JPanel[][] gridCells2;
    private Map<Point, Ship> player1Ships;
    private Map<Point, Ship> player2Ships;
    private Map<Point, Ship> player1Shots;
    private Map<Point, Ship> player2Shots;
    private boolean isPlayer1Turn = true; 

    public ShootingView(Map<Point, Ship> player1Ships, Map<Point, Ship> player2Ships, Map<Point, Ship> player1Shots, Map<Point, Ship> player2Shots) {
        this.player1Ships = player1Ships; 
        this.player2Ships = player2Ships;
        this.player2Shots = player2Shots;
        this.player1Shots = player1Shots;

        initComponents();
    }

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

    private class GridClickListener extends MouseAdapter {
        private final int row;
        private final int col;
        private final int playerGrid;

        public GridClickListener(int row, int col, int playerGrid) {
            this.row = row;
            this.col = col;
            this.playerGrid = playerGrid;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Handle click event
        }
    }

}