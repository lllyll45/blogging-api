package com.example.blogging_api;

import com.example.blogging_api.model.Category;
import com.example.blogging_api.model.Post;
import com.example.blogging_api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PostRepository postRepository;

    @Override
    public void run(String... args) throws Exception {
        if (postRepository.count() == 0) {
            Post post1 = new Post();
            post1.setTitle("My First Blog Post");
            post1.setContent("This is the content of my first blog post.");
            post1.setCategory(Category.TECHNOLOGY);
            post1.setTags(List.of("Tech", "Programming"));
            post1.setCreatedAt(new Date());
            post1.setUpdatedAt(new Date());
            postRepository.save(post1);

            Post post2 = new Post();
            post2.setTitle("Learning Spring Boot");
            post2.setContent("Spring Boot is awesome for building REST APIs!");
            post2.setCategory(Category.EDUCATION);
            post2.setTags(List.of("Java", "Spring"));
            post2.setCreatedAt(new Date());
            post2.setUpdatedAt(new Date());
            postRepository.save(post2);

            System.out.println("Тестовые посты добавлены");
        }
    }
}