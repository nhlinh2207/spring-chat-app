package com.example.chatapp.repository;

import com.example.chatapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepo extends JpaRepository<Role, Integer> {
    Role findByName(String name);

}
