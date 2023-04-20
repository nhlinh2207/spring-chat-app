package com.example.chatapp.service;

import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.model.ConnectedUser;
import com.example.chatapp.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IChatRoomService {

    ChatRoom save(ChatRoom chatRoom);
    List<ChatRoom> findAll();
    ChatRoom findById(Integer id);
    ChatRoom join(Integer chatRoomId, ConnectedUser user);
    ChatRoom leave(Integer chatRoomId, ConnectedUser user) throws JsonProcessingException;
    void sendPublicMessage(Message message) throws JsonProcessingException;
    void sendPrivateMessage(Message message) throws JsonProcessingException;
    void loadOldMessage(Integer chatRoomId, ConnectedUser user);
}
