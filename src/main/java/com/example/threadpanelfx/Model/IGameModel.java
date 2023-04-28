package com.example.threadpanelfx.Model;

import com.example.threadpanelfx.Model.GameEvent.ArrowChanged;
import com.example.threadpanelfx.Model.GameEvent.ScoresChanged;
import com.example.threadpanelfx.Model.GameEvent.TargetChanged;
import javafx.geometry.Point2D;

import java.util.List;

public interface IGameModel {
    double GetArrowOffset(String playerName);
    void SetArrowOffset(String playerName, boolean arrowIsVisible, double offset);

    Point2D GetArrowHeadStartPositionAbs(String playerName);
    void SetArrowHeadStartPositionAbs(String playerName, Point2D arrowPositionAbs);

    Point2D GetTargetPositionAbs(int targetNumber);
    void SetTargetPositionAbs(int targetNumber, Point2D targetPosition);

    int GetScores(String playerName);
    void SetScores(String playerName, int newScores);

    void ResetPlayerInfo(); // обнуление всех данных, кроме имени игрока и состояния готовности

    int GetShots(String playerName);
    void SetShots(String playerName, int newShots);

    boolean AddPlayer(String playerName, boolean notify);

    boolean UpdatePlayerState(String playerName, boolean isPlayerReady);
    boolean PlayersReady();
    void NotifyAboutPlayers();
    List<PlayerInfo> GetPlayersInfoCopy();

    enum GameState
    {
        started,
        paused,
        continued, // active?
        stopped
    }
    void SetGameStarted();
    void SetGamePaused(String playerName);
    void SetGameContinued();
    void SetGameStopped(String winnerName);
    GameState GetGameState();
}
