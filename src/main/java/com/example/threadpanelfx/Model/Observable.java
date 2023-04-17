package com.example.threadpanelfx.Model;

import javafx.application.Platform;

import java.util.ArrayList;

public class Observable implements IObservable {
    private ArrayList<IObserver> observers = new ArrayList<>();

    public void AddObserver(IObserver observer)
    {
        observers.add(observer);
    }

    public void RemoveObserver(IObserver observer)
    {
        observers.remove(observer);
    }

    public void Notify(Object event)
    {
        for (var observer : observers)
        {
            if (observer.CanImmediatelyUpdate())
                observer.Update(event);
            else
                Platform.runLater(()->{observer.Update(event);});
        }
    }
}
