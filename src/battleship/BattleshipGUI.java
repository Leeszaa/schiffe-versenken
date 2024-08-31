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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Random;

/**
 * @class BattleshipGUI
 *        Main GUI class for the Battleship game.
 *        Extends {@link JFrame} to create the main window for the game.
 */
public class BattleshipGUI extends JFrame {

    private static final long serialVersionUID = 6478487785876189474L;
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

        panelCont.add(new MainMenuView(panelCont), "MainMenuView");

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
        player1 = null;
        player2 = null;
        computer = null;
        cardLayout.show(panelCont, "MainMenuView");
    }

    public void showSinglePlacementView() {
        if (player1 == null || computer == null) {
            System.err.println("Players are not initialized. Call initializeComputerOpponentGame first.");
            return;
        }

        SinglePlacementView singlePlacementView = new SinglePlacementView(
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

    public void showComputerShootingViewDebug(boolean isOnePlayerDebug) {
        if (player1 == null || computer == null) {
            System.err.println("Players are not initialized. Call initializeComputerOpponentGame first.");
            return;
        }

        ComputerShootingView computerDebugShootingView = new ComputerShootingView(player1, computer, isOnePlayerDebug, this);
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

        placeAllShips(player1);
        placeAllShips(player2);

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

        placeAllShips(player1);
    }

    private static final int MAX_ATTEMPTS_PER_SHIP = 100;

    private void placeAllShips(IPlayer playerToPlace) {
        boolean allShipsPlaced = false;

        while (!allShipsPlaced) {
            clearBoard(playerToPlace);
            try {
                placeShip(5, "SchlachtschiffFactory", playerToPlace);
                placeShip(4, "KreuzerFactory", playerToPlace);
                placeShip(4, "KreuzerFactory", playerToPlace);
                placeShip(3, "ZerstörerFactory", playerToPlace);
                placeShip(3, "ZerstörerFactory", playerToPlace);
                placeShip(3, "ZerstörerFactory", playerToPlace);
                placeShip(2, "U_BootFactory", playerToPlace);
                placeShip(2, "U_BootFactory", playerToPlace);
                placeShip(2, "U_BootFactory", playerToPlace);
                placeShip(2, "U_BootFactory", playerToPlace);

                allShipsPlaced = true;
            } catch (IllegalStateException e) {
                System.out.println("Keine gültige Platzierung gefunden, Neustart der Platzierung...");
            }
        }
    }

    private void placeShip(int shipSize, String factoryType, IPlayer playerToPlace) {
        boolean placed = false;
        int attempts = 0;
        Random random = new Random();

        while (!placed && attempts < MAX_ATTEMPTS_PER_SHIP) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            boolean isHorizontal = random.nextBoolean();

            if (canPlaceShip(y, x, shipSize, isHorizontal, playerToPlace)) {
                switch (factoryType) {
                    case "SchlachtschiffFactory":
                        SchlachtschiffFactory schlachtschiffFactory = new SchlachtschiffFactory();
                        playerToPlace.getGameBoard().placeShip(x, y, schlachtschiffFactory.createShip(), isHorizontal);
                        break;
                    case "KreuzerFactory":
                        KreuzerFactory kreuzerFactory = new KreuzerFactory();
                        playerToPlace.getGameBoard().placeShip(x, y, kreuzerFactory.createShip(), isHorizontal);
                        break;
                    case "ZerstörerFactory":
                        ZerstörerFactory zerstörerFactory = new ZerstörerFactory();
                        playerToPlace.getGameBoard().placeShip(x, y, zerstörerFactory.createShip(), isHorizontal);
                        break;
                    case "U_BootFactory":
                        U_BootFactory u_BootFactory = new U_BootFactory();
                        playerToPlace.getGameBoard().placeShip(x, y, u_BootFactory.createShip(), isHorizontal);
                        break;
                    default:
                        break;
                }
                placed = true;
            }
            attempts++;
        }
    }

    private boolean canPlaceShip(int row, int col, int shipSize, boolean isHorizontal, IPlayer playerToPlace) {

        for (int i = 0; i < shipSize; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || isAdjacentToShip(r, c, playerToPlace)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAdjacentToShip(int row, int col, IPlayer playerToPlace) {
        Map<Point, IShip> ships = playerToPlace.getGameBoard().getShipLocations();
        return isAdjacent(row, col, ships);
    }

    /**
     * Checks if a cell is adjacent to any ship in the given map.
     * 
     * @param row   The row of the cell.
     * @param col   The column of the cell.
     * @param ships The map of ship locations.
     * @return True if the cell is adjacent to any ship, false otherwise.
     */
    private boolean isAdjacent(int row, int col, Map<Point, IShip> ships) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int r = row + i;
                int c = col + j;
                if (r >= 0 && r < 10 && c >= 0 && c < 10 && ships.containsKey(new Point(c, r))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void clearBoard(IPlayer playerToPlace) {
        Map<Point, IShip> shipLocations = playerToPlace.getGameBoard().getShipLocations();
        Set<IShip> uniqueShips = new HashSet<>(shipLocations.values());
        for (IShip ship : uniqueShips) {
            playerToPlace.getGameBoard().removeShip(ship);
        }
        shipLocations.clear();
    }

}