package com.example.threadpanelfx.Model;

public class GameModelPool {
    public enum ModelType
    {
        local,
        remote
    }

    private static GameModelPool instance;

    private LocalGameModel m_localGameModel;
    //private RemoteGameModel m_remoteGameModel;

    private GameModelPool()
    {
        m_localGameModel = new LocalGameModel();
    }

    public static GameModelPool Instance()
    {
        if (instance == null)
        {
            instance = new GameModelPool();
        }
        return instance;
    }

    public IGameModel GetModel(ModelType modelType)
    {
        IGameModel model = null;
        switch (modelType)
        {
            case local:
                model = m_localGameModel;
                break;
            /*case remote:
                model = m_remoteGameModel;
                break;*/
            default:
                throw new RuntimeException("Unknown model type");
        }
        return model;
    }

}
