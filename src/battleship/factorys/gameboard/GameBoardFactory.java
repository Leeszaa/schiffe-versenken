/**
 * @file GameBoardFactory.java
 *   Abstract factory class for creating game boards in the Battleship game.
 */

 package battleship.factorys.gameboard;

 /**
  * @class GameBoardFactory
  *   Abstract factory class for creating game boards in the Battleship game.
  * Subclasses of this class must implement the {@code createGameBoard} method
  * to provide the logic for creating a specific type of game board.
  */
 public abstract class GameBoardFactory {
     /**
      *   Creates a game board.
      * @return A new instance of a game board implementing the {@link IGameBoard} interface.
      */
     public abstract IGameBoard createGameBoard();
 }