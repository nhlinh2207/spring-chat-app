package com.example.chatapp.repository;

import com.example.chatapp.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IChatRoomRepo extends JpaRepository<ChatRoom, Integer> {
}
