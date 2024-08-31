package battleship.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.awt.Point;

import battleship.factorys.hits.IHits;
import battleship.factorys.hits.ShipHitFactory;
import battleship.factorys.player.IPlayer;
import battleship.factorys.ships.IShip;

/**
 * @class ComputerShootingManager
 *        Manages the shooting phase in a Battleship game against a computer
 *        opponent.
 *        Handles player and computer shots, game over conditions, and sunk ship
 *        detection.
 */
public class ComputerShootingManager {

    private final IPlayer player;
    private final IPlayer computer;
    private final BattleshipAI battleshipAI;
    public IPlayer currentPlayer;
    public IPlayer opponentPlayer;
    private final ShipHitFactory hitFactory;

    /**
     * Constructor for ComputerShootingManager.
     * 
     * @param player1  The human player object.
     * @param computer The computer player object.
     */
    public ComputerShootingManager(IPlayer player1, IPlayer computer) {
        this.player = player1;
        this.computer = computer;
        this.battleshipAI = new BattleshipAI(player1, computer);
        this.hitFactory = new ShipHitFactory();
        this.currentPlayer = player1;
        this.opponentPlayer = computer;
    }

    /**
     * Executes the computer's shooting turn using the BattleshipAI.
     */
    public void computerShoot() {
        battleshipAI.makeNextMove();
    }

    /**
     * Adds a hit to the player's targeting board,
     * registering it as a hit or miss on the computer's game board.
     * 
     * @param x The column coordinate of the shot.
     * @param y The row coordinate of the shot.
     * @return True if the shot hit a ship, false otherwise (a miss).
     */
    public boolean addHitToTargetBoard(int x, int y) {
        boolean isHit = isHitHittingShip(x, y);
        IHits hit = hitFactory.createHit(isHit);
        player.getTargetingBoard().placeHit(x, y, hit);
        return isHit;
    }

    /**
     * Checks if a shot at the given coordinates would hit a ship on the computer's
     * game board.
     * 
     * @param x The column coordinate of the shot.
     * @param y The row coordinate of the shot.
     * @return True if the shot would hit a ship, false otherwise.
     */
    public boolean isHitHittingShip(int x, int y) {
        return computer.getGameBoard().isShipHit(x, y);
    }

    /**
     * Checks if the game is over, meaning one player has sunk all the opponent's
     * ships.
     * 
     * @param lastShooter The IPlayer who made the last shot.
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver(IPlayer lastShooter) {
        IPlayer opponent = lastShooter == player ? computer : player;

        // Check if all ships of the opponent have been hit
        for (Map.Entry<Point, IShip> entry : opponent.getGameBoard().getShipLocations().entrySet()) {
            Point shipLocation = entry.getKey();
            IHits hit = lastShooter.getTargetingBoard().getHits().get(shipLocation);

            if (hit == null || !hit.isHit()) {
                return false; // At least one ship cell hasn't been hit yet
            }
        }
        return true; // All opponent's ships have been hit
    }

    /**
     * Determines if a ship has been sunk at the given coordinates.
     * 
     * @param x The column coordinate of the last shot.
     * @param y The row coordinate of the last shot.
     * @return A List of Point objects representing the coordinates of the sunk
     *         ship,
     *         or an empty list if no ship was sunk.
     */
    public List<Point> isShipSunk(int x, int y) {
        IShip ship = getShipAt(x, y);
        if (ship == null) {
            return Collections.emptyList(); // No ship at the given coordinates
        }

        int shipLength = ship.getShipSize();
        int hits = 0;
        List<Point> shipCoordinates = new ArrayList<>();

        for (Map.Entry<Point, IShip> entry : computer.getGameBoard().getShipLocations().entrySet()) {
            if (entry.getValue().equals(ship)) {
                shipCoordinates.add(entry.getKey());
            }
        }

        for (Point location : shipCoordinates) {
            if (player.getTargetingBoard().getHits().containsKey(location)) {
                hits++;
            }
        }

        return hits == shipLength ? shipCoordinates : Collections.emptyList();
    }

    /**
     * Returns the ship located at the given coordinates on the computer's game
     * board.
     * 
     * @param x The column coordinate.
     * @param y The row coordinate.
     * @return The IShip object at the given coordinates, or null if no ship is
     *         present.
     */
    public IShip getShipAt(int x, int y) {
        Map<Point, IShip> ships = computer.getGameBoard().getShipLocations();
        return ships.get(new Point(x, y));
    }

    /**
     * Checks if the player has already fired a shot at the given coordinates.
     * 
     * @param x The column coordinate.
     * @param y The row coordinate.
     * @return True if the coordinates have already been hit, false otherwise.
     */
    public boolean isAlreadyHit(int x, int y) {
        return player.getTargetingBoard().getHits().containsKey(new Point(x, y));
    }

    /**
     * Randomly selects a player (human or computer) to start the game.
     * 
     * @return The IPlayer object that was selected to go first.
     */
    public IPlayer selectRandomPlayer() {
        if (Math.random() < 0.5) {
            this.currentPlayer = player;
            this.opponentPlayer = computer;
        } else {
            this.currentPlayer = computer;
            this.opponentPlayer = player;
        }
        return currentPlayer;
    }

    /**
     * Sets the current player for the shooting phase.
     * 
     * @param player The IPlayer to set as the current player.
     */
    public void setCurrentPlayer(IPlayer player) {
        this.currentPlayer = player;
    }

}
