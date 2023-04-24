package com.example.threadpanelfx.Model;

public class PlayerSettings {
    private static String m_playerName = "";

    public static void SetPlayerName(String playerName)
    {
        m_playerName = playerName;
    }

    public static String GetPlayerName()
    {
        return m_playerName;
    }
}
