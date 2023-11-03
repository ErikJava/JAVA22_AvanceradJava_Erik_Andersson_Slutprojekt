package services;

import utils.Item;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
    private final Queue<Item> buffer = new LinkedList<>();

    public synchronized void add(Item item) {
        buffer.add(item);
        notify();
    }

    public synchronized Item remove() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait();
        }
        return buffer.poll();
    }
}

