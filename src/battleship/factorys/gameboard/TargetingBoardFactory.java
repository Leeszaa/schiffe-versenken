/**
 * @file TargetingBoardFactory.java
 * @brief Factory class for creating targeting game boards in the Battleship game.
 */

package battleship.factorys.gameboard;

/**
 * @class TargetingBoardFactory
 * @brief Factory class for creating targeting game boards in the Battleship game.
 * Extends the {@link GameBoardFactory} class.
 */
public class TargetingBoardFactory extends GameBoardFactory {

    /**
     * @brief Creates a new targeting game board.
     * @return A new instance of {@link TargetingBoard}.
     */
    @Override
    public IGameBoard createGameBoard() {
        return new TargetingBoard();
    }
}