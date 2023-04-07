package com.example.threadpanelfx.Model;

public interface IObserver {
    void Update(Object event);
    boolean CanImmediatelyUpdate();
}
