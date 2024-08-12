package battleship;

public interface GameBoardFactory {
    GameBoard createGameBoard();
}

class DefaultGameBoardFactory implements GameBoardFactory {
    @Override
    public GameBoard createGameBoard() {
        return new GameBoard();
    }
}