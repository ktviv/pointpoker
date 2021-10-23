package com.ktviv.pointpoker.domain.service;

import com.ktviv.pointpoker.domain.events.EventQueue;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface SessionEventBroadcastStrategy {

    public SseEmitter execute(String sessionId, String userId, EventQueue eventQueue);
}
