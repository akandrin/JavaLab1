package com.example.threadpanelfx.Model;

import com.example.threadpanelfx.Model.GameEvent.ArrowChanged;
import com.example.threadpanelfx.Model.GameEvent.ScoresChanged;
import com.example.threadpanelfx.Model.GameEvent.TargetChanged;
import javafx.geometry.Point2D;

public interface IGameModel {
    public double GetArrowOffset(String playerName);
    public void SetArrowOffset(String playerName, boolean arrowIsVisible, double offset);

    public Point2D GetArrowHeadStartPositionAbs(String playerName);
    public void SetArrowHeadStartPositionAbs(String playerName, Point2D arrowPositionAbs);

    public Point2D GetTargetPositionAbs(int targetNumber);
    public void SetTargetPositionAbs(int targetNumber, Point2D targetPosition);

    public int GetScores(String playerName);
    public void SetScores(String playerName, int newScores);

    public void ResetPlayerInfo(); // обнуление всех данных, кроме имени игрока

    public int GetShots(String playerName);
    public void SetShots(String playerName, int newShots);

    public void AddPlayer(String playerName);
}
