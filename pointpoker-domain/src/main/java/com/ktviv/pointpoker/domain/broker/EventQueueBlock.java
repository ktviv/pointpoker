package com.ktviv.pointpoker.domain.broker;

import com.ktviv.pointpoker.domain.events.PokerEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventQueueBlock {

    private final BlockingQueue<PokerEvent> pokerEventQueue;
    private final AtomicBoolean active;

    public EventQueueBlock() {

        this.pokerEventQueue = new PriorityBlockingQueue<>();
        this.active = new AtomicBoolean(false);
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
