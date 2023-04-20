package com.example.chatapp.service;

import com.example.chatapp.model.Message;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface IMessageService {
    void appendInstantMessageToConversations(Message message) throws JsonProcessingException;
    List<Message> findAllInstantMessagesFor(String username, String chatRoomId);

}
