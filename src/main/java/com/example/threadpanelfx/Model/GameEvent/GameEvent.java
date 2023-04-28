package com.example.threadpanelfx.Model.GameEvent;

import java.io.Serializable;

public abstract class GameEvent implements Serializable {
    public enum Type
    {
        arrowChanged,
        targetChanged,
        scoresChanged,
        shotsChanged,
        newPlayerAdded,
        gameStarted,
        gamePaused,
        gameContinued,
        gameStopped
    }
    abstract public Type GetType();
}
