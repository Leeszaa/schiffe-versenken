/**
 * @file MainMenuView.java
 * @brief Represents the main menu view in the Battleship game.
 */

package battleship.views;

import javax.swing.*;

import battleship.BattleshipGUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * @class MainMenuView
 * @brief Represents the main menu view in the Battleship game.
 * Extends {@link JPanel} to create a custom panel for the main menu.
 */
public class MainMenuView extends JPanel {
    private CardLayout cardLayout; /**< The card layout for switching views */
    private JPanel parentPanel; /**< The parent panel containing this view */

    /**
     * @brief Constructor for MainMenuView.
     * @param cardLayout The card layout for switching views.
     * @param parentPanel The parent panel containing this view.
     */
    public MainMenuView(CardLayout cardLayout, JPanel parentPanel) {
        this.cardLayout = cardLayout;
        this.parentPanel = parentPanel;
        initComponents();
    }

    /**
     * @brief Initializes the components of the view.
     */
    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Schiffeversenken 0.0.1");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 64));

        JButton localCoopButton = new JButton("Lokaler Coop");
        localCoopButton.addActionListener(e -> {
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showPlacementView();
            ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).initializeLocalCoopGame();
        });

        JButton buttonTwo = new JButton("Online Multiplayer (WIP)");
        JButton buttonThree = new JButton("K.I. Gegner (WIP)");
        JButton buttonFour = new JButton("Debug Mode");
        JButton buttonFive = new JButton("Platzierungsphase");
        JButton buttonSix = new JButton("Schießphase");

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
                cardLayout.show(parentPanel, "PlacementView");
            }
        });

        buttonSix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ((BattleshipGUI) SwingUtilities.getWindowAncestor(parentPanel)).showShootingView();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1; 
        add(localCoopButton, gbc);

        gbc.gridy = 2;
        add(buttonTwo, gbc);

        gbc.gridy = 3;
        add(buttonThree, gbc);

        gbc.gridy = 5;
        add(buttonFour, gbc);

        gbc.gridy = 6;
        add(buttonFive, gbc);
        buttonFive.setVisible(false);

        gbc.gridy = 7;
        add(buttonSix, gbc);
        buttonSix.setVisible(false);

        String gifPath = "src/battleship/assets/waves.gif";
        File gifFile = new File(gifPath);
        String absoluteGifPath = gifFile.getAbsolutePath();
        ImageIcon gifIcon = new ImageIcon(absoluteGifPath);
        if (gifIcon.getImageLoadStatus() != MediaTracker.COMPLETE) {
            System.out.println("Failed to load GIF.");
        }

        gbc.gridy = 8;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(new JLabel(gifIcon), gbc);

        setBackground(Color.darkGray);
    }
}