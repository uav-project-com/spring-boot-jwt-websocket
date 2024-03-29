package murraco.configuration;

import murraco.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    Logger log = LoggerFactory.getLogger(WebSocketConfig.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/example-endpoint")
                .setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/chat/queue");
        //TODO: config.enableStompBrokerRelay("/topic"); // Uncomment for external message broker (ActiveMQ, RabbitMQ)
        config.setApplicationDestinationPrefixes("/app");// prefix in client queries
        // prefix of user MUST be same as config.enableSimpleBroker() above: "/chat/queue"
        //      client will subscribe: "/chat/queue/{username}/chat/queue/messages"
        config.setUserDestinationPrefix("/chat/queue");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            /**
             * Invoked after the completion of a send regardless of any exception that
             * have been raised thus allowing for proper resource cleanup.
             * <p>Note that this will be invoked only if preSend successfully
             * completed and returned a Message, i.e. it did not return {@code null}.
             * @since 4.1
             */
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                assert accessor != null;
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authorization = accessor.getNativeHeader("authorization");
                    // token WITHOUT 'Bearer '
                    log.info("authorization: {}", authorization);
                    assert authorization != null;
                    jwtTokenProvider.validateToken(authorization.get(0));
                } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    log.info("SUBSCRIBE");
                } else if (StompCommand.SEND.equals(accessor.getCommand())) {
                    log.info("SEND");
                } else if (StompCommand.RECEIPT.equals(accessor.getCommand())) {
                    log.info("RECEIPT");
                } else if (StompCommand.MESSAGE.equals(accessor.getCommand())) {
                    log.info("MESSAGE");
                } else {
                    Object type = accessor.getHeader("simpMessageType");
                    if (null != type && type.toString().equals("HEARTBEAT")) {
                        // DO NOTHING
                    } else {
                        log.error("UNKNOWN, msg type:  {}", accessor.getHeader("simpMessageType"));
                    }

                }
                return message;
            }
        });
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(8 * 1024);
    }
}
