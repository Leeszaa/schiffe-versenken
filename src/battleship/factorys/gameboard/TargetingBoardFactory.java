package battleship.factorys.gameboard;

public class TargetingBoardFactory extends GameBoardFactory {
    @Override
    public IGameBoard createGameBoard() {
        return new TargetingBoard();
    }
}
