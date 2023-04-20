package com.example.chatapp.repository;

import com.example.chatapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMessageRepo extends JpaRepository<Message, Integer> {

    List<Message> findByUsernameAndChatRoomId(String username, Integer chatRoomId);

}

