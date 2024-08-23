package battleship.managers;

import battleship.factorys.player.*;
import battleship.factorys.ships.IShip;

import java.util.Map;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.hits.*;

public class ShootingManager {

    private IPlayer player1;
    private IPlayer player2;
    private IPlayer currentPlayer;
    private IPlayer opponentPlayer;
    private ShipHitFactory hitFactory;
    public IGameBoard currentGameBoard;
    public IGameBoard currentTargetBoard;
    public IGameBoard currentOpponentBoard;


    public ShootingManager(IPlayer player1, IPlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.hitFactory = new ShipHitFactory();

        initialShootingBoard();
    }

    public void initialShootingBoard() {
        selectRandomPlayer();
        getCurrentPlayerBoards();
    }

    public void selectRandomPlayer() {
        if (Math.random() < 0.5) {
            this.currentPlayer = player1;
            this.opponentPlayer = player2;
        } else {
            this.currentPlayer = player2;
            this.opponentPlayer = player1;
        }
    }

    public void getCurrentPlayerBoards() {

        this.currentGameBoard = currentPlayer.getGameBoard();
        this.currentTargetBoard = currentPlayer.getTargetingBoard();
        this.currentOpponentBoard = opponentPlayer.getGameBoard();
    }

    public void addHitToTargetBoard(int x, int y) {
        boolean isHit = isHitHittingShip(x, y);
        IHits hit = hitFactory.createHit(isHit);
        currentTargetBoard.placeHit(x, y, hit);
        //currentOpponentBoard.getShipLocations()
    }

    public boolean isHitHittingShip(int x, int y) {
        return currentOpponentBoard.isShipHit(x, y);
    }

    public void switchPlayers() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
            opponentPlayer = player1;
        } else {
            currentPlayer = player1;
            opponentPlayer = player2;
        }
        getCurrentPlayerBoards();
    }
    
}
