package battleship.managers;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import battleship.factorys.gameboard.*;
import battleship.factorys.hits.IHits;
import battleship.factorys.hits.ShipHitFactory;
import battleship.factorys.ships.*;
import battleship.factorys.player.*;

public class BattleshipAI {

    private Random random;
    private KreuzerFactory KreuzerFactory;
    private ZerstörerFactory ZerstörerFactory;
    private U_BootFactory U_BootFactory;
    private SchlachtschiffFactory SchlachtschiffFactory;
    private List<int[]> hitPositions; // Speichert die Positionen der Treffer
    private boolean inHuntMode = false;
    private boolean inSinkMode = false;
    private int[] lastHit; // Der letzte Treffer
    private ShipHitFactory hitFactory;
    private IPlayer player;
    private IPlayer computer;
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

    // Maximal 100 Versuche pro Schiff bevor der Vorgang neu gestartet wird
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
                // Fehler bei der Platzierung, versuche den Prozess erneut
                System.out.println("Keine gültige Platzierung gefunden, Neustart der Platzierung...");
            }
        }
    }

    private void placeShip(int shipSize, String factoryType) {
        boolean placed = false;
        int attempts = 0;

        System.out.println("Attempting to place ship of size " + shipSize + " using factory " + factoryType);

        while (!placed && attempts < MAX_ATTEMPTS_PER_SHIP) {
            int x = random.nextInt(10);
            int y = random.nextInt(10);
            boolean isHorizontal = random.nextBoolean();

            System.out.println("Attempt " + (attempts + 1) + ": Trying to place ship at (" + x + ", " + y + ") "
                    + (isHorizontal ? "horizontally" : "vertically"));

            if (canPlaceShip(y, x, shipSize, isHorizontal)) {
                switch (factoryType) {
                    case "SchlachtschiffFactory":
                        computer.getGameBoard().placeShip(x, y, SchlachtschiffFactory.createShip(), isHorizontal);
                        System.out.println("Placed Schlachtschiff at (" + x + ", " + y + ")");
                        break;
                    case "KreuzerFactory":
                        computer.getGameBoard().placeShip(x, y, KreuzerFactory.createShip(), isHorizontal);
                        System.out.println("Placed Kreuzer at (" + x + ", " + y + ")");
                        break;
                    case "ZerstörerFactory":
                        computer.getGameBoard().placeShip(x, y, ZerstörerFactory.createShip(), isHorizontal);
                        System.out.println("Placed Zerstörer at (" + x + ", " + y + ")");
                        break;
                    case "U_BootFactory":
                        computer.getGameBoard().placeShip(x, y, U_BootFactory.createShip(), isHorizontal);
                        System.out.println("Placed U-Boot at (" + x + ", " + y + ")");
                        break;
                    default:
                        System.out.println("Unknown factory type: " + factoryType);
                        break;
                }
                placed = true;
            } else {
                System.out.println("Cannot place ship at (" + x + ", " + y + ")");
            }

            attempts++;
        }

        if (!placed) {
            System.out.println("Failed to place ship after " + attempts + " attempts.");
            throw new IllegalStateException("Keine gültige Position für das Schiff gefunden.");
        } else {
            System.out.println("Successfully placed ship after " + attempts + " attempts.");
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
        return true; // Gültige Platzierung++

        /*
         * if (isHorizontal && x + shipSize > 10) {
         * return false;
         * } else if (!isHorizontal && y + shipSize > 10) {
         * return false;
         * }
         * 
         * for (int i = 0; i < shipSize; i++) {
         * int currentCol = isHorizontal ? x + i : x;
         * int currentRow = isHorizontal ? y : y + i;
         * 
         * if (isOccupiedOrAdjacent(currentCol, currentRow)) {
         * return false;
         * }
         * }
         * 
         * return true;
         */
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
        // Get all ship locations
        Map<Point, IShip> shipLocations = computer.getGameBoard().getShipLocations();

        // Use a set to store unique ships to avoid removing the same ship multiple
        // times
        Set<IShip> uniqueShips = new HashSet<>(shipLocations.values());

        // Iterate over all unique ships and remove each ship
        for (IShip ship : uniqueShips) {
            computer.getGameBoard().removeShip(ship);
        }

        // Optionally, clear the ship locations map if needed
        shipLocations.clear();
    }

    // Führt den nächsten Schuss aus
        public void makeNextMove() {
        if (inSinkMode) {
            System.out.println("Entering Sink Mode");
            // Systematisches Versenken im Sink Mode
            sink();
        } else if (inHuntMode) {
            System.out.println("Entering Hunt Mode");
            // Zielgerichtete Schüsse im Hunt Mode
            hunt();
        } else {
            System.out.println("Entering Random Shot Mode");
            // Zufälliger Schuss
            randomShot();
        }
    }

    // Zufälliger Schuss, wenn kein Treffer bekannt ist
    private void randomShot() {
        int col, row;

        // Suche zufällige Position, die noch nicht getroffen wurde
        do {
            col = random.nextInt(10);
            row = random.nextInt(10);
        } while (computer.getTargetingBoard().getHits().containsKey(new Point(col, row)));

        boolean hit = addHitToTargetBoard(col, row);

        System.out.println("Computer shot at (" + col + ", " + row + ") and " + (hit ? "hit a ship" : "missed"));

        // Treffer registrieren
        if (hit) {
            lastHit = new int[] { col, row };
            hitPositions.add(lastHit);
            inHuntMode = true; // Wechsel in den Hunt Mode
            potentialDirections = generatePotentialDirections(col, row); // Mögliche Richtungen speichern
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

    // Logik für gezieltes Schießen, wenn ein Treffer gemacht wurde
    private void hunt() {
        int col = lastHit[0];
        int row = lastHit[1];

        // Versuche, in den Nachbarpositionen zu schießen: rechts, links, oben, unten
        for (int[] direction : potentialDirections) {
            int newCol = col + direction[0];
            int newRow = row + direction[1];

            if (tryShootingAt(newCol, newRow)) {
                return; // Beende den Zug nach einem Schuss
            }
        }

        inHuntMode = false; // Wenn kein weiterer Treffer gefunden wurde, beende den Hunt Mode
        randomShot();

    }

    // Versuche, an einer bestimmten Position zu schießen
    private boolean tryShootingAt(int col, int row) {
        if (col >= 0 && col < 10 && row >= 0 && row < 10
                && !computer.getTargetingBoard().getHits().containsKey(new Point(col, row))) {
            boolean hit = addHitToTargetBoard(col, row);

            if (hit) {
                lastHit = new int[] { col, row };
                hitPositions.add(lastHit);

                if (hitPositions.size() == 2) {
                    inSinkMode = true; // Wenn zwei Treffer hintereinander sind, aktiviere den Sink Mode
                }
                 // Beende den Zug nach einem Schuss
            }
            return true;
        }
        return false;
    }

    // Weitere Logik für den Sink Mode könnte hinzugefügt werden,
    // um sicherzustellen, dass das gesamte Schiff versenkt wird
    private void sink() {
        int col = lastHit[0];
        int row = lastHit[1];
    
        // Bestimme die Richtung basierend auf den ersten zwei Treffern
        int[] firstHit = hitPositions.get(0);
        int[] secondHit = hitPositions.get(1);
    
        int dCol = secondHit[0] - firstHit[0];
        int dRow = secondHit[1] - firstHit[1];
    
        System.out.println("First hit: (" + firstHit[0] + ", " + firstHit[1] + ")");
        System.out.println("Second hit: (" + secondHit[0] + ", " + secondHit[1] + ")");
        System.out.println("Direction: (" + dCol + ", " + dRow + ")");
    
        // Schieße in die erkannte Richtung
        int newCol = col + dCol;
        int newRow = row + dRow;
    
        System.out.println("Trying to shoot at: (" + newCol + ", " + newRow + ")");
        if (!tryShootingAt(newCol, newRow)) {
            // Wenn der Schuss nicht trifft oder außerhalb des Bereichs ist, versuche die
            // andere Richtung
            dCol = -dCol;
            dRow = -dRow;
            newCol = firstHit[0] + dCol;
            newRow = firstHit[1] + dRow;
    
            System.out.println("Trying to shoot in the opposite direction at: (" + newCol + ", " + newRow + ")");
            if (!tryShootingAt(newCol, newRow)) {
                // Handle the case when both directions result in a miss or are out of bounds
                System.out.println("Both directions missed or out of bounds. Exiting sink mode.");
                inSinkMode = false;
                hunt(); // Return to hunt mode
            }
        }
    }

    private List<int[]> generatePotentialDirections(int col, int row) {
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[] { 1, 0 }); // Rechts
        directions.add(new int[] { -1, 0 }); // Links
        directions.add(new int[] { 0, 1 }); // Unten
        directions.add(new int[] { 0, -1 }); // Oben
        return directions;
    }

}