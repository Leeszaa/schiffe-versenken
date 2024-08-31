package battleship.views;

import javax.swing.*;
import battleship.BattleshipGUI;
import java.awt.*;

/**
 * @class MainMenuView
 *        Represents the main menu view of the Battleship game.
 *        Extends {@link JPanel} for a custom main menu panel.
 */
public class MainMenuView extends JPanel {
    /**
     * SerialVersionUID for the MainMenuView class.
     */
    private static final long serialVersionUID = -3508847850101162469L;
    private final JPanel parentPanel;
    private JButton buttonFive;
    private JButton buttonSix;
    private JButton buttonSeven;
    private JButton buttonEight;
    private JButton buttonNine;
    private JButton buttonTen;

    /**
     * Constructor for MainMenuView.
     * 
     * @param parentPanel The parent panel containing this view.
     */
    public MainMenuView(JPanel parentPanel) {
        this.parentPanel = parentPanel;
        initComponents();
    }

    /**
     * Initializes the components of the view, including title, buttons, and
     * background.
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

    /**
     * Adds the game title label to the main menu.
     * 
     * @param gbc The GridBagConstraints for positioning the title label.
     */
    private void addTitle(GridBagConstraints gbc) {
        JLabel titleLabel = new JLabel("Schiffeversenken 0.0.1");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 64));
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(titleLabel, gbc);
    }

    /**
     * Adds the main menu buttons to the panel, including game mode selections and
     * debug options.
     * 
     * @param gbc The GridBagConstraints for positioning the buttons.
     */
    private void addButtons(GridBagConstraints gbc) {
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(createButton("Lokaler Coop", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeLocalCoopGame();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showPlacementView();
        }), gbc);

        gbc.gridy = 2;
        add(createButton("Computer Gegner", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeComputerOpponentGame();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showSinglePlacementView();
        }), gbc);

        gbc.gridy = 4;
        JButton debugButton = createButton("Debug Mode", () -> {
            toggleDebugButtons();
        });
        add(debugButton, gbc);

        // Add debug buttons, initially hidden
        buttonFive = addDebugButton(gbc, 5, "Platzierungsphase Debug", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeLocalCoopGame();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showPlacementView();
        });
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
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showComputerShootingViewDebug(false);
        });
        buttonTen = addDebugButton(gbc, 10, "Computer Shooting View (nur Computer spielt) Debug", () -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeComputerOpponentGameDebug();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showComputerShootingViewDebug(true);
        });
    }

    /**
     * Toggles the visibility of the debug buttons.
     */
    private void toggleDebugButtons() {
        buttonFive.setVisible(!buttonFive.isVisible());
        buttonSix.setVisible(!buttonSix.isVisible());
        buttonSeven.setVisible(!buttonSeven.isVisible());
        buttonEight.setVisible(!buttonEight.isVisible());
        buttonNine.setVisible(!buttonNine.isVisible());
        buttonTen.setVisible(!buttonTen.isVisible());
    }

    /**
     * Creates and adds a debug button to the panel. The button is initially hidden.
     * 
     * @param gbc    The GridBagConstraints for positioning the button.
     * @param gridy  The grid y-coordinate for the button.
     * @param label  The label text for the button.
     * @param action The action to perform when the button is clicked.
     * @return The created JButton.
     */
    private JButton addDebugButton(GridBagConstraints gbc, int gridy, String label, Runnable action) {
        JButton button = createButton(label, action);
        button.setVisible(false);
        gbc.gridy = gridy;
        add(button, gbc);
        return button;
    }

    /**
     * Creates a JButton with the specified label and action.
     * 
     * @param label  The text label for the button.
     * @param action The Runnable action to perform when the button is clicked.
     * @return The created JButton.
     */
    private JButton createButton(String label, Runnable action) {
        JButton button = new JButton(label);
        button.addActionListener(e -> action.run());
        return button;
    }
}