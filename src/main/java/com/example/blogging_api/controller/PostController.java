package com.example.blogging_api.controller;

import com.example.blogging_api.model.Post;
import com.example.blogging_api.model.Category;
import com.example.blogging_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public List<Post> getPosts(@RequestParam(required = false) String term) {
        if (term != null && !term.trim().isEmpty()) {
            return postRepository.searchByTerm(term.trim());
        }
        return postRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post newPost) {
        if (newPost.getTitle() == null || newPost.getTitle().trim().isEmpty() ||
                newPost.getContent() == null || newPost.getContent().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (newPost.getCategory() == null) {
            newPost.setCategory(Category.GENERAL);
        }
        if (newPost.getTags() == null) {
            newPost.setTags(new ArrayList<>());
        }

        Date now = new Date();
        newPost.setCreatedAt(now);
        newPost.setUpdatedAt(now);

        Post savedPost = postRepository.save(newPost);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        if (updatedPost.getTitle() == null || updatedPost.getTitle().trim().isEmpty() ||
                updatedPost.getContent() == null || updatedPost.getContent().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return postRepository.findById(id)
                .map(existingPost -> {
                    existingPost.setTitle(updatedPost.getTitle());
                    existingPost.setContent(updatedPost.getContent());
                    if (updatedPost.getCategory() != null) {
                        existingPost.setCategory(updatedPost.getCategory());
                    }
                    if (updatedPost.getTags() != null) {
                        existingPost.setTags(updatedPost.getTags());
                    }
                    existingPost.setUpdatedAt(new Date());
                    return ResponseEntity.ok(postRepository.save(existingPost));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}