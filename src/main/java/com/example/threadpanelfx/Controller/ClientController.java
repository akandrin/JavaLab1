package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.NetUtility.Invoker.ObjectForInvokeGetter;
import com.example.threadpanelfx.NetUtility.Invoker.RemoteInvoker;

public class ClientController implements IController {
    private RemoteInvoker m_remoteInvoker;

    public ClientController()
    {
        m_remoteInvoker = new RemoteInvoker(new ObjectForInvokeGetter(CurrentControllerHolder.class, "Get"));
    }

    @Override
    public void OnReadyForGame(String playerName) {
        m_remoteInvoker.Invoke("OnReadyForGame", new Class[]{String.class}, playerName);
    }

    @Override
    public void OnPauseGame(String playerName) {
        m_remoteInvoker.Invoke("OnPauseGame", new Class[]{String.class}, playerName);
    }

    @Override
    public void OnShot(String playerName) {
        m_remoteInvoker.Invoke("OnShot", new Class[]{String.class}, playerName);
    }

    @Override
    public void OnStopGame(String playerName) {
        throw new RuntimeException("Not implemented");
    }
}
