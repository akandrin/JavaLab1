package com.example.threadpanelfx.Model.GameEvent;

public class GameStarted extends GameEvent {
    @Override
    public Type GetType() {
        return Type.gameStarted;
    }
}
