/**
 * @file LocalPlayer.java
 *   Implementation of the IPlayer interface for a local player in the Battleship game.
 */

package battleship.factorys.player;

import battleship.factorys.gameboard.IGameBoard;

/**
 * @class LocalPlayer
 *   Represents a local player in the Battleship game.
 * Implements the {@link IPlayer} interface.
 */
public class LocalPlayer implements IPlayer {
    private String name; /**< The name of the player */
    private IGameBoard gameBoard; /**< The game board of the player */
    private IGameBoard targetingBoard; /**< The targeting board of the player */

    /**
     *   Constructor for LocalPlayer.
     * @param name The name of the player.
     */
    public LocalPlayer(String name) {
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
     *   Places ships on the given game board.
     * @param gameBoard The game board on which to place ships.
     */
    @Override
    public void placeShips(IGameBoard gameBoard) {
        // Implementation for placing ships
    }

    /**
     *   Takes a turn by attacking the opponent's board.
     * @param opponentBoard The opponent's game board.
     * @return True if the turn was successful, false otherwise.
     */
    @Override
    public boolean takeTurn(IGameBoard opponentBoard) {
        // Implementation for taking a turn
        return true;
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