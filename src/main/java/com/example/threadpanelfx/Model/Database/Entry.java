package com.example.threadpanelfx.Model.Database;

import javax.persistence.*;

@Entity
@Table (name = "high_scores_table")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String playerName;
    private int winCount; // число побед

    public Entry(){}

    public Entry(String playerName, int winCount)
    {
        this.playerName = playerName;
        this.winCount = winCount;
    }

    public int GetId()
    {
        return id;
    }

    public String GetPlayerName()
    {
        return playerName;
    }

    public void SetPlayerName(String playerName)
    {
        this.playerName = playerName;
    }

    public int GetWinCount()
    {
        return winCount;
    }

    public void SetWinCount(int winCount)
    {
        this.winCount = winCount;
    }
}
