/**
 * @file TargetingBoardFactory.java
 *   Factory class for creating targeting game boards in the Battleship game.
 */

package battleship.factorys.gameboard;

/**
 * @class TargetingBoardFactory
 *   Factory class for creating targeting game boards in the Battleship game.
 * Extends the {@link GameBoardFactory} class.
 */
public class TargetingBoardFactory extends GameBoardFactory {

    /**
     *   Creates a new targeting game board.
     * @return A new instance of {@link TargetingBoard}.
     */
    @Override
    public IGameBoard createGameBoard() {
        return new TargetingBoard();
    }
}