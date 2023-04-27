package com.example.threadpanelfx.Model.GameEvent;

public class GameStopped extends GameEvent {
    @Override
    public Type GetType() {
        return Type.gameStopped;
    }
}
