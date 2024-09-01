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
 * The main GUI class for the Battleship game, responsible for creating the window,
 * handling user interactions, and managing different game views. 
 * Extends {@link JFrame}.
 */
public class BattleshipGUI extends JFrame {

    /**
	 * SerialVersionUID for the BattleshipGUI class.
	 */
	private static final long serialVersionUID = 6478487785876189474L;
	private final CardLayout cardLayout; /**< The CardLayout used to switch between different game views. */
    private final JPanel panelCont; /**< The main JPanel that acts as a container for the different views. */

    private IGameBoard player1Board; /**< The game board for Player 1. */
    private IGameBoard player1TargetingBoard; /**< The targeting board for Player 1. */
    private IGameBoard player2Board; /**< The game board for Player 2 (or the computer). */
    private IGameBoard player2TargetingBoard; /**< The targeting board for Player 2 (or the computer). */
    private IPlayer player1; /**< The IPlayer object representing Player 1. */
    private IPlayer player2; /**< The IPlayer object representing Player 2. */
    private IPlayer computer; /**< The IPlayer object representing the computer opponent. */

    /**
     * Constructor for BattleshipGUI.
     * Initializes the JFrame, sets up the CardLayout, and adds the MainMenuView as the starting view.
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
     * Switches to the PlacementView, where players place their ships on their boards.
     * Ensures that player objects are initialized before displaying the view.
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
     * Switches to the ShootingView, where players take turns firing shots at each other's boards. 
     * Ensures that player objects are initialized.
     * 
     * @param isOnePlayerDebug True if single-player debug mode is enabled, false otherwise. 
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

    /**
     * Returns to the main menu (MainMenuView) and resets player objects. 
     */
    public void showMainMenuView() {
        player1 = null;
        player2 = null;
        computer = null;
        cardLayout.show(panelCont, "MainMenuView");
    }

    /**
     * Switches to the SinglePlacementView, where the human player places their ships in single-player mode. 
     * Ensures that player objects are initialized.
     */
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

    /**
     * Switches to the ComputerDebugView for debugging the computer's ship placement.
     * Ensures that player objects are initialized. 
     */
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

    /**
     * Switches to the ComputerShootingView for the main shooting phase against the computer opponent.
     * Ensures that player objects are initialized.
     */
    public void showComputerShootingView() {
        if (player1 == null || computer == null) {
            System.err.println("Players are not initialized. Call initializeComputerOpponentGame first.");
            return;
        }

        ComputerShootingView computerShootingView = new ComputerShootingView(player1, computer, false, this);
        panelCont.add(computerShootingView, "ComputerShootingView");
        cardLayout.show(panelCont, "ComputerShootingView");
    }

    /**
     * Switches to a debug version of the ComputerShootingView.
     * Ensures that player objects are initialized. 
     * 
     * @param isOnePlayerDebug True to enable debug features, false for normal gameplay.
     */
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
     * Initializes the game for local two-player mode (local coop).
     * Creates player objects, game boards, and targeting boards.
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

    /**
     * Initializes the game for debug mode with two local players. 
     * Places ships randomly on the boards for both players. 
     */
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

    /**
     * Initializes the game for single-player mode against a computer opponent.
     * Sets up player and computer objects, boards, and uses the BattleshipAI to place ships for the computer. 
     */
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

    /**
     * Initializes a debug game for single-player mode against a computer opponent. 
     * Places ships randomly for both the human player and the computer.
     */
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

    /**
     * Maximum number of attempts to place a single ship randomly.
     */
    private static final int MAX_ATTEMPTS_PER_SHIP = 100;

    /**
     * Places all ships randomly on the game board for the specified player.
     * 
     * @param playerToPlace The IPlayer object for whom to place the ships.
     */
    private void placeAllShips(IPlayer playerToPlace) {
        boolean allShipsPlaced = false;

        while (!allShipsPlaced) {
            // Clear the board before trying to place ships
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

                allShipsPlaced = true; // All ships successfully placed 
            } catch (IllegalStateException e) {
                // Error placing ships, try again 
                System.out.println("Keine gültige Platzierung gefunden, Neustart der Platzierung...");
            }
        }
    }

    /**
     * Places a single ship of the specified size and type on the board for the given player.
     * 
     * @param shipSize     The length of the ship to place.
     * @param factoryType The name of the ship factory class to use (e.g., "SchlachtschiffFactory").
     * @param playerToPlace The IPlayer object for whom to place the ship. 
     * @throws IllegalStateException if the ship cannot be placed after MAX_ATTEMPTS_PER_SHIP tries.
     */
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

        // If the ship couldn't be placed after many attempts, throw an error
        if (!placed) {
            throw new IllegalStateException("Failed to place ship after " + MAX_ATTEMPTS_PER_SHIP + " attempts.");
        }
    }

    /**
     * Checks if it's possible to place a ship at the given coordinates 
     * on the specified player's game board, without overlapping or touching other ships.
     * 
     * @param row          The row coordinate of the ship's starting position.
     * @param col          The column coordinate of the ship's starting position.
     * @param shipSize     The length of the ship.
     * @param isHorizontal True for horizontal placement, false for vertical.
     * @param playerToPlace The IPlayer object whose board is being checked.
     * @return True if the ship can be placed at the given location, false otherwise.
     */
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

    /**
     * Checks if a cell at the given coordinates is adjacent to any ship on the given player's game board.
     * 
     * @param row           The row coordinate.
     * @param col           The column coordinate.
     * @param playerToPlace The IPlayer whose game board to check.
     * @return True if the cell is adjacent to a ship, false otherwise.
     */
    private boolean isAdjacentToShip(int row, int col, IPlayer playerToPlace) {
        Map<Point, IShip> ships = playerToPlace.getGameBoard().getShipLocations();
        return isAdjacent(row, col, ships);
    }

    /**
     * Checks if a cell at the given coordinates is adjacent to any ship in the provided map of ship locations.
     * 
     * @param row   The row coordinate.
     * @param col   The column coordinate.
     * @param ships The map of ship locations to check against. 
     * @return True if the cell is adjacent to a ship, false otherwise.
     */
    private boolean isAdjacent(int row, int col, Map<Point, IShip> ships) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Skip the cell itself
                if (i == 0 && j == 0) {
                    continue;
                }
                int r = row + i;
                int c = col + j;
                // Check if the adjacent cell is within bounds and contains a ship
                if (r >= 0 && r < 10 && c >= 0 && c < 10 && ships.containsKey(new Point(c, r))) {
                    return true; 
                }
            }
        }
        return false; 
    }

    /**
     * Clears all ships from the game board of the specified player.
     * 
     * @param playerToPlace The IPlayer whose board should be cleared. 
     */
    private void clearBoard(IPlayer playerToPlace) {
        Map<Point, IShip> shipLocations = playerToPlace.getGameBoard().getShipLocations();
        Set<IShip> uniqueShips = new HashSet<>(shipLocations.values());
        for (IShip ship : uniqueShips) {
            playerToPlace.getGameBoard().removeShip(ship);
        }
        shipLocations.clear();
    }

}





