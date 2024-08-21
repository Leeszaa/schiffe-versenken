package battleship;

import javax.swing.*;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.gameboard.PlayerBoardFactory;
import battleship.factorys.gameboard.TargetingBoardFactory;
import battleship.factorys.player.IPlayer;
import battleship.factorys.player.LocalPlayerFactory;
import battleship.views.MainMenuView;
import battleship.views.PlacementView;
import battleship.views.ShootingView;

import java.awt.*;

public class BattleshipGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelCont;

    private IGameBoard player1Board;
    private IGameBoard player1TargetingBoard;
    private IGameBoard player2Board;
    private IGameBoard player2TargetingBoard;
    private IPlayer player1;
    private IPlayer player2;

    public BattleshipGUI() {
        super("Battleship Game");
        cardLayout = new CardLayout();
        panelCont = new JPanel(cardLayout);

        panelCont.add(new MainMenuView(cardLayout, panelCont), "MainMenuView");

        add(panelCont);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 1000);
        setLocationRelativeTo(null);
        setVisible(true);

        // Initialize the game
        initializeLocalCoopGame();
    }

    public void showPlacementView() {
        if (player1 == null || player2 == null) {
            System.err.println("Players are not initialized. Call initializeLocalCoopGame first.");
            return;
        }

        PlacementView placementView = new PlacementView(
                cardLayout,
                panelCont,
                player1Board,
                player2Board,
                player1,
                player2);
        panelCont.add(placementView, "PlacementView");
        cardLayout.show(panelCont, "PlacementView");
    }

    public void showShootingView() {
        if (player1 == null || player2 == null) {
            System.err.println("Players are not initialized. Call initializeLocalCoopGame first.");
            return;
        }

        ShootingView shootingView = new ShootingView(player1Board, player1TargetingBoard, player2Board,
                player1TargetingBoard);
        panelCont.add(shootingView, "ShootingView");
        cardLayout.show(panelCont, "ShootingView");
    }

    public void initializeLocalCoopGame() {
        // Spieler erstellen
        player1 = new LocalPlayerFactory().createPlayer("Spieler 1");
        player2 = new LocalPlayerFactory().createPlayer("Spieler 2");

        // Spielbretter erstellen
        player1Board = new PlayerBoardFactory().createGameBoard();
        player1TargetingBoard = new TargetingBoardFactory().createGameBoard();
        player2Board = new PlayerBoardFactory().createGameBoard();
        player2TargetingBoard = new TargetingBoardFactory().createGameBoard();

        player1.setGameBoard(player1Board);
        player2.setGameBoard(player2Board);
        player1.setTargetingBoard(player1TargetingBoard);
        player2.setTargetingBoard(player2TargetingBoard);
    }

}