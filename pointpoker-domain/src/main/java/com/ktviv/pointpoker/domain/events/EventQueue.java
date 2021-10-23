package com.ktviv.pointpoker.domain.events;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventQueue {

    private final BlockingQueue<PokerEvent> pokerEventQueue;
    private final AtomicBoolean active;

    public EventQueue() {

        this(false);
    }

    public EventQueue(boolean activated) {

        this.pokerEventQueue = new PriorityBlockingQueue<>();
        this.active = new AtomicBoolean(activated);
    }

    public void addEvent(PokerEvent pokerEvent) {

        this.pokerEventQueue.add(pokerEvent);
    }

    public BlockingQueue<PokerEvent> getEvents() {

        return this.pokerEventQueue;
    }

    public void clearEvents() {

        this.pokerEventQueue.clear();
    }

    public void clearAll() {

        this.clearEvents();
        this.disable();
    }

    public boolean isActive() {

        return active.get();
    }

    public boolean enable() {

        return active.compareAndSet(false, true);
    }

    public boolean disable() {

        return active.compareAndSet(true, false);
    }
}
