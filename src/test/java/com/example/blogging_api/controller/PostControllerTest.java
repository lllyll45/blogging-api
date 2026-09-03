package com.example.blogging_api.controller;

import com.example.blogging_api.model.Category;
import com.example.blogging_api.model.Post;
import com.example.blogging_api.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class PostControllerTest {

    private MockMvc mockMvc;
    private PostService postService = mock(PostService.class);
    private ObjectMapper objectMapper = new ObjectMapper();
    private Post testPost;

    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("Test Content");
        testPost.setCategory(Category.TECHNOLOGY);

        PostController postController = new PostController(postService);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }


    @Test
    void getAllPosts_ShouldReturnPageOfPosts() throws Exception {
        Page<Post> page = new PageImpl<>(List.of(testPost), PageRequest.of(0, 10), 1);

        when(postService.getAllPosts(any(), anyInt(), anyInt(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].title").value("Test Post"));
    }

    @Test
    void getAllPosts_WithSearchTerm_ShouldReturnFilteredPosts() throws Exception {
        Page<Post> page = new PageImpl<>(List.of(testPost), PageRequest.of(0, 10), 1);

        when(postService.getAllPosts(eq("Test"), anyInt(), anyInt(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/posts")
                        .param("term", "Test")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Test Post"));
    }

    @Test
    void getAllPosts_WhenNoPosts_ShouldReturnEmptyPage() throws Exception {
        Page<Post> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        when(postService.getAllPosts(any(), anyInt(), anyInt(), any(), any()))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/posts")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }


    @Test
    void getPostById_ShouldReturnPost_WhenExists() throws Exception {
        when(postService.getPostById(1L)).thenReturn(testPost);

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Post"));
    }


    @Test
    void createPost_ShouldReturnCreatedPost() throws Exception {
        Post newPost = new Post();
        newPost.setTitle("New Post");
        newPost.setContent("New Content");
        newPost.setCategory(Category.SCIENCE);

        Post savedPost = new Post();
        savedPost.setId(2L);
        savedPost.setTitle("New Post");
        savedPost.setContent("New Content");
        savedPost.setCategory(Category.SCIENCE);

        when(postService.createPost(any(Post.class))).thenReturn(savedPost);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("New Post"));
    }

    @Test
    void createPost_WithEmptyTitle_ShouldReturnBadRequest() throws Exception {
        Post invalidPost = new Post();
        invalidPost.setTitle("");
        invalidPost.setContent("Content");
        invalidPost.setCategory(Category.TECHNOLOGY);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPost_WithEmptyContent_ShouldReturnBadRequest() throws Exception {
        Post invalidPost = new Post();
        invalidPost.setTitle("Title");
        invalidPost.setContent("");
        invalidPost.setCategory(Category.TECHNOLOGY);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPost_WithTitleTooLong_ShouldReturnBadRequest() throws Exception {
        String longTitle = "a".repeat(101);
        Post invalidPost = new Post();
        invalidPost.setTitle(longTitle);
        invalidPost.setContent("Content");
        invalidPost.setCategory(Category.TECHNOLOGY);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updatePost_ShouldReturnUpdatedPost() throws Exception {
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated Content");
        updatedPost.setCategory(Category.EDUCATION);

        Post savedPost = new Post();
        savedPost.setId(1L);
        savedPost.setTitle("Updated Title");
        savedPost.setContent("Updated Content");
        savedPost.setCategory(Category.EDUCATION);

        when(postService.updatePost(eq(1L), any(Post.class))).thenReturn(savedPost);

        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPost)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }


    @Test
    void updatePost_WithEmptyTitle_ShouldReturnBadRequest() throws Exception {
        Post invalidPost = new Post();
        invalidPost.setTitle("");
        invalidPost.setContent("Content");
        invalidPost.setCategory(Category.TECHNOLOGY);

        mockMvc.perform(put("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deletePost_ShouldReturn204_WhenDeleted() throws Exception {
        doNothing().when(postService).deletePost(1L);

        mockMvc.perform(delete("/posts/1"))
                .andExpect(status().isNoContent());
    }

}