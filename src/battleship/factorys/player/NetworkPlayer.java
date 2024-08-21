package battleship.factorys.player;

import battleship.factorys.gameboard.*;

public class NetworkPlayer implements IPlayer {

    private IGameBoard gameBoard;
    private IGameBoard targetingBoard;

    private String name;

    public NetworkPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void placeShips(IGameBoard gameBoard) {
    }

    @Override
    public boolean takeTurn(IGameBoard opponentBoard) {
        return true;
    }

    @Override
    public void setGameBoard(IGameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    public void setTargetingBoard(IGameBoard targetingBoard) {
        this.targetingBoard = targetingBoard;
    }

    @Override
    public IGameBoard getGameBoard() {
        return gameBoard;
    }

    @Override
    public IGameBoard getTargetingBoard() {
        return targetingBoard;
    }
}
