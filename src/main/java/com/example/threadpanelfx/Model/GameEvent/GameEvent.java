package com.example.threadpanelfx.Model.GameEvent;

public interface GameEvent {
    enum Type
    {
        arrowChanged,
        targetChanged,
        scoresChanged,
        shotsChanged,
        newPlayerAdded
    }
    public Type GetType();
}
