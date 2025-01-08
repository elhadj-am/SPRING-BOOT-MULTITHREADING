package com.example.multithreading_completableFuture.repository;

import com.example.multithreading_completableFuture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
