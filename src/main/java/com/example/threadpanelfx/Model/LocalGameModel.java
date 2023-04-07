package com.example.threadpanelfx.Model;

import com.example.threadpanelfx.Model.GameEvent.ArrowChanged;
import com.example.threadpanelfx.Model.GameEvent.ScoresChanged;
import com.example.threadpanelfx.Model.GameEvent.ShotsChanged;
import com.example.threadpanelfx.Model.GameEvent.TargetChanged;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;

public class LocalGameModel extends Observable implements IGameModel {
    private ArrayList<PlayerInfo> m_playerInfoList;
    private ArrayList<Point2D> m_targetPositionList; // две мишени

    private PlayerInfo getPlayerInfo(String name)
    {
        if (name == null)
        {
            throw new RuntimeException("Name was null");
        }

        PlayerInfo result = null;

        for (var playerInfo : m_playerInfoList)
        {
            if (name.equals(playerInfo.name))
            {
                result = playerInfo;
                break;
            }
        }

        if (result == null)
            throw new RuntimeException("Name not found");

        return result;
    }

    public LocalGameModel()
    {
        this.m_playerInfoList = new ArrayList<>();
        this.m_targetPositionList = new ArrayList<>();
    }

    @Override
    public Point2D GetArrowPosition(String playerName)
    {
        Point2D result = null;
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        if (playerInfo.arrowIsVisible)
        {
            result = playerInfo.arrowPosition;
        }
        return result;
    }

    @Override
    public void SetArrowPosition(String playerName, boolean arrowIsVisible, Point2D pos)
    {
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        playerInfo.arrowIsVisible = arrowIsVisible;
        playerInfo.arrowPosition = pos;
        Notify(new ArrowChanged(arrowIsVisible, pos));
    }

    @Override
    public Point2D GetTargetPosition(int targetNumber)
    {
        return m_targetPositionList.get(targetNumber);
    }

    @Override
    public void SetTargetPosition(int targetNumber, Point2D targetPosition)
    {
        // если в списке нет такого индекса - увеличиваем список
        while (targetNumber >= m_targetPositionList.size())
        {
            m_targetPositionList.add(null);
        }

        m_targetPositionList.set(targetNumber, targetPosition);
        Notify(new TargetChanged(targetNumber, targetPosition));
    }

    @Override
    public int GetScores(String playerName)
    {
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        return playerInfo.scores;
    }

    @Override
    public void SetScores(String playerName, int newScores)
    {
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        playerInfo.scores = newScores;
        Notify(new ScoresChanged(playerName, newScores));
    }

    @Override
    public int GetShots(String playerName) {
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        return playerInfo.shots;
    }

    @Override
    public void SetShots(String playerName, int newShots) {
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        playerInfo.shots = newShots;
        Notify(new ShotsChanged(playerName, newShots));
    }

    @Override
    public void AddPlayer(String playerName) {
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.name = playerName;
        m_playerInfoList.add(playerInfo);
    }
}
