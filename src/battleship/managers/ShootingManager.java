package battleship.managers;

import battleship.factorys.player.*;
import battleship.factorys.ships.IShip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.awt.Point;

import battleship.factorys.gameboard.IGameBoard;
import battleship.factorys.hits.*;

public class ShootingManager {

    private IPlayer player1;
    private IPlayer player2;
    public IPlayer currentPlayer;
    public IPlayer opponentPlayer;
    private ShipHitFactory hitFactory;
    public IGameBoard currentGameBoard;
    public IGameBoard currentTargetBoard;
    public IGameBoard currentOpponentBoard;

    private List<ShootingManagerObserver> observers = new ArrayList<>();

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

    public void addObserver(ShootingManagerObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (ShootingManagerObserver observer : observers) {
            observer.onPlayerSwitched(currentPlayer, opponentPlayer);
        }
    }

    public void selectRandomPlayer() {
        if (Math.random() < 0.5) {
            this.currentPlayer = player1;
            this.opponentPlayer = player2;
        } else {
            this.currentPlayer = player2;
            this.opponentPlayer = player1;
        }
        notifyObservers();
    }

    public void getCurrentPlayerBoards() {

        this.currentGameBoard = currentPlayer.getGameBoard();
        this.currentTargetBoard = currentPlayer.getTargetingBoard();
        this.currentOpponentBoard = opponentPlayer.getGameBoard();
    }

    public boolean addHitToTargetBoard(int x, int y) {
        boolean isHit = isHitHittingShip(x, y);
        IHits hit = hitFactory.createHit(isHit);
        currentTargetBoard.placeHit(x, y, hit);
        System.out.println("Hit: " + currentTargetBoard.getHits());
        return isHit;
        // currentOpponentBoard.getShipLocations()
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
        System.out.println("Current player (ShootingManager): " + currentPlayer.getName());
        getCurrentPlayerBoards();
        notifyObservers();
    }

    public boolean isGameOver() {
        for (Map.Entry<Point, IShip> entry : currentOpponentBoard.getShipLocations().entrySet()) {
            Point shipLocation = entry.getKey();
            IHits hit = currentTargetBoard.getHits().get(shipLocation);

            if (hit == null || !hit.isHit()) {
                return false;
            }
        }
        return true;
    }

    public List<Point> isShipSunk(int x, int y) {
        IShip ship = getShipAt(x, y);
        if (ship == null) {
            return Collections.emptyList();
        }
    
        int shipLength = ship.getShipSize();
        int hits = 0;
        List<Point> shipCoordinates = new ArrayList<>();
    
        for (Map.Entry<Point, IShip> entry : currentOpponentBoard.getShipLocations().entrySet()) {
            if (entry.getValue().equals(ship)) {
                shipCoordinates.add(entry.getKey());
            }
        }

        for (Point location : shipCoordinates) {
            if (currentTargetBoard.getHits().containsKey(location)) {
                hits++;
                System.out.println("Hit detected at: " + location);
            } else {
                System.out.println("No hit at: " + location);
            }
        }
    
        System.out.println("Hits: " + hits + ", Ship length: " + shipLength);
    
        return hits == shipLength ? shipCoordinates : Collections.emptyList();
    }

    public IShip getShipAt(int x, int y) {
        Map<Point, IShip> ships = opponentPlayer.getGameBoard().getShipLocations();
        return ships.get(new Point(x, y));
    }

    public boolean isAlreadyHit(int x, int y) {
        return currentTargetBoard.getHits().containsKey(new Point(x, y));
    }

}
