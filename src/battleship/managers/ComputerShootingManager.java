package battleship.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.awt.Point;


import battleship.factorys.hits.IHits;
import battleship.factorys.hits.ShipHit;
import battleship.factorys.hits.ShipHitFactory;
import battleship.factorys.player.IPlayer;
import battleship.factorys.ships.IShip;
import battleship.factorys.ships.*;
import battleship.factorys.gameboard.*;



public class ComputerShootingManager {

    private final IPlayer player;
    private final IPlayer computer;
    private final BattleshipAI battleshipAI;
    private IPlayer player2;
    public IPlayer currentPlayer;
    public IPlayer opponentPlayer;
    private ShipHitFactory hitFactory;


    public ComputerShootingManager(IPlayer player1, IPlayer computer) {
        this.player = player1;
        this.computer = computer;
        this.battleshipAI = new BattleshipAI(player1, computer);
        this.hitFactory = new ShipHitFactory();
        this.currentPlayer = player1;
        this.opponentPlayer = computer;
    }

    public void computerShoot() {
        battleshipAI.makeNextMove();
    }

    public boolean addHitToTargetBoard(int x, int y) {
        boolean isHit = isHitHittingShip(x, y);
        IHits hit = hitFactory.createHit(isHit);
        player.getTargetingBoard().placeHit(x, y, hit);
        System.out.println("Hit: " + player.getTargetingBoard().getHits());
        return isHit;
        // currentOpponentBoard.getShipLocations()
    }

    public boolean isHitHittingShip(int x, int y) {
        return computer.getGameBoard().isShipHit(x, y);
    }

     public boolean isGameOver(IPlayer lastShooter) {
        IPlayer opponent = lastShooter == player ? computer : player;

        for (Map.Entry<Point, IShip> entry : opponent.getGameBoard().getShipLocations().entrySet()) {
            Point shipLocation = entry.getKey();
            IHits hit = lastShooter.getTargetingBoard().getHits().get(shipLocation);

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
    
        for (Map.Entry<Point, IShip> entry : computer.getGameBoard().getShipLocations().entrySet()) {
            if (entry.getValue().equals(ship)) {
                shipCoordinates.add(entry.getKey());
            }
        }

        for (Point location : shipCoordinates) {
            if (player.getTargetingBoard().getHits().containsKey(location)) {
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
        Map<Point, IShip> ships = computer.getGameBoard().getShipLocations();
        return ships.get(new Point(x, y));
    }

    public boolean isAlreadyHit(int x, int y) {
        return player.getTargetingBoard().getHits().containsKey(new Point(x, y));
    }

    public IPlayer selectRandomPlayer() {
        if (Math.random() < 0.5) {
            this.currentPlayer = player;
            this.opponentPlayer = computer;
        } else {
            this.currentPlayer = computer;
            this.opponentPlayer = player;
        }
        return currentPlayer;
    }

    public void setCurrentPlayer(IPlayer player) {
        this.currentPlayer = player;
    }
    
}
