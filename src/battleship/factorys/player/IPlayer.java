/**
 * @file IPlayer.java
 * @brief Interface for a player in the Battleship game.
 */

package battleship.factorys.player;

import battleship.factorys.gameboard.IGameBoard;

/**
 * @interface IPlayer
 * @brief Interface for a player in the Battleship game.
 */
public interface IPlayer {

    /**
     * @brief Gets the name of the player.
     * @return The name of the player.
     */
    String getName();

    /**
     * @brief Places ships on the given game board.
     * @param gameBoard The game board on which to place ships.
     */
    void placeShips(IGameBoard gameBoard);

    /**
     * @brief Takes a turn by attacking the opponent's board.
     * @param opponentBoard The opponent's game board.
     * @return True if the turn was successful, false otherwise.
     */
    boolean takeTurn(IGameBoard opponentBoard);

    /**
     * @brief Sets the player's game board.
     * @param gameBoard The game board to set.
     */
    void setGameBoard(IGameBoard gameBoard);

    /**
     * @brief Sets the player's targeting board.
     * @param targetingBoard The targeting board to set.
     */
    void setTargetingBoard(IGameBoard targetingBoard);

    /**
     * @brief Gets the player's game board.
     * @return The player's game board.
     */
    IGameBoard getGameBoard();

    /**
     * @brief Gets the player's targeting board.
     * @return The player's targeting board.
     */
    IGameBoard getTargetingBoard();
}