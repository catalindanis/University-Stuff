package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;

//@Configuration
//@EnableWebSocketSecurity
//public class WebSocketSecurityConfig {
//
//    @Bean
//    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
//        messages
//                .nullDestMatcher().permitAll()
//                .simpSubscribeDestMatchers("/topic/**").authenticated()
//                .anyMessage().authenticated();
//
//        return messages.build();
//    }
//
//    @Bean("csrfChannelInterceptor")
//    public org.springframework.messaging.support.ChannelInterceptor csrfChannelInterceptor() {
//        return new org.springframework.messaging.support.ChannelInterceptor() {};
//    }
//}
