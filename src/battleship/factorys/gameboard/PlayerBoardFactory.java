/**
 * @file PlayerBoardFactory.java
 * @brief Factory class for creating player game boards in the Battleship game.
 */

package battleship.factorys.gameboard;

/**
 * @class PlayerBoardFactory
 * @brief Factory class for creating player game boards in the Battleship game.
 * Extends the {@link GameBoardFactory} class.
 */
public class PlayerBoardFactory extends GameBoardFactory {

    /**
     * @brief Creates a new player game board.
     * @return A new instance of {@link PlayerBoard}.
     */
    @Override
    public IGameBoard createGameBoard() {
        return new PlayerBoard();
    }
}