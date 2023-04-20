package com.example.chatapp.model;

import com.example.chatapp.utils.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ConnectedUser {

    private String username;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date joinAt;

    public ConnectedUser(String username){
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectedUser that = (ConnectedUser) o;
        return Objects.equals(username, that.username);
    }
}
