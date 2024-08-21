package battleship.factorys.gameboard;

import battleship.factorys.ships.IShip;

import java.awt.*;
import java.util.Map;

public interface IGameBoard {
    void placeShip(int x, int y, IShip ship, boolean isHorizontal);
    void receiveHit(int x, int y, boolean isHit);
    void display();
    Map<Point, IShip> getShipLocations();
    boolean isShipHit(int x, int y);
}