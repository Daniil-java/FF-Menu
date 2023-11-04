package ru.findFood.menu.utils;

import java.util.LinkedList;

public class BoundedQueue<T> extends LinkedList<T> {
    private int limitedSize;

    public BoundedQueue(int limitedSize) {
        this.limitedSize = limitedSize;
    }

    //The deserialization from redis needs this constructor without arguments
    public BoundedQueue() {
    }

    @Override
    public boolean add(T t) {
        while (this.size() == limitedSize && !this.isEmpty()) {
            super.remove();
        }
        super.add(t);
        return true;
    }
}
