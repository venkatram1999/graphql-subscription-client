package com.mockserver.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.WebSocketGraphQlClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;

@Configuration
public class GraphQlClientConfig {

    @Bean
    public WebSocketGraphQlClient webSocketGraphQlClient(
            @Value("${employee.subscription.server-url}") String serverUrl) {

        ReactorNettyWebSocketClient webSocketClient = new ReactorNettyWebSocketClient();

        return WebSocketGraphQlClient.builder(serverUrl, webSocketClient).build();
    }
}
