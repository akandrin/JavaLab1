package com.example.threadpanelfx.Model;

import com.example.threadpanelfx.NetUtility.Invoker.ObjectForInvokeGetter;
import com.example.threadpanelfx.NetUtility.Invoker.RemoteInvoker;
import javafx.geometry.Point2D;

/*public class RemoteGameModel implements IGameModel {

    private RemoteInvoker m_remoteInvoker;

    public RemoteGameModel()
    {
        try {
            var objectForInvokeGetter = new ObjectForInvokeGetter(GameModelPool.class, GameModelPool.class.getMethod("Instance"), GameModelPool.ModelType.local);

            m_remoteInvoker = new RemoteInvoker(objectForInvokeGetter);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double GetArrowOffset(String playerName) {
        try {
            Object result = m_remoteInvoker.Invoke(IGameModel.class.getMethod("GetArrowOffset"), playerName);
            return (Double)result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void SetArrowOffset(String playerName, boolean arrowIsVisible, double offset) {

    }

    @Override
    public Point2D GetArrowHeadStartPositionAbs(String playerName) {
        return null;
    }

    @Override
    public void SetArrowHeadStartPositionAbs(String playerName, Point2D arrowPositionAbs) {

    }

    @Override
    public Point2D GetTargetPositionAbs(int targetNumber) {
        return null;
    }

    @Override
    public void SetTargetPositionAbs(int targetNumber, Point2D targetPosition) {

    }

    @Override
    public int GetScores(String playerName) {
        return 0;
    }

    @Override
    public void SetScores(String playerName, int newScores) {

    }

    @Override
    public void ResetPlayerInfo() {

    }

    @Override
    public int GetShots(String playerName) {
        return 0;
    }

    @Override
    public void SetShots(String playerName, int newShots) {

    }

    @Override
    public void AddPlayer(String playerName) {

    }
}
*/