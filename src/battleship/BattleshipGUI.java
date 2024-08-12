package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BattleshipGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelCont;
    private Map<Point, Ship> player1Ships; 
    private Map<Point, Ship> player2Ships;
    private Map<Point, Ship> player1Shoots = new HashMap<>();
    private Map<Point, Ship> player2Shoots = new HashMap<>();


    public BattleshipGUI() {
        super("Battleship Game");
        cardLayout = new CardLayout();
        panelCont = new JPanel(cardLayout);

        player1Ships = new HashMap<>();
        player2Ships = new HashMap<>();

        // Views erstellen und hinzufügen
        panelCont.add(new MainMenuView(cardLayout, panelCont), "MainMenuView");
        panelCont.add(new PlacementView(cardLayout, panelCont, player1Ships, player2Ships), "PlacementView"); 
        panelCont.add(new ShootingView(player1Ships, player2Ships, player1Shoots, player2Shoots), "ShootingView"); 

        add(panelCont);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 1000);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}