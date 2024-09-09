package com.example.jwt;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Repo extends JpaRepository<Users,Long> {
    Optional<Users> findByName(String username);
}
