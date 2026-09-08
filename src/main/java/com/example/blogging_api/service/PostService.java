package com.example.blogging_api.service;

import com.example.blogging_api.model.Category;
import com.example.blogging_api.model.Post;
import com.example.blogging_api.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    public Page<Post> getAllPosts(String term, int page, int size, String sortBy, String sortDir) {
        logger.info("Запрос всех постов: term={}, page={}, size={}, sortBy={}, sortDir={}",
                term, page, size, sortBy, sortDir);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> result;
        if (term != null && !term.trim().isEmpty()) {
            logger.debug("Поиск по термину: {}", term);
            result = postRepository.searchByTerm(term.trim(), pageable);
        } else {
            result = postRepository.findAll(pageable);
        }

        logger.info("Найдено {} постов (всего: {})", result.getNumberOfElements(), result.getTotalElements());
        return result;
    }

    public Post getPostById(Long id) {
        logger.info("Запрос поста с ID: {}", id);

        return postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Пост с ID {} не найден", id);
                    return new RuntimeException("Post not found with id: " + id);
                });
    }

    public Post createPost(Post newPost) {
        logger.info("Создание нового поста: title='{}', category={}",
                newPost.getTitle(), newPost.getCategory());

        if (newPost.getCategory() == null) {
            logger.debug("Категория не указана, устанавливаем GENERAL");
            newPost.setCategory(Category.GENERAL);
        }
        if (newPost.getTags() == null) {
            logger.debug("Теги не указаны, создаем пустой список");
            newPost.setTags(new ArrayList<>());
        }

        Date now = new Date();
        newPost.setCreatedAt(now);
        newPost.setUpdatedAt(now);

        Post savedPost = postRepository.save(newPost);
        logger.info("Пост создан с ID: {}", savedPost.getId());
        return savedPost;
    }

    public Post updatePost(Long id, Post updatedPost) {
        logger.info("Обновление поста с ID: {}", id);
        logger.debug("Новые данные: title='{}', content='{}...'",
                updatedPost.getTitle(),
                updatedPost.getContent().substring(0, Math.min(updatedPost.getContent().length(), 20)));

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Пост с ID {} не найден для обновления", id);
                    return new RuntimeException("Post not found with id: " + id);
                });

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        if (updatedPost.getCategory() != null) {
            existingPost.setCategory(updatedPost.getCategory());
        }
        if (updatedPost.getTags() != null) {
            existingPost.setTags(updatedPost.getTags());
        }
        existingPost.setUpdatedAt(new Date());

        Post savedPost = postRepository.save(existingPost);
        logger.info("Пост с ID {} обновлен", savedPost.getId());
        return savedPost;
    }

    public void deletePost(Long id) {
        logger.info("Удаление поста с ID: {}", id);

        if (!postRepository.existsById(id)) {
            logger.error("Пост с ID {} не найден для удаления", id);
            throw new RuntimeException("Post not found with id: " + id);
        }

        postRepository.deleteById(id);
        logger.info("Пост с ID {} удален", id);
    }
}