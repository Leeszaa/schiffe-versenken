/**
 * @file PlayerBoardFactory.java
 *   Factory class for creating player game boards in the Battleship game.
 */

package battleship.factorys.gameboard;

/**
 * @class PlayerBoardFactory
 *   Factory class for creating player game boards in the Battleship game.
 * Extends the {@link GameBoardFactory} class.
 */
public class PlayerBoardFactory extends GameBoardFactory {

    /**
     *   Creates a new player game board.
     * @return A new instance of {@link PlayerBoard}.
     */
    @Override
    public IGameBoard createGameBoard() {
        return new PlayerBoard();
    }
}