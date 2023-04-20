package com.example.chatapp.model;

import com.example.chatapp.utils.CustomDateSerializer;
import com.example.chatapp.utils.SystemUsers;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_message")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "FromUser")
    private String fromUser;

    @Column(name = "ToUser")
    private String toUser;

    @Column(name = "Username")
    private String username;

    @Column(name = "ChatRoomId")
    private Integer chatRoomId;

    @Column(name = "Content")
    private String content;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    public boolean isPublic() {
        return this.toUser == null;
    }

    public boolean isFromAdmin() {
        return this.fromUser.equals(SystemUsers.ADMIN.getUsername());
    }
}
