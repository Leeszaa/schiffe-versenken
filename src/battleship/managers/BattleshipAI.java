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
    private List<int[]> potentialDirections; // Mögliche Richtungen für den Sink Mode

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

    private static final int MAX_ATTEMPTS_PER_SHIP = 100;

    public void placeAllShips() {
        boolean allShipsPlaced = false;

        while (!allShipsPlaced) {
            // Leeres Spielfeld resetten
            clearBoard();
            try {
                // Ein Schlachtschiff (5 Felder)
                placeShip(5, "SchlachtschiffFactory");
                // Zwei Kreuzer (je 4 Felder)
                placeShip(4, "KreuzerFactory");
                placeShip(4, "KreuzerFactory");
                // Drei Zerstörer (je 3 Felder)
                placeShip(3, "ZerstörerFactory");
                placeShip(3, "ZerstörerFactory");
                placeShip(3, "ZerstörerFactory");
                // Vier U-Boote (je 2 Felder)
                placeShip(2, "U_BootFactory");
                placeShip(2, "U_BootFactory");
                placeShip(2, "U_BootFactory");
                placeShip(2, "U_BootFactory");

                allShipsPlaced = true; // Alle Schiffe erfolgreich platziert
            } catch (IllegalStateException e) {
                System.out.println("Keine gültige Platzierung gefunden, Neustart der Platzierung...");
            }
        }
    }

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
    }

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

    private void clearBoard() {
        Map<Point, IShip> shipLocations = computer.getGameBoard().getShipLocations();
        Set<IShip> uniqueShips = new HashSet<>(shipLocations.values());
        for (IShip ship : uniqueShips) {
            computer.getGameBoard().removeShip(ship);
        }
        shipLocations.clear();
    }

    public void makeNextMove() {
        if (inSinkMode) {
            sink();
        } else if (inHuntMode) {
            hunt();
        } else {
            randomShot();
        }
    }

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

    private boolean addHitToTargetBoard(int x, int y) {
        boolean isHit = isHitHittingShip(x, y);
        IHits hit = hitFactory.createHit(isHit);
        computer.getTargetingBoard().placeHit(x, y, hit);
        return isHit;
    }

    public boolean isHitHittingShip(int x, int y) {
        return player.getGameBoard().isShipHit(x, y);
    }

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

            dCol = -dCol;
            dRow = -dRow;
            newCol = firstHit[0] + dCol;
            newRow = firstHit[1] + dRow;

            if (!tryShootingAt(newCol, newRow)) {
                inSinkMode = false;
                hunt();
            }
        }
    }

    private List<int[]> generatePotentialDirections(int col, int row) {
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[] { 1, 0 });
        directions.add(new int[] { -1, 0 });
        directions.add(new int[] { 0, 1 });
        directions.add(new int[] { 0, -1 });
        return directions;
    }

}