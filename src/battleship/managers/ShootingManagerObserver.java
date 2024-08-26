package battleship.managers;

import battleship.factorys.player.IPlayer;

public interface ShootingManagerObserver {
    void onPlayerSwitched(IPlayer newPlayer, IPlayer opponentPlayer);
}
