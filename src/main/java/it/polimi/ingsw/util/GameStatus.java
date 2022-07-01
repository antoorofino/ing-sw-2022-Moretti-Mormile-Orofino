package it.polimi.ingsw.util;

/**
 * Status of match
 */
public enum GameStatus {
    /**
     * Match still accepts players
     */
    ACCEPT_PLAYERS,
    /**
     * Match is waiting for information from all players in the game,
     * Match doesn't accept other players
     */
    WAITING_ALL_PLAYERS_INFO,
    /**
     * Ready to start a match
     */
    READY_TO_START,
    /**
     * Match is started
     */
    ACTIVE,
    /**
     * Match is not already active
     */
    INACTIVE
}
