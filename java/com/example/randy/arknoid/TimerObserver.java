package com.example.randy.arknoid;

import java.util.List;

public interface TimerObserver {
    public abstract void timerNotify(Paddle pad1, Paddle pad2, List<DynamicItem> ditems, List<Item> sitems);
    public abstract String getIdentifier();
}