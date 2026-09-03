package com.example.blogging_api.controller;

import com.example.blogging_api.model.Post;
import com.example.blogging_api.service.PostService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;

    @GetMapping
    public Page<Post> getPosts(
            @RequestParam(required = false) String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        logger.info("GET /posts - term={}, page={}, size={}", term, page, size);
        return postService.getAllPosts(term, page, size, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable Long id) {
        logger.info("GET /posts/{}", id);
        return postService.getPostById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@Valid @RequestBody Post newPost) {
        logger.info("POST /posts - создание поста: {}", newPost.getTitle());
        return postService.createPost(newPost);
    }

    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @Valid @RequestBody Post updatedPost) {
        logger.info("PUT /posts/{} - обновление поста", id);
        return postService.updatePost(id, updatedPost);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id) {
        logger.info("DELETE /posts/{} - удаление поста", id);
        postService.deletePost(id);
    }
}