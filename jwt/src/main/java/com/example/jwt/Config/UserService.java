package com.example.jwt.Config;

import com.example.jwt.Repo;
import com.example.jwt.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private Repo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public String addUser(Users users){
        System.out.println("HELLO S");
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        repo.save(users);
        return "User added";
    }
}
