//package com.spig.spig.domain.room.signaling.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketStompBrokerConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//        /*  구독(sub) : 접두사로 시작하는 메시지는 브로커가 처리하도록 설정.
//            클라이언트는 이 접두사로 시작하는 채널은 구독해서 메시지를 받음.
//        ex) 소켓 통신에서 사용자가 특정 메시지를 받기 위해 /sub 라는 prefix 기반 메시지 수신을 위해 subscribe를 함*/
//        config.enableSimpleBroker("/sub");
//
//        /*  발행(pub) : 접두사로 시작하는 메시지는 @MessageMapping이 달린 메시지로 라우팅됨.
//            클라이언트가 서버로 메시지를 보낼 때 이 접두사 사용.
//        ex) 소켓 통신에서 사용자가 특정 메시지를 받기 위해 /pub 라는 prefix 기반 메시지 수신을 위해 publish를 함*/
//        config.setApplicationDestinationPrefixes("/pub");
//
//    }
//
    /*
//    *  각각 특정 url에 매핑되는 STOMP 엔드포인트를 등록하고, 선택적으로 SockJS 폴백 옵션을 활성화하고 구성
//    *
//    * */
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry
//                // 클라이언트가 WebSocket에 연결하기 위한 클라이언트가엔드포인트를 "ws-stomp"로 설정
//                .addEndpoint("ws-stomp")
//                // 클라이언트가 origin을 명시적으로 지정
//                .setAllowedOrigins("<http://local:3000>")
//                // WebSocket을 지원하지 않는 브라우저에서도 SockJS를 통해 WebSocket 기능을 사용할 수 있게 설정
//                .withSockJS();
//    }
//}
