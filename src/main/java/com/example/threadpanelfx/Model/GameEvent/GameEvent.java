package com.example.threadpanelfx.Model.GameEvent;

public interface GameEvent {
    enum Type
    {
        arrowChanged,
        targetChanged,
        scoresChanged,
        shotsChanged
    }
    public Type GetType();
}
