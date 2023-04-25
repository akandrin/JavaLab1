package com.example.threadpanelfx.Controller;

public class CurrentControllerHolder {
    static private IController m_controller;

    static public IController Get()
    {
        return m_controller;
    }

    static public void Set(IController controller)
    {
        m_controller = controller;
    }
}
