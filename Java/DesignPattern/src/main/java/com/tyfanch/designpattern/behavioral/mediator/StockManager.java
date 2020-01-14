package com.tyfanch.designpattern.behavioral.mediator;

public interface StockManager {
    void increase(int number);

    void decrease(int number);

    void clear();

    int getStockNumber();
}
