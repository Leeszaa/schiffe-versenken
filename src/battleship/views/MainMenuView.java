package battleship.views;

import javax.swing.*;
import battleship.BattleshipGUI;
import java.awt.*;
import java.awt.event.*;

/**
 * Represents the main menu view in the Battleship game.
 * Extends {@link JPanel} for a custom main menu panel.
 */
public class MainMenuView extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel parentPanel;
    private JButton buttonFive;
    private JButton buttonSix;
    private JButton buttonSeven;
    private JButton buttonEight;
    private JButton buttonNine;

    /**
     * Constructor for MainMenuView.
     * @param cardLayout The card layout for switching views.
     * @param parentPanel The parent panel containing this view.
     */
    public MainMenuView(CardLayout cardLayout, JPanel parentPanel) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        initComponents();
    }

    /**
     * Initializes the components of the view.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        addTitle(gbc);
        addButtons(gbc);

        gbc.gridy = 11;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(new JLabel(new ImageIcon(getClass().getResource("waves.gif"))), gbc);

        setBackground(Color.darkGray);
    }

    private void addTitle(GridBagConstraints gbc) {
        JLabel titleLabel = new JLabel("Schiffeversenken 0.0.1");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 64));
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(titleLabel, gbc);
    }

    private void addButtons(GridBagConstraints gbc) {
        gbc.gridy = 1;
        gbc.gridwidth = 1; 
        add(createButton("Lokaler Coop", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeLocalCoopGame();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showPlacementView();
        }), gbc);

        gbc.gridy = 2;
        add(createButton("K.I. Gegner (WIP)", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeComputerOpponentGame();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showSinglePlacementView();
        }), gbc);

        gbc.gridy = 4;
        JButton debugButton = createButton("Debug Mode", () -> {
            toggleDebugButtons();
        });
        add(debugButton, gbc);

        buttonFive = addDebugButton(gbc, 5, "Platzierungsphase Debug",  () -> ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showPlacementView());
        buttonSix = addDebugButton(gbc, 6, "Schießphase Debug", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeDebugGame();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showShootingView(false);
        });
        buttonSeven = addDebugButton(gbc, 7, "Schießphase (kein Player Switch) Debug", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeDebugGame();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showShootingView(true);
        });
        buttonEight = addDebugButton(gbc, 8, "Computer Gameboard Generator", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeComputerOpponentGame();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showComputerDebugView();
        });
        buttonNine = addDebugButton(gbc, 9, "Computer Shooting View Debug", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeComputerOpponentGameDebug();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showComputerShootingViewDebug();
        });
    }

    private void toggleDebugButtons(){
        buttonFive.setVisible(!buttonFive.isVisible());
        buttonSix.setVisible(!buttonSix.isVisible());
        buttonSeven.setVisible(!buttonSeven.isVisible());
        buttonEight.setVisible(!buttonEight.isVisible());
        buttonNine.setVisible(!buttonNine.isVisible());
      }
    
        private JButton addDebugButton(GridBagConstraints gbc, int gridy, String label, Runnable action) {
            JButton button = createButton(label, action);
            button.setVisible(false);
            gbc.gridy = gridy;
            add(button, gbc);
            return button; // Return the button so it can be stored
        }

    private JButton createButton(String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> action.run());
        return button;
    }
}