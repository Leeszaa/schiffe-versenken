package battleship.managers;

import battleship.factorys.player.*;
import battleship.factorys.ships.IShip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.awt.Point;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.hits.*;

/**
 * @class ShootingManager
 *        Manages the shooting phase in a Battleship game between two players.
 *        Handles player turns, shot validation, hit/miss registration, and game
 *        over detection.
 */
public class ShootingManager {

    private IPlayer player1;
    private IPlayer player2;
    public IPlayer currentPlayer;
    public IPlayer opponentPlayer;
    private final ShipHitFactory hitFactory;
    public IGameBoard currentGameBoard;
    public IGameBoard currentTargetBoard;
    public IGameBoard currentOpponentBoard;

    private List<ShootingManagerObserver> observers = new ArrayList<>();

    /**
     * Constructor for ShootingManager.
     * 
     * @param player1 The first player.
     * @param player2 The second player.
     */
    public ShootingManager(IPlayer player1, IPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1; // Player 1 starts by default
        this.hitFactory = new ShipHitFactory();

        initialShootingBoard();
    }

    /**
     * Initializes the shooting phase by selecting a random starting player
     * and setting up the initial game boards.
     */
    public void initialShootingBoard() {
        selectRandomPlayer();
        getCurrentPlayerBoards();
    }

    /**
     * Adds an observer to the list of ShootingManagerObservers.
     * 
     * @param observer The observer to add.
     */
    public void addObserver(ShootingManagerObserver observer) {
        observers.add(observer);
    }

    /**
     * Notifies all observers of a player switch.
     */
    private void notifyObservers() {
        for (ShootingManagerObserver observer : observers) {
            observer.onPlayerSwitched(currentPlayer, opponentPlayer);
        }
    }

    /**
     * Randomly selects a player to start the game.
     */
    public void selectRandomPlayer() {
        if (Math.random() < 0.5) {
            this.currentPlayer = player1;
            this.opponentPlayer = player2;
        } else {
            this.currentPlayer = player2;
            this.opponentPlayer = player1;
        }
        notifyObservers();
    }

    /**
     * Adds a hit to the current player's targeting board
     * and updates the opponent's game board accordingly.
     * 
     * @param x The column coordinate of the shot.
     * @param y The row coordinate of the shot.
     * @return True if the shot was a hit (a ship was present at the coordinates),
     *         false if it was a miss.
     */
    public boolean addHitToTargetBoard(int x, int y) {
        boolean isHit = isHitHittingShip(x, y);
        IHits hit = hitFactory.createHit(isHit);
        currentTargetBoard.placeHit(x, y, hit);
        return isHit;
    }

    /**
     * Checks if a shot at the given coordinates would hit a ship on the opponent's
     * game board.
     * 
     * @param x The column coordinate of the shot.
     * @param y The row coordinate of the shot.
     * @return True if the shot would hit a ship, false otherwise.
     */
    public boolean isHitHittingShip(int x, int y) {
        return currentOpponentBoard.isShipHit(x, y);
    }

    /**
     * Switches the turn to the next player and updates the relevant game boards.
     */
    public void switchPlayers() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
            opponentPlayer = player1;
        } else {
            currentPlayer = player1;
            opponentPlayer = player2;
        }
        getCurrentPlayerBoards();
        notifyObservers();
    }

    /**
     * Updates the internal references to the current player's game board,
     * targeting board, and the opponent's game board.
     */
    public void getCurrentPlayerBoards() {
        this.currentGameBoard = currentPlayer.getGameBoard();
        this.currentTargetBoard = currentPlayer.getTargetingBoard();
        this.currentOpponentBoard = opponentPlayer.getGameBoard();
    }

    /**
     * Checks if the game is over, meaning all of the opponent's ships have been
     * sunk.
     * 
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        // Check every ship location on the opponent's board
        for (Map.Entry<Point, IShip> entry : currentOpponentBoard.getShipLocations().entrySet()) {
            Point shipLocation = entry.getKey();
            IHits hit = currentTargetBoard.getHits().get(shipLocation);

            if (hit == null || !hit.isHit()) {
                return false; // At least one ship cell is not hit
            }
        }
        // All ship cells have been hit, the game is over
        return true;
    }

    /**
     * Determines if a ship has been sunk at the given coordinates.
     * 
     * @param x The column coordinate of the last shot.
     * @param y The row coordinate of the last shot.
     * @return A List of Point objects representing the coordinates of the sunk
     *         ship,
     *         or an empty list if no ship was sunk at that location.
     */
    public List<Point> isShipSunk(int x, int y) {
        IShip ship = getShipAt(x, y);
        if (ship == null) {
            return Collections.emptyList(); // No ship at these coordinates
        }

        int shipLength = ship.getShipSize();
        int hits = 0;
        List<Point> shipCoordinates = new ArrayList<>();

        // Get all coordinates occupied by the ship on the opponent's board
        for (Map.Entry<Point, IShip> entry : currentOpponentBoard.getShipLocations().entrySet()) {
            if (entry.getValue().equals(ship)) {
                shipCoordinates.add(entry.getKey());
            }
        }

        // Check if each ship coordinate has been hit on the current player's targeting
        // board
        for (Point location : shipCoordinates) {
            if (currentTargetBoard.getHits().containsKey(location)) {
                hits++;
            }
        }

        // If all parts of the ship have been hit, return the ship coordinates,
        // otherwise an empty list
        return hits == shipLength ? shipCoordinates : Collections.emptyList();
    }

    /**
     * Retrieves the IShip object at the specified coordinates on the opponent's
     * game board.
     * 
     * @param x The column coordinate.
     * @param y The row coordinate.
     * @return The IShip object at the given location, or null if no ship is
     *         present.
     */
    public IShip getShipAt(int x, int y) {
        Map<Point, IShip> ships = opponentPlayer.getGameBoard().getShipLocations();
        return ships.get(new Point(x, y));
    }

    /**
     * Checks if the current player has already fired a shot at the given
     * coordinates.
     * 
     * @param x The column coordinate.
     * @param y The row coordinate.
     * @return True if a shot has already been made at these coordinates, false
     *         otherwise.
     */
    public boolean isAlreadyHit(int x, int y) {
        return currentTargetBoard.getHits().containsKey(new Point(x, y));
    }

}
