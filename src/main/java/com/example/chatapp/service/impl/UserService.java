package com.example.chatapp.service.impl;

import com.example.chatapp.model.Role;
import com.example.chatapp.model.User;
import com.example.chatapp.repository.IRoleRepo;
import com.example.chatapp.repository.IUserRepo;
import com.example.chatapp.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final IUserRepo userRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IRoleRepo roleRepo;

    @Override
    public User createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Role userRole = roleRepo.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();
        roles.add(userRole);
        user.setRoles(roles);
        return userRepo.saveAndFlush(user);
    }

}
