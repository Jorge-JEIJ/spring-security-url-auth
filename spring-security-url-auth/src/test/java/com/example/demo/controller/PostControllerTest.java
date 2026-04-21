package com.example.demo.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PostControllerTest extends Assertions {

    PostController postController;

    @BeforeEach
    void setUp() {
        postController = new PostController();
    }

    @Test
    void publicPost_returnsPublicMessage() throws Exception {
        assertThat(postController.publicPost()).isEqualTo("This post is PUBLIC");
    }

    @Test
    void getPost_returnsPostContent() throws Exception {
        assertThat(postController.getPost(42L)).isEqualTo("Viewing post 42");
    }

    @Test
    void createPost_returnsCreatedMessage() throws Exception {
        assertThat(postController.createPost()).isEqualTo("Post created");
    }

    @Test
    void deletePost_returnsDeletedMessage() throws Exception {
        final var id = 99L;
        assertThat(postController.deletePost(id)).isEqualTo("Post deleted " + id);
    }
}
