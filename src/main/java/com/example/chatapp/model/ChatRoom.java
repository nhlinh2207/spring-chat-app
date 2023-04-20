package com.example.chatapp.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_chatroom")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "ConnectedUsers", columnDefinition = "LONGTEXT CHARACTER SET utf8")
   private String connectedUsers;
}
