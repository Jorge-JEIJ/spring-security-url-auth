package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @GetMapping("/public")
    public String publicPost() {
        return "This post is PUBLIC";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String getPost(@PathVariable Long id) {
        return "Viewing post " + id;
    }

    @PostMapping
    @PreAuthorize("hasRole( 'USER' )")
    public String createPost() {
        return "Post created";
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole( 'ADMIN' )")
    public String deletePost(@PathVariable Long id) {
        return "Post deleted " + id;
    }
}
