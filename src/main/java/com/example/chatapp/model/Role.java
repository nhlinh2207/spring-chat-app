package com.example.chatapp.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tbl_role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name", columnDefinition = "VARCHAR(255)")
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;
}
