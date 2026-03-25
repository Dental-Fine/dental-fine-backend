package com.DentalFine.Dental_Fine_BackEnd.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/citas"); // Aquí se emiten mensajes hacia los clientes

        config.setApplicationDestinationPrefixes("/app"); // Es donde el cliente envia mensajes al servidor
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registro){
        registro.addEndpoint("/ws-dental-fine") // URL por defecto donde se conecta el front
                .setAllowedOrigins("http://localhost:5173") // Lo que se configuró en security config, para web sockets
                .withSockJS(); // Fallback por si el navegador no soporta WebSockets puros
    }
}
