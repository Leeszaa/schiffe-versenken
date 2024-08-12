package battleship;

import javax.swing.JFrame;

public class BattleshipGame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Schiffe versenken");
        BattleshipGUI gui = new BattleshipGUI();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(gui);
        frame.pack();
        frame.setVisible(true);
    }
}

