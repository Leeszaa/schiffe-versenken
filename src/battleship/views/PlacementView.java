/**
 * @file PlacementView.java
 *   Represents the ship placement view in the Battleship game.
 */

 package battleship.views;

 import battleship.BattleshipGUI;
 import battleship.factorys.gameboard.IGameBoard;
 import battleship.factorys.player.IPlayer;
 import battleship.managers.ShipPlacementManager;
 import battleship.factorys.ships.IShip;
 
 import javax.swing.*;
 import java.awt.*;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.util.Map;
 
 /**
  * @class PlacementView
  *   Represents the ship placement view in the Battleship game.
  * Extends {@link JPanel} to create a custom panel for ship placement.
  */
 public class PlacementView extends JPanel {
     private JPanel gridPanel; /**< Panel for the grid layout */
     private JPanel[][] gridCells; /**< 2D array of grid cells */
     private CardLayout cardLayout; /**< Card layout for switching views */
     private JPanel parentPanel; /**< Parent panel containing this view */
     private ShipPlacementManager shipPlacementManager; /**< Manager for ship placement */
     private BattleshipGUI battleshipGUI; /**< Reference to the main GUI */
     private JLabel[] shipCountLabels; /**< Labels for displaying ship counts */
     private IPlayer player1; /**< The first player */
     private IPlayer player2; /**< The second player */
 
     private static final int TOTAL_SHIPS = 10; /**< Total number of ships to be placed */
 
     /**
      *   Constructor for PlacementView.
      * @param cardLayout The card layout for switching views.
      * @param parentPanel The parent panel containing this view.
      * @param player1Board The game board for player 1.
      * @param player2Board The game board for player 2.
      * @param player1 The first player.
      * @param player2 The second player.
      * @param battleshipGUI The main GUI.
      */
     public PlacementView(CardLayout cardLayout, JPanel parentPanel, IGameBoard player1Board, IGameBoard player2Board,
                          IPlayer player1, IPlayer player2, BattleshipGUI battleshipGUI) {
         this.cardLayout = cardLayout;
         this.parentPanel = parentPanel;
         this.battleshipGUI = battleshipGUI;
         this.shipPlacementManager = new ShipPlacementManager(player1, player2, this, battleshipGUI);
            this.player1 = player1;
            this.player2 = player2;
 
         initComponents();
     }
 
     /**
      *   Initializes the components of the view.
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
    
        // Add the ship list panel to the right of the grid panel
        JPanel shipListPanel = createShipListPanel();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(shipListPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(rulesButton, gbc);
    
        add(mainPanel, BorderLayout.CENTER);
    }

       private void showRulesDialog() {
        JDialog dialog = new JDialog(battleshipGUI, "Regeln", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(battleshipGUI);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().setBackground(Color.darkGray);
    
        // Create a panel to hold the labels
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.darkGray);
    
        // Create and add labels with the desired font and color
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
    
        dialog.getContentPane().add(contentPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
 
     /**
      *   Creates the column labels for the grid.
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
      *   Creates the row labels for the grid.
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

         private JPanel createShipListPanel() {
        JPanel shipListPanel = new JPanel();
        shipListPanel.setLayout(new BoxLayout(shipListPanel, BoxLayout.Y_AXIS));
        shipListPanel.setBackground(Color.darkGray);
    
        String[] shipNames = { "Schlachtschiff", "Kreuzer", "Zerstörer", "U-Boot" };
        int[] shipCounts = { 1, 2, 3, 4 }; // Example counts for each ship type
        shipCountLabels = new JLabel[shipNames.length];
    
        JLabel titleLabel = new JLabel("Noch zu verteilende Schiffe:");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        shipListPanel.add(titleLabel);
    
        shipListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    
        for (int i = 0; i < shipNames.length; i++) {
            JPanel shipPanel = new JPanel(new BorderLayout());
            shipPanel.setBackground(Color.darkGray);
    
            JLabel nameLabel = new JLabel(shipNames[i] + ": ");
            nameLabel.setFont(new Font("Roboto", Font.BOLD, 16));
            nameLabel.setForeground(Color.WHITE);
    
            shipCountLabels[i] = new JLabel("x" + shipCounts[i]);
            shipCountLabels[i].setFont(new Font("Roboto", Font.BOLD, 16));
            shipCountLabels[i].setForeground(Color.WHITE);
    
            shipPanel.add(nameLabel, BorderLayout.WEST);
            shipPanel.add(shipCountLabels[i], BorderLayout.EAST);
    
            shipListPanel.add(shipPanel);
            shipListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    
        return shipListPanel;
    }

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
      *   Listener for grid cell clicks.
      * Extends {@link MouseAdapter} to handle mouse click events on grid cells.
      */
     private class GridCellClickListener extends MouseAdapter {
         private final int row; /**< The row of the grid cell */
         private final int col; /**< The column of the grid cell */
 
         /**
          *   Constructor for GridCellClickListener.
          * @param row The row of the grid cell.
          * @param col The column of the grid cell.
          */
         public GridCellClickListener(int row, int col) {
             this.row = row;
             this.col = col;
         }
 
         /**
          *   Handles mouse click events on grid cells.
          * @param e The mouse event.
          */
         @Override
         public void mouseClicked(MouseEvent e) {
             IShip existingShip = shipPlacementManager.getShipAt(row, col);
             if (existingShip != null) {
                int confirm = JOptionPane.showConfirmDialog(PlacementView.this,
                        "An dieser Stelle befindet sich bereits ein Schiff. Möchten Sie es löschen?",
                        "Schiff löschen", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    shipPlacementManager.removeShip(row, col, existingShip, gridCells);
                    gridCells[row][col].setBackground(null);
                    updateShipCount(existingShip.getShipName(), 1); // Increment the counter
                }
                return;
            }
 
             if (shipPlacementManager.getPlacedShipsCount() >= TOTAL_SHIPS) {
                 if (shipPlacementManager.getCurrentPlayer().equals("player1")) {
                     clearGrid();
                     JOptionPane.showMessageDialog(PlacementView.this,
                             "Spieler 1 hat alle Schiffe platziert. Nun ist Spieler 2 dran.");
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
                         updateShipCount(selectedShip, -1);
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
 
     /**
      *   Shows the ship locations for both players.
      */
     private void showShipLocations() {
         Map<Point, IShip> player1Ships = shipPlacementManager.getPlayer1ShipLocations();
         Map<Point, IShip> player2Ships = shipPlacementManager.getPlayer2ShipLocations();
 
         System.out.println("Player 1 Ships: " + player1Ships);
         System.out.println("Player 2 Ships: " + player2Ships);
     }
 
     /**
      *   Clears the grid by resetting the background color of all cells.
      */
     public void clearGrid() {
         for (int i = 0; i < 10; i++) {
             for (int j = 0; j < 10; j++) {
                 gridCells[i][j].setBackground(null);
             }
         }
     }
 }