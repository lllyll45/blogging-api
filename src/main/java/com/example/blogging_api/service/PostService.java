package com.example.blogging_api.service;

import com.example.blogging_api.model.Category;
import com.example.blogging_api.model.Post;
import com.example.blogging_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts(String term) {
        if (term != null && !term.trim().isEmpty()) {
            return postRepository.searchByTerm(term.trim());
        }
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
    }

    public Post createPost(Post newPost) {

        if (newPost.getCategory() == null) {
            newPost.setCategory(Category.GENERAL);
        }
        if (newPost.getTags() == null) {
            newPost.setTags(new ArrayList<>());
        }

        Date now = new Date();
        newPost.setCreatedAt(now);
        newPost.setUpdatedAt(now);

        return postRepository.save(newPost);
    }

    public Post updatePost(Long id, Post updatedPost) {

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        if (updatedPost.getCategory() != null) {
            existingPost.setCategory(updatedPost.getCategory());
        }
        if (updatedPost.getTags() != null) {
            existingPost.setTags(updatedPost.getTags());
        }
        existingPost.setUpdatedAt(new Date());

        return postRepository.save(existingPost);
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
}