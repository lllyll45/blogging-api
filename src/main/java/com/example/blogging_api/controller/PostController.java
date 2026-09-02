package com.example.blogging_api.controller;

import com.example.blogging_api.model.Category;
import com.example.blogging_api.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private Map<Long, Post> posts = new HashMap<>();
    private Long nextId = 1L;

    public PostController() {
        Post testPost1 = new Post();
        testPost1.setId(nextId++);
        testPost1.setTitle("My First Blog Post");
        testPost1.setContent("This is the content of my first blog post.");
        testPost1.setCategory(Category.TECHNOLOGY);
        testPost1.setTags(List.of("Tech", "Programming"));
        testPost1.setCreatedAt(new Date());
        testPost1.setUpdatedAt(new Date());
        posts.put(testPost1.getId(), testPost1);

        Post testPost2 = new Post();
        testPost2.setId(nextId++);
        testPost2.setTitle("Learning Spring Boot");
        testPost2.setContent("Spring Boot is awesome for building REST APIs!");
        testPost2.setCategory(Category.EDUCATION);
        testPost2.setTags(List.of("Java", "Spring"));
        testPost2.setCreatedAt(new Date());
        testPost2.setUpdatedAt(new Date());
        posts.put(testPost2.getId(), testPost2);
    }

    @GetMapping
    public List<Post> getPosts(@RequestParam(required = false) String term) {
        List<Post> allPosts = new ArrayList<>(posts.values());

        if (term == null || term.trim().isEmpty()) {
            return allPosts;
        }

        List<Post> filtered = new ArrayList<>();
        String termLower = term.toLowerCase().trim();

        for (Post post : allPosts) {
            String title = post.getTitle() != null ? post.getTitle().toLowerCase() : "";
            String content = post.getContent() != null ? post.getContent().toLowerCase() : "";
            String category = post.getCategory() != null ? post.getCategory().toString().toLowerCase() : "";

            if (title.contains(termLower) || content.contains(termLower) || category.contains(termLower)) {
                filtered.add(post);
            }
        }
        return filtered;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        Post post = posts.get(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post newPost) {
        if (newPost.getTitle() == null || newPost.getTitle().trim().isEmpty() ||
                newPost.getContent() == null || newPost.getContent().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        newPost.setId(nextId++);

        if (newPost.getCategory() == null) {
            newPost.setCategory(Category.GENERAL);
        }

        if (newPost.getTags() == null) {
            newPost.setTags(new ArrayList<>());
        }

        Date now = new Date();
        newPost.setCreatedAt(now);
        newPost.setUpdatedAt(now);

        posts.put(newPost.getId(), newPost);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
        if (updatedPost.getTitle() == null || updatedPost.getTitle().trim().isEmpty() ||
                updatedPost.getContent() == null || updatedPost.getContent().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Post existingPost = posts.get(id);
        if (existingPost != null) {
            existingPost.setTitle(updatedPost.getTitle());
            existingPost.setContent(updatedPost.getContent());

            if (updatedPost.getCategory() != null) {
                existingPost.setCategory(updatedPost.getCategory());
            }

            if (updatedPost.getTags() != null) {
                existingPost.setTags(updatedPost.getTags());
            }

            existingPost.setUpdatedAt(new Date());
            return ResponseEntity.ok(existingPost);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Post removed = posts.remove(id);
        if (removed != null) {
            return ResponseEntity.noContent().build(); //204
        }
        return ResponseEntity.notFound().build(); //404
    }
}