package com.ktviv.pointpoker.domain.service.config;

import com.ktviv.pointpoker.domain.service.PokerSessionRepository;
import com.ktviv.pointpoker.domain.service.SessionEventBroadcastStrategy;
import com.ktviv.pointpoker.domain.service.SessionUserEventService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
@Getter
public class PointPokerServiceConfig {

    private final PokerSessionRepository pokerSessionRepository;
    private final Clock clock;
    private final SessionUserEventService sessionUserEventService;
    private final SessionEventBroadcastStrategy sessionEventBroadcastStrategy;
    private final ApplicationEventMulticaster applicationEventMulticaster;

    @Autowired
    public PointPokerServiceConfig(PokerSessionRepository pokerSessionRepository,
                                   Clock clock,
                                   SessionUserEventService sessionUserEventService,
                                   SessionEventBroadcastStrategy sessionEventBroadcastStrategy,
                                   ApplicationEventMulticaster applicationEventMulticaster) {
        this.pokerSessionRepository = pokerSessionRepository;
        this.clock = clock;
        this.sessionUserEventService = sessionUserEventService;
        this.sessionEventBroadcastStrategy = sessionEventBroadcastStrategy;
        this.applicationEventMulticaster = applicationEventMulticaster;
    }
}
