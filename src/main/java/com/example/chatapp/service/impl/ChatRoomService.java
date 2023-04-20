package com.example.chatapp.service.impl;

import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.model.ConnectedUser;
import com.example.chatapp.model.Message;
import com.example.chatapp.repository.IChatRoomRepo;
import com.example.chatapp.service.IChatRoomService;
import com.example.chatapp.service.IMessageService;
import com.example.chatapp.utils.Destination;
import com.example.chatapp.utils.SystemUsers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class ChatRoomService implements IChatRoomService {

    private final IChatRoomRepo chatRoomRepo;
    private final SimpMessagingTemplate webSocketMessagingTemplate;
    private final IMessageService messageService;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        chatRoom.setConnectedUsers("[]");
        return chatRoomRepo.saveAndFlush(chatRoom);
    }

    @Override
    public List<ChatRoom> findAll() {
        return chatRoomRepo.findAll();
    }

    @Override
    public ChatRoom findById(Integer id) {
        return chatRoomRepo.findById(id).get();
    }

    @Override
    public ChatRoom join(Integer chatRoomId, ConnectedUser user) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ChatRoom chatRoom = chatRoomRepo.findById(chatRoomId).get();
            List<ConnectedUser> connectedUsersList = objectMapper.readValue(chatRoom.getConnectedUsers(), ArrayList.class);
            connectedUsersList.add(user);
            String connectedUsers = objectMapper.writeValueAsString(connectedUsersList);
            chatRoom.setConnectedUsers(connectedUsers);
            chatRoom = chatRoomRepo.save(chatRoom);

            sendPublicMessage(
                    Message.builder()
                            .chatRoomId(chatRoomId)
                            .fromUser(SystemUsers.ADMIN.getUsername())
                            .content(user.getUsername()+ " join us :)")
                            .build()
            );

            updateConnectedUsersViaWebSocket(chatRoom);
            return chatRoom;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ChatRoom leave(Integer chatRoomId, ConnectedUser user) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatRoom chatRoom = chatRoomRepo.findById(chatRoomId).get();
        List<LinkedHashMap<String, String>> connectedUsersList = objectMapper.readValue(chatRoom.getConnectedUsers(), ArrayList.class);
        for (LinkedHashMap<String, String> item : connectedUsersList){
            if (item.get("username").equals(user.getUsername())){
                connectedUsersList.remove(item);
                break;
            }
        }
        String connectedUsers = objectMapper.writeValueAsString(connectedUsersList);
        chatRoom.setConnectedUsers(connectedUsers);
        chatRoom = chatRoomRepo.save(chatRoom);
        sendPublicMessage(
                Message.builder()
                        .chatRoomId(chatRoomId)
                        .fromUser(SystemUsers.ADMIN.getUsername())
                        .content(user.getUsername()+ " left us :(")
                        .build()
        );
        updateConnectedUsersViaWebSocket(chatRoom);
        return chatRoom;
    }

    @Override
    public void sendPublicMessage(Message message) throws JsonProcessingException {
        webSocketMessagingTemplate.convertAndSend(
                Destination.publicMessages(message.getChatRoomId()+""),
                message
        );
        messageService.appendInstantMessageToConversations(message);
    }

    @Override
    public void sendPrivateMessage(Message message) throws JsonProcessingException {
        webSocketMessagingTemplate.convertAndSendToUser(
                message.getToUser(),
                Destination.privateMessages(message.getChatRoomId()+""),
                message);

        webSocketMessagingTemplate.convertAndSendToUser(
                message.getFromUser(),
                Destination.privateMessages(message.getChatRoomId()+""),
                message);

        messageService.appendInstantMessageToConversations(message);
    }

    @Override
    public void loadOldMessage(Integer chatRoomId, ConnectedUser user) {
        List<Message> oldMessages = messageService.findAllInstantMessagesFor(user.getUsername(), chatRoomId+"");
        for (Message m : oldMessages){
            if (m.isPublic()){
                webSocketMessagingTemplate.convertAndSend(
                        Destination.publicMessages(m.getChatRoomId()+""),
                        m
                );
            }else {
                webSocketMessagingTemplate.convertAndSendToUser(
                        m.getUsername(),
                        Destination.privateMessages(m.getChatRoomId()+""),
                        m);
            }
        }
    }

    public void updateConnectedUsersViaWebSocket(ChatRoom chatRoom) throws JsonProcessingException {
        List<ConnectedUser> connectedUsers = new ObjectMapper().readValue(chatRoom.getConnectedUsers(), ArrayList.class);
        webSocketMessagingTemplate.convertAndSend(
                Destination.connectedUsers(chatRoom.getId()+""),
               connectedUsers
        );
    }
}
