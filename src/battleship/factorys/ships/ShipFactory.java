/**
 * @file ShipFactory.java
 *   Abstract factory class for creating ships in the Battleship game.
 */

 package battleship.factorys.ships;

 /**
  * @class ShipFactory
  *   Abstract factory class for creating ships in the Battleship game.
  */
 public abstract class ShipFactory {
     /**
      *   Creates a ship.
      * @return A new instance of a ship implementing the {@link IShip} interface.
      */
     public abstract IShip createShip();
 }