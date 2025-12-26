package com.saham.hr_system.config;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEvents {

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        System.out.println("✅ WebSocket CONNECTED");
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        System.out.println("❌ WebSocket DISCONNECTED");
    }
}
