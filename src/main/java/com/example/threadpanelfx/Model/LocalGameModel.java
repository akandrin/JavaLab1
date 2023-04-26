package com.example.threadpanelfx.Model;

import com.example.threadpanelfx.Model.GameEvent.*;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;

public class LocalGameModel extends Observable implements IGameModel {
    private final ArrayList<String> m_playerNameList = new ArrayList<>(); // используется до начала игры
    private final ArrayList<PlayerInfo> m_playerInfoList = new ArrayList<>();
    private final ArrayList<Point2D> m_targetPositionAbsList = new ArrayList<>(); // две мишени

    private PlayerInfo getPlayerInfoNoThrow(String name)
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

        return result;
    }

    private PlayerInfo getPlayerInfo(String name)
    {
        PlayerInfo result = getPlayerInfoNoThrow(name);
        if (result == null)
            throw new RuntimeException("Name not found");

        return result;
    }

    @Override
    public double GetArrowOffset(String playerName)
    {
        double result = 0;
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        if (playerInfo.arrowIsVisible)
        {
            result = playerInfo.arrowOffset;
        }
        return result;
    }

    @Override
    public void SetArrowOffset(String playerName, boolean arrowIsVisible, double offset)
    {
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        playerInfo.arrowIsVisible = arrowIsVisible;
        playerInfo.arrowOffset = offset;
        Notify(new ArrowChanged(arrowIsVisible, offset, playerName));
    }

    @Override
    public Point2D GetArrowHeadStartPositionAbs(String playerName) {
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        return playerInfo.arrowHeadAbs;
    }

    @Override
    public void SetArrowHeadStartPositionAbs(String playerName, Point2D arrowPositionAbs) {
        PlayerInfo playerInfo = getPlayerInfo(playerName);
        playerInfo.arrowHeadAbs = arrowPositionAbs;
    }

    @Override
    public Point2D GetTargetPositionAbs(int targetNumber)
    {
        return m_targetPositionAbsList.get(targetNumber);
    }

    @Override
    public void SetTargetPositionAbs(int targetNumber, Point2D targetPositionAbs)
    {
        // если в списке нет такого индекса - увеличиваем список
        while (targetNumber >= m_targetPositionAbsList.size())
        {
            m_targetPositionAbsList.add(null);
        }

        m_targetPositionAbsList.set(targetNumber, targetPositionAbs);
        Notify(new TargetChanged(targetNumber, targetPositionAbs));
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
    public void ResetPlayerInfo() {
        for (PlayerInfo playerInfo : m_playerInfoList)
        {
            playerInfo.Reset();
        }
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
        PlayerInfo existedPlayerInfo = getPlayerInfoNoThrow(playerName);
        boolean needToAdd = existedPlayerInfo == null;
        if (needToAdd) {
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.name = playerName;
            m_playerInfoList.add(playerInfo);
        }
        Notify(new NewPlayerAdded(playerName));
    }

    @Override
    public boolean AddPlayerBeforeGameStarts(String playerName) {
        if(m_playerNameList.contains(playerName))
            return false;

        m_playerNameList.add(playerName);
        return true;
    }
}
