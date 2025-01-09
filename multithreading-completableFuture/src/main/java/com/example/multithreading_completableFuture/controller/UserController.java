package com.example.multithreading_completableFuture.controller;

import com.example.multithreading_completableFuture.entity.User;
import com.example.multithreading_completableFuture.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class UserController {
    private  UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity saveUser(@RequestParam (value = "files") MultipartFile[] files) throws Exception {
        for (MultipartFile file:files){
            userService.saveUser(file);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public CompletableFuture<ResponseEntity> findAllUsers () {
        return userService.findAllUsers().thenApply(ResponseEntity::ok);
    }


    @GetMapping(value = "/getUsersByMultiThreads", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity findAllUsersMultiThreads () {
        CompletableFuture<List<User>> usersFuture1 = userService.findAllUsers();
        CompletableFuture<List<User>> usersFuture2 = userService.findAllUsers();
        CompletableFuture<List<User>> usersFuture3 = userService.findAllUsers();
        CompletableFuture.allOf(usersFuture1, usersFuture2, usersFuture3).join();

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
