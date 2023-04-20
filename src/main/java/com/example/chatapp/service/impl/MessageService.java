package com.example.chatapp.service.impl;

import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.model.ConnectedUser;
import com.example.chatapp.model.Message;
import com.example.chatapp.repository.IChatRoomRepo;
import com.example.chatapp.repository.IMessageRepo;
import com.example.chatapp.service.IMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService implements IMessageService {

    private final IMessageRepo messageRepo;
    private final IChatRoomRepo chatRoomRepo;


    @Override
    public void appendInstantMessageToConversations(Message message) throws JsonProcessingException {
        if (message.isPublic()) {
            ChatRoom chatRoom = chatRoomRepo.findById(message.getChatRoomId()).get();

            List<LinkedHashMap<String, String>> connectedUsers = new ObjectMapper().readValue(chatRoom.getConnectedUsers(),ArrayList.class);
            for (LinkedHashMap<String, String> user : connectedUsers){
                messageRepo.save(Message.builder()
                        .chatRoomId(message.getChatRoomId())
                        .content(message.getContent())
                        .fromUser(message.getFromUser())
                        .username(user.get("username"))
                        .build()
                );
            }
        } else {
            messageRepo.save(Message.builder()
                    .chatRoomId(message.getChatRoomId())
                    .content(message.getContent())
                    .fromUser(message.getFromUser())
                    .toUser(message.getToUser())
                    .username(message.getFromUser())
                    .build()
            );

            messageRepo.save(Message.builder()
                    .chatRoomId(message.getChatRoomId())
                    .content(message.getContent())
                    .fromUser(message.getFromUser())
                    .toUser(message.getToUser())
                    .username(message.getToUser())
                    .build()
            );
        }
    }

    @Override
    public List<Message> findAllInstantMessagesFor(String username, String chatRoomId) {
        return messageRepo.findByUsernameAndChatRoomId(username, Integer.valueOf(chatRoomId));
    }
}
