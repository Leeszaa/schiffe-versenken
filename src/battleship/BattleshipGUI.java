/**
 * @file BattleshipGUI.java
 *   Main GUI class for the Battleship game.
 */

package battleship;

import javax.swing.*;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.gameboard.PlayerBoardFactory;
import battleship.factorys.gameboard.TargetingBoardFactory;
import battleship.factorys.player.ComputerPlayerFactory;
import battleship.factorys.player.IPlayer;
import battleship.factorys.player.LocalPlayerFactory;
import battleship.factorys.ships.*;
import battleship.managers.BattleshipAI;
import battleship.views.MainMenuView;
import battleship.views.PlacementView;
import battleship.views.ShootingView;
import battleship.views.SinglePlacementView;
import battleship.views.ComputerDebugView;
import battleship.views.ComputerShootingView;

import java.awt.*;

/**
 * @class BattleshipGUI
 *        Main GUI class for the Battleship game.
 *        Extends {@link JFrame} to create the main window for the game.
 */
public class BattleshipGUI extends JFrame {

    private final CardLayout cardLayout;
    /** < The card layout for switching views */
    private final JPanel panelCont;
    /** < The main container panel */

    private IGameBoard player1Board;
    /** < Game board for player 1 */
    private IGameBoard player1TargetingBoard;
    /** < Targeting board for player 1 */
    private IGameBoard player2Board;
    /** < Game board for player 2 */
    private IGameBoard player2TargetingBoard;
    /** < Targeting board for player 2 */
    private IPlayer player1;
    /** < The first player */
    private IPlayer player2;

    private IPlayer computer;

    /** < The second player */

    /**
     * Constructor for BattleshipGUI.
     * Initializes the main window and starts the local coop game.
     */
    public BattleshipGUI() {
        super("Battleship Game");
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
        cardLayout = new CardLayout();
        panelCont = new JPanel(cardLayout);

        panelCont.add(new MainMenuView(cardLayout, panelCont), "MainMenuView");

        add(panelCont);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1400, 1000);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Shows the ship placement view.
     * Ensures players are initialized before showing the placement view.
     */
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
                player2,
                this);
        panelCont.add(placementView, "PlacementView");
        cardLayout.show(panelCont, "PlacementView");
    }

    /**
     * Shows the shooting view.
     * Ensures players are initialized before showing the shooting view.
     */
    public void showShootingView(boolean isOnePlayerDebug) {
        if (player1 == null || player2 == null) {
            System.err.println("Players are not initialized. Call initializeLocalCoopGame first.");
            return;
        }

        ShootingView shootingView = new ShootingView(player1, player2, isOnePlayerDebug, this);
        panelCont.add(shootingView, "ShootingView");
        cardLayout.show(panelCont, "ShootingView");
    }

    public void showMainMenuView() {
        cardLayout.show(panelCont, "MainMenuView");
    }

    public void showSinglePlacementView() {
        if (player1 == null || computer == null) {
            System.err.println("Players are not initialized. Call initializeComputerOpponentGame first.");
            return;
        }

        SinglePlacementView singlePlacementView = new SinglePlacementView(
                cardLayout,
                panelCont,
                player1,
                computer,
                this);
        panelCont.add(singlePlacementView, "SinglePlacementView");
        cardLayout.show(panelCont, "SinglePlacementView");
    }

    public void showComputerDebugView() {
        if (player1 == null || computer == null) {
            System.err.println("Players are not initialized. Call initializeComputerOpponentGame first.");
            return;
        }

        ComputerDebugView computerDebugView = new ComputerDebugView(
                cardLayout,
                panelCont,
                player1Board,
                player2Board,
                player1,
                computer,
                this);
        panelCont.add(computerDebugView, "ComputerDebugView");
        cardLayout.show(panelCont, "ComputerDebugView");
    }

    public void showComputerShootingView() {
        if (player1 == null || computer == null) {
            System.err.println("Players are not initialized. Call initializeComputerOpponentGame first.");
            return;
        }

        ComputerShootingView computerShootingView = new ComputerShootingView(player1, computer, false, this);
        panelCont.add(computerShootingView, "ComputerShootingView");
        cardLayout.show(panelCont, "ComputerShootingView");
    }

    public void showComputerShootingViewDebug() {
        if (player1 == null || computer == null) {
            System.err.println("Players are not initialized. Call initializeComputerOpponentGame first.");
            return;
        }

        ComputerShootingView computerDebugShootingView = new ComputerShootingView(player1, computer, false, this);
        panelCont.add(computerDebugShootingView, "ComputerDebugShootingView");
        cardLayout.show(panelCont, "ComputerDebugShootingView");
    }

    /**
     * Initializes the local coop game.
     * Creates players and their respective game boards and targeting boards.
     */
    public void initializeLocalCoopGame() {
        player1 = new LocalPlayerFactory().createPlayer("Spieler 1");
        player2 = new LocalPlayerFactory().createPlayer("Spieler 2");

        player1Board = new PlayerBoardFactory().createGameBoard();
        player1TargetingBoard = new TargetingBoardFactory().createGameBoard();
        player2Board = new PlayerBoardFactory().createGameBoard();
        player2TargetingBoard = new TargetingBoardFactory().createGameBoard();

        player1.setGameBoard(player1Board);
        player2.setGameBoard(player2Board);
        player1.setTargetingBoard(player1TargetingBoard);
        player2.setTargetingBoard(player2TargetingBoard);
    }

    public void initializeDebugGame() {
        player1 = new LocalPlayerFactory().createPlayer("Spieler 1");
        player2 = new LocalPlayerFactory().createPlayer("Spieler 2");

        player1Board = new PlayerBoardFactory().createGameBoard();
        player1TargetingBoard = new TargetingBoardFactory().createGameBoard();
        player2Board = new PlayerBoardFactory().createGameBoard();
        player2TargetingBoard = new TargetingBoardFactory().createGameBoard();

        player1.setGameBoard(player1Board);
        player2.setGameBoard(player2Board);
        player1.setTargetingBoard(player1TargetingBoard);
        player2.setTargetingBoard(player2TargetingBoard);

        SchlachtschiffFactory schlachtschiffFactory = new SchlachtschiffFactory();
        ZerstörerFactory zerstörerFactory = new ZerstörerFactory();

        KreuzerFactory kreuzerFactory = new KreuzerFactory();
        U_BootFactory u_BootFactory = new U_BootFactory();

        player1.getGameBoard().placeShip(0, 0, schlachtschiffFactory.createShip(), true);
        player1.getGameBoard().placeShip(0, 2, zerstörerFactory.createShip(), true);
        player1.getGameBoard().placeShip(0, 4, zerstörerFactory.createShip(), true);
        player1.getGameBoard().placeShip(0, 6, kreuzerFactory.createShip(), true);
        player1.getGameBoard().placeShip(0, 8, kreuzerFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 0, kreuzerFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 2, u_BootFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 4, u_BootFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 6, u_BootFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 8, u_BootFactory.createShip(), true);

        player2.getGameBoard().placeShip(0, 0, schlachtschiffFactory.createShip(), true);
        player2.getGameBoard().placeShip(0, 2, zerstörerFactory.createShip(), true);
        player2.getGameBoard().placeShip(0, 4, zerstörerFactory.createShip(), true);
        player2.getGameBoard().placeShip(0, 6, kreuzerFactory.createShip(), true);
        player2.getGameBoard().placeShip(0, 8, kreuzerFactory.createShip(), true);
        player2.getGameBoard().placeShip(6, 0, kreuzerFactory.createShip(), true);
        player2.getGameBoard().placeShip(6, 2, u_BootFactory.createShip(), true);
        player2.getGameBoard().placeShip(6, 4, u_BootFactory.createShip(), true);
        player2.getGameBoard().placeShip(6, 6, u_BootFactory.createShip(), true);
        player2.getGameBoard().placeShip(6, 8, u_BootFactory.createShip(), true);

    }

    public void initializeComputerOpponentGame() {

        player1 = new LocalPlayerFactory().createPlayer("Spieler 1");
        computer = new ComputerPlayerFactory().createPlayer("Computer");

        player1Board = new PlayerBoardFactory().createGameBoard();
        player1TargetingBoard = new TargetingBoardFactory().createGameBoard();
        player2Board = new PlayerBoardFactory().createGameBoard();
        player2TargetingBoard = new TargetingBoardFactory().createGameBoard();

        player1.setGameBoard(player1Board);
        computer.setGameBoard(player2Board);
        player1.setTargetingBoard(player1TargetingBoard);
        computer.setTargetingBoard(player2TargetingBoard);
        
        BattleshipAI battleshipAI = new BattleshipAI(player1, computer);
        battleshipAI.placeAllShips();
    }

    public void initializeComputerOpponentGameDebug() {

        player1 = new LocalPlayerFactory().createPlayer("Spieler 1");
        computer = new ComputerPlayerFactory().createPlayer("Computer");

        player1Board = new PlayerBoardFactory().createGameBoard();
        player1TargetingBoard = new TargetingBoardFactory().createGameBoard();
        player2Board = new PlayerBoardFactory().createGameBoard();
        player2TargetingBoard = new TargetingBoardFactory().createGameBoard();

        player1.setGameBoard(player1Board);
        computer.setGameBoard(player2Board);
        player1.setTargetingBoard(player1TargetingBoard);
        computer.setTargetingBoard(player2TargetingBoard);

        BattleshipAI battleshipAI = new BattleshipAI(player1, computer);
        battleshipAI.placeAllShips();

        SchlachtschiffFactory schlachtschiffFactory = new SchlachtschiffFactory();
        ZerstörerFactory zerstörerFactory = new ZerstörerFactory();

        KreuzerFactory kreuzerFactory = new KreuzerFactory();
        U_BootFactory u_BootFactory = new U_BootFactory();

        player1.getGameBoard().placeShip(0, 0, schlachtschiffFactory.createShip(), true);
        player1.getGameBoard().placeShip(0, 2, zerstörerFactory.createShip(), true);
        player1.getGameBoard().placeShip(0, 4, zerstörerFactory.createShip(), true);
        player1.getGameBoard().placeShip(0, 6, kreuzerFactory.createShip(), true);
        player1.getGameBoard().placeShip(0, 8, kreuzerFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 0, kreuzerFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 2, u_BootFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 4, u_BootFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 6, u_BootFactory.createShip(), true);
        player1.getGameBoard().placeShip(6, 8, u_BootFactory.createShip(), true);
    }

}