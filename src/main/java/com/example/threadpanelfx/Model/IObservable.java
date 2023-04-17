package com.example.threadpanelfx.Model;

public interface IObservable {
    void AddObserver(IObserver observer);
    void RemoveObserver(IObserver observer);
    void Notify(Object event);
}
