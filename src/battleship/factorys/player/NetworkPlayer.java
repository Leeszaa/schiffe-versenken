/**
 * @file NetworkPlayer.java
 *   Implementation of the IPlayer interface for a network player in the Battleship game.
 */

package battleship.factorys.player;

import battleship.factorys.gameboard.IGameBoard;

/**
 * @class NetworkPlayer
 *   Represents a network player in the Battleship game.
 * Implements the {@link IPlayer} interface.
 */
public class NetworkPlayer implements IPlayer {
    private IGameBoard gameBoard; /**< The game board of the player */
    private IGameBoard targetingBoard; /**< The targeting board of the player */
    private String name; /**< The name of the player */

    /**
     *   Constructor for NetworkPlayer.
     * @param name The name of the player.
     */
    public NetworkPlayer(String name) {
        this.name = name;
    }

    /**
     *   Gets the name of the player.
     * @return The name of the player.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *   Sets the player's game board.
     * @param gameBoard The game board to set.
     */
    @Override
    public void setGameBoard(IGameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     *   Sets the player's targeting board.
     * @param targetingBoard The targeting board to set.
     */
    @Override
    public void setTargetingBoard(IGameBoard targetingBoard) {
        this.targetingBoard = targetingBoard;
    }

    /**
     *   Gets the player's game board.
     * @return The player's game board.
     */
    @Override
    public IGameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     *   Gets the player's targeting board.
     * @return The player's targeting board.
     */
    @Override
    public IGameBoard getTargetingBoard() {
        return targetingBoard;
    }
}