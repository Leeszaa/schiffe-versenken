package battleship.managers;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import battleship.factorys.hits.IHits;
import battleship.factorys.hits.ShipHitFactory;
import battleship.factorys.ships.*;
import battleship.factorys.player.*;

/**
 * @class BattleshipAI
 *        Represents the artificial intelligence (AI) for the Battleship game.
 *        Responsible for placing ships and making shot attempts for the
 *        computer player.
 */
public class BattleshipAI {

    private Random random;
    private final KreuzerFactory KreuzerFactory;
    private final ZerstörerFactory ZerstörerFactory;
    private final U_BootFactory U_BootFactory;
    private final SchlachtschiffFactory SchlachtschiffFactory;
    private List<int[]> hitPositions;
    private boolean inHuntMode = false;
    private boolean inSinkMode = false;
    private int[] lastHit;
    private final ShipHitFactory hitFactory;
    private final IPlayer player;
    private final IPlayer computer;
    private List<int[]> potentialDirections; // Potential directions for Sink Mode

    /**
     * Constructor for the BattleshipAI.
     * 
     * @param player   The human player object.
     * @param computer The computer player object.
     */
    public BattleshipAI(IPlayer player, IPlayer computer) {
        this.player = player;
        this.computer = computer;
        this.hitFactory = new ShipHitFactory();
        this.random = new Random();
        this.hitPositions = new ArrayList<>();
        this.potentialDirections = new ArrayList<>();
        this.KreuzerFactory = new KreuzerFactory();
        this.ZerstörerFactory = new ZerstörerFactory();
        this.U_BootFactory = new U_BootFactory();
        this.SchlachtschiffFactory = new SchlachtschiffFactory();
    }

    /**
     * Maximum number of attempts allowed to place a single ship.
     */
    private static final int MAX_ATTEMPTS_PER_SHIP = 100;

    /**
     * Places all ships for the computer player on the game board.
     * Ensures that ships are placed randomly and without overlapping or touching.
     */
    public void placeAllShips() {
        boolean allShipsPlaced = false;

        while (!allShipsPlaced) {
            clearBoard();
            try {
                placeShip(5, "SchlachtschiffFactory");
                placeShip(4, "KreuzerFactory");
                placeShip(4, "KreuzerFactory");
                placeShip(3, "ZerstörerFactory");
                placeShip(3, "ZerstörerFactory");
                placeShip(3, "ZerstörerFactory");
                placeShip(2, "U_BootFactory");
                placeShip(2, "U_BootFactory");
                placeShip(2, "U_BootFactory");
                placeShip(2, "U_BootFactory");

                allShipsPlaced = true;
            } catch (IllegalStateException e) {
                System.out.println("Keine gültige Platzierung gefunden, Neustart der Platzierung...");
            }
        }
    }

    /**
     * Places a single ship of the given size and type on the computer's game board.
     * 
     * @param shipSize    The size (length) of the ship.
     * @param factoryType The type of ship factory to use (e.g.,
     *                    "SchlachtschiffFactory").
     */
    private void placeShip(int shipSize, String factoryType) {
        boolean placed = false;
        int attempts = 0;

        while (!placed && attempts < MAX_ATTEMPTS_PER_SHIP) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            boolean isHorizontal = random.nextBoolean();

            if (canPlaceShip(y, x, shipSize, isHorizontal)) {
                switch (factoryType) {
                    case "SchlachtschiffFactory":
                        computer.getGameBoard().placeShip(x, y, SchlachtschiffFactory.createShip(), isHorizontal);
                        break;
                    case "KreuzerFactory":
                        computer.getGameBoard().placeShip(x, y, KreuzerFactory.createShip(), isHorizontal);
                        break;
                    case "ZerstörerFactory":
                        computer.getGameBoard().placeShip(x, y, ZerstörerFactory.createShip(), isHorizontal);
                        break;
                    case "U_BootFactory":
                        computer.getGameBoard().placeShip(x, y, U_BootFactory.createShip(), isHorizontal);
                        break;
                    default:
                        break;
                }
                placed = true;
            }
            attempts++;
        }

        if (!placed) {
            throw new IllegalStateException("Failed to place ship after " + MAX_ATTEMPTS_PER_SHIP + " attempts.");
        }
    }

    /**
     * Checks if a ship can be placed at the given coordinates without overlapping
     * or
     * touching other ships.
     * 
     * @param row          The starting row coordinate for the ship placement.
     * @param col          The starting column coordinate for the ship placement.
     * @param shipSize     The size (length) of the ship.
     * @param isHorizontal True if the ship should be placed horizontally, false if
     *                     vertically.
     * @return True if the ship can be placed, false otherwise.
     */
    private boolean canPlaceShip(int row, int col, int shipSize, boolean isHorizontal) {

        for (int i = 0; i < shipSize; i++) {
            int r = isHorizontal ? row : row + i;
            int c = isHorizontal ? col + i : col;

            if (r >= 10 || c >= 10 || isAdjacentToShip(r, c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a cell at the given coordinates is adjacent to any existing ships
     * on the
     * computer's game board.
     * 
     * @param row The row coordinate of the cell.
     * @param col The column coordinate of the cell.
     * @return True if the cell is adjacent to a ship, false otherwise.
     */
    private boolean isAdjacentToShip(int row, int col) {
        Map<Point, IShip> ships = computer.getGameBoard().getShipLocations();
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

    /**
     * Clears the computer's game board, removing all placed ships.
     */
    private void clearBoard() {
        Map<Point, IShip> shipLocations = computer.getGameBoard().getShipLocations();
        Set<IShip> uniqueShips = new HashSet<>(shipLocations.values());
        for (IShip ship : uniqueShips) {
            computer.getGameBoard().removeShip(ship);
        }
        shipLocations.clear();
    }

    /**
     * Determines and executes the computer's next move,
     * following a simple AI strategy.
     */
    public void makeNextMove() {
        if (inSinkMode) {
            sink();
        } else if (inHuntMode) {
            hunt();
        } else {
            randomShot();
        }
    }

    /**
     * Makes a random shot on the player's board, avoiding cells that have already
     * been hit.
     */
    private void randomShot() {
        int col, row;

        do {
            col = random.nextInt(10);
            row = random.nextInt(10);
        } while (computer.getTargetingBoard().getHits().containsKey(new Point(col, row)));

        boolean hit = addHitToTargetBoard(col, row);

        if (hit) {
            lastHit = new int[] { col, row };
            hitPositions.add(lastHit);
            inHuntMode = true;
            potentialDirections = generatePotentialDirections(col, row);
        }
    }

    /**
     * Adds a hit to the computer's targeting board,
     * indicating whether the shot hit a ship or missed.
     * 
     * @param x The column coordinate of the shot.
     * @param y The row coordinate of the shot.
     * @return True if the shot hit a ship, false otherwise.
     */
    private boolean addHitToTargetBoard(int x, int y) {
        boolean isHit = isHitHittingShip(x, y);
        IHits hit = hitFactory.createHit(isHit);
        computer.getTargetingBoard().placeHit(x, y, hit);
        return isHit;
    }

    /**
     * Checks if a shot at the given coordinates would hit a ship on the player's
     * game board.
     * 
     * @param x The column coordinate of the shot.
     * @param y The row coordinate of the shot.
     * @return True if the shot would hit a ship, false otherwise.
     */
    public boolean isHitHittingShip(int x, int y) {
        return player.getGameBoard().isShipHit(x, y);
    }

    /**
     * Executes the "hunt" mode of the AI, where it systematically tries to find
     * adjacent hits after a successful hit.
     */
    private void hunt() {
        int col = lastHit[0];
        int row = lastHit[1];

        for (int[] direction : potentialDirections) {
            int newCol = col + direction[0];
            int newRow = row + direction[1];

            if (tryShootingAt(newCol, newRow)) {
                return;
            }
        }
        inHuntMode = false;
        randomShot();

    }

    /**
     * Attempts to shoot at the given coordinates if the cell is valid and hasn't
     * been hit before.
     * 
     * @param col The column coordinate of the shot.
     * @param row The row coordinate of the shot.
     * @return True if the shot was attempted (whether hit or miss), false if the
     *         cell was invalid or already hit.
     */
    private boolean tryShootingAt(int col, int row) {
        if (col >= 0 && col < 10 && row >= 0 && row < 10
                && !computer.getTargetingBoard().getHits().containsKey(new Point(col, row))) {
            boolean hit = addHitToTargetBoard(col, row);

            if (hit) {
                lastHit = new int[] { col, row };
                hitPositions.add(lastHit);

                if (hitPositions.size() == 2) {
                    inSinkMode = true;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Executes the "sink" mode of the AI, where it tries to sink a ship after
     * finding
     * two consecutive hits.
     */
    private void sink() {
        int col = lastHit[0];
        int row = lastHit[1];

        int[] firstHit = hitPositions.get(0);
        int[] secondHit = hitPositions.get(1);

        int dCol = secondHit[0] - firstHit[0];
        int dRow = secondHit[1] - firstHit[1];

        int newCol = col + dCol;
        int newRow = row + dRow;

        if (!tryShootingAt(newCol, newRow)) {
            // Try shooting in the opposite direction
            dCol = -dCol;
            dRow = -dRow;
            newCol = firstHit[0] + dCol;
            newRow = firstHit[1] + dRow;

            if (!tryShootingAt(newCol, newRow)) {
                // Ship likely sunk, switch back to hunt mode
                inSinkMode = false;
                hunt();
            }
        }
    }

    /**
     * Generates a list of potential shooting directions (up, down, left, right)
     * relative to the given coordinates.
     * 
     * @param col The column coordinate.
     * @param row The row coordinate.
     * @return A list of int arrays, each representing a direction (e.g., {1, 0} for
     *         right).
     */
    private List<int[]> generatePotentialDirections(int col, int row) {
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[] { 1, 0 });
        directions.add(new int[] { -1, 0 });
        directions.add(new int[] { 0, 1 });
        directions.add(new int[] { 0, -1 });
        return directions;
    }

}
