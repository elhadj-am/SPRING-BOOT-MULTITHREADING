package com.example.multithreading_completableFuture.service;

import com.example.multithreading_completableFuture.entity.User;
import com.example.multithreading_completableFuture.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    private UserRepository  userRepository;

    Object target;
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Async
    public CompletableFuture<List<User>> saveUser(MultipartFile file) throws Exception {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();
        List<User> users = parceCSVFile(file);
        logger.info("Saving list of users with size = {}", users.size(), " "+ Thread.currentThread().getName());
        users = userRepository.saveAll(users);
        long end = System.currentTimeMillis();
        logger.info("Total time {}", (end - startTime));
        return CompletableFuture.completedFuture(users);
    }

    public CompletableFuture findAllUsers() {
        logger.info("Finding all users by " + Thread.currentThread().getName());
        List<User> users = userRepository.findAll();
        return CompletableFuture.completedFuture(users);
    }

    private List<User> parceCSVFile(MultipartFile file) throws Exception {
        final List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] data = line.split(",");
                final User user = new User();
                user.setName(data[0]);
                user.setEmail(data[1]);
                user.setGender(data[2]);
                users.add(user);
            }
            return users;
        }catch (final IOException e) {
            logger.error("Failled to parse CSV file {} ", file.getOriginalFilename());
            throw new Exception("Failled to parse CSV file {} ", e);
        }
    }

}
