package battleship.managers;

import battleship.factorys.player.IPlayer;

/**
 * @interface ShootingManagerObserver
 *            An interface for observing changes in player turns during the
 *            shooting phase
 *            managed by the ShootingManager.
 */
public interface ShootingManagerObserver {
    /**
     * Called when the current player in the shooting phase is switched.
     * 
     * @param newPlayer      The new IPlayer whose turn it is now.
     * @param opponentPlayer The IPlayer who is now the opponent.
     */
    void onPlayerSwitched(IPlayer newPlayer, IPlayer opponentPlayer);
}
