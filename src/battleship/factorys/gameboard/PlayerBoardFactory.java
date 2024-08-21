package battleship.factorys.gameboard;

public class PlayerBoardFactory extends GameBoardFactory {
    @Override
    public IGameBoard createGameBoard() {
        return new PlayerBoard();
    }
}
