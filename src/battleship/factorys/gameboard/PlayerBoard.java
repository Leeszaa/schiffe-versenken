/**
 * @file PlayerBoard.java
 *   Represents a player's game board in the Battleship game.
 */

 package battleship.factorys.gameboard;

 import java.util.HashMap;
 import java.util.Map;
 import java.awt.*;
 import battleship.factorys.ships.*;
 import battleship.factorys.hits.*;
 
 /**
  * @class PlayerBoard
  * Represents a player's game board in the Battleship game.
  * Implements the {@link IGameBoard} interface.
  */
 public class PlayerBoard implements IGameBoard {
     private final int BOARD_SIZE = 10; /**< The size of the game board */
     private final Map<Point, IShip> ships; /**< A map of ship locations on the board */
 
     /**
      * Constructor for PlayerBoard.
      * Initializes the ships map.
      */
     public PlayerBoard() {
         ships = new HashMap<>();
     }
 
     /**
      * Places a ship on the game board.
      * @param x The x-coordinate of the starting position.
      * @param y The y-coordinate of the starting position.
      * @param ship The ship to be placed.
      * @param isHorizontal True if the ship is placed horizontally, false if vertically.
      * @throws IllegalArgumentException if the ship placement is invalid.
      */
     @Override
     public void placeShip(int x, int y, IShip ship, boolean isHorizontal) {
         if (isValidPlacement(x, y, ship.getShipSize(), isHorizontal)) {
             for (int i = 0; i < ship.getShipSize(); i++) {
                 int newX = isHorizontal ? x + i : x;
                 int newY = isHorizontal ? y : y + i;
                 ships.put(new Point(newX, newY), ship);
             }
         } else {
             throw new IllegalArgumentException("Ungültige Schiffsplatzierung.");
         }
     }
 
     /**
      * Receives a hit on the game board.
      * @param x The x-coordinate of the hit.
      * @param y The y-coordinate of the hit.
      * @param isHit True if the hit was successful, false otherwise.
      */
     @Override
     public void receiveHit(int x, int y, boolean isHit) {
         if (ships.containsKey(new Point(x, y))) {
             IShip ship = ships.get(new Point(x, y));
             // Hier die Logik zum Umgang mit Schiffstreffern einfügen
             
             //if (/* Bedingung für Schiff versenkt */) { 
                 ships.remove(new Point(x, y));
             //}
         }
     }
 
     /**
      *   Displays the game board.
      */
     @Override
     public void display() {
         System.out.println("Player Board");
     }
 
     /**
      * Gets the locations of all ships on the board.
      * @return A map of ship locations.
      */
     @Override
     public Map<Point, IShip> getShipLocations() {
         return new HashMap<>(ships); 
     }
 
     /**
      *   Checks if a ship is hit at the given coordinates.
      * @param x The x-coordinate to check.
      * @param y The y-coordinate to check.
      * @return True if a ship is hit, false otherwise.
      */
     @Override
     public boolean isShipHit(int x, int y) {
         return ships.containsKey(new Point(x, y));
     }
 
     /**
      *   Checks if the placement of a ship is valid.
      * @param x The x-coordinate of the starting position.
      * @param y The y-coordinate of the starting position.
      * @param shipSize The size of the ship.
      * @param isHorizontal True if the ship is placed horizontally, false if vertically.
      * @return True if the placement is valid, false otherwise.
      */
     private boolean isValidPlacement(int x, int y, int shipSize, boolean isHorizontal) {
         if (x < 0 || y < 0 || x >= BOARD_SIZE || y >= BOARD_SIZE) {
             return false; // Außerhalb des Spielfelds
         }
 
         for (int i = 0; i < shipSize; i++) {
             int newX = isHorizontal ? x + i : x;
             int newY = isHorizontal ? y : y + i;
 
             if (newX >= BOARD_SIZE || newY >= BOARD_SIZE || ships.containsKey(new Point(newX, newY))) {
                 return false; // Überschneidung oder außerhalb des Spielfelds
             }
         }
         return true; // Gültige Platzierung
     }

        @Override
        public void removeShip(IShip ship) {
            ships.entrySet().removeIf(entry -> entry.getValue().equals(ship));
        }

        @Override
        public void placeHit(int x, int y, IHits hit) {
            // TODO Auto-generated method stub
        }

        @Override
        public Map<Point, IHits> getHits() {
            // TODO Auto-generated method stub
            return null;
        }
 }