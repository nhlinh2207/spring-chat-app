package com.example.chatapp.configuration;

import com.example.chatapp.model.ConnectedUser;
import com.example.chatapp.service.IChatRoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Date;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private final IChatRoomService chatRoomService;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event){
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String chatRoomId = accessor.getNativeHeader("chatRoomId").get(0);
        accessor.getSessionAttributes().put("chatRoomId", chatRoomId);
        ConnectedUser joiningUser = new ConnectedUser(event.getUser().getName(),new Date());
        chatRoomService.loadOldMessage(Integer.valueOf(chatRoomId), joiningUser);
        chatRoomService.join(Integer.valueOf(chatRoomId), joiningUser);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) throws JsonProcessingException {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String chatRoomId = headers.getSessionAttributes().get("chatRoomId").toString();
        ConnectedUser leavingUser = new ConnectedUser(event.getUser().getName());
        chatRoomService.leave(Integer.valueOf(chatRoomId), leavingUser);
    }
}
