package battleship.factorys.player;

import battleship.factorys.gameboard.*;

public interface IPlayer {

    String getName();

    void placeShips(IGameBoard gameBoard);

    boolean takeTurn(IGameBoard opponentBoard);

    void setGameBoard(IGameBoard gameBoard);

    void setTargetingBoard(IGameBoard targetingBoard);

    IGameBoard getGameBoard();

    IGameBoard getTargetingBoard();
}
