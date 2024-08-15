package battleship;

import battleship.factorys.GameBoardFactory;
import battleship.factorys.IGameBoard;

public class PlayerBoardFactory extends GameBoardFactory {
    @Override
    public IGameBoard createGameBoard() {
        return new PlayerBoard();
    }
}
