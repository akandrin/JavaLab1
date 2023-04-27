package com.example.threadpanelfx.Model;

public class PlayerState implements Cloneable {
    public String playerName;
    public boolean playerIsReady;
    public PlayerState(String playerName, boolean playerIsReady)
    {
        this.playerName = playerName;
        this.playerIsReady = playerIsReady;
    }

    @Override
    public PlayerState clone() {
        try {
            PlayerState copy = (PlayerState) super.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
