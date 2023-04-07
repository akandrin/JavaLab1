package com.example.threadpanelfx.Model;

import com.example.threadpanelfx.Model.GameEvent.ArrowChanged;
import com.example.threadpanelfx.Model.GameEvent.ScoresChanged;
import com.example.threadpanelfx.Model.GameEvent.TargetChanged;
import javafx.geometry.Point2D;

public interface IGameModel {
    public Point2D GetArrowPosition(String playerName);
    public void SetArrowPosition(String playerName, boolean arrowIsVisible, Point2D pos);

    public Point2D GetTargetPosition(int targetNumber);
    public void SetTargetPosition(int targetNumber, Point2D targetPosition);

    public int GetScores(String playerName);
    public void SetScores(String playerName, int newScores);

    public int GetShots(String playerName);
    public void SetShots(String playerName, int newShots);

    public void AddPlayer(String playerName);
}
