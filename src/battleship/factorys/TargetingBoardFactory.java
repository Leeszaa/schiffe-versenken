package battleship;

public class TargetingBoardFactory extends GameBoardFactory {
    @Override
    public IGameBoard createGameBoard() {
        return new TargetingBoard();
    }
}
