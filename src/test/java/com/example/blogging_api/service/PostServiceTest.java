package com.example.blogging_api.service;

import com.example.blogging_api.model.Category;
import com.example.blogging_api.model.Post;
import com.example.blogging_api.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post testPost;

    @BeforeEach
    void setUp() {
        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("Test Content");
        testPost.setCategory(Category.TECHNOLOGY);
    }

    @Test
    void getAllPosts_ShouldReturnAllPosts() {
        List<Post> posts = Arrays.asList(testPost);
        Page<Post> page = new PageImpl<>(posts);

        when(postRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Post> actualPage = postService.getAllPosts(null, 0, 10, "createdAt", "desc");

        assertNotNull(actualPage);
        assertEquals(1, actualPage.getContent().size());
        assertEquals("Test Post", actualPage.getContent().get(0).getTitle());
        verify(postRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getPostById_ShouldReturnPost_WhenExists() {
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        Post found = postService.getPostById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Test Post", found.getTitle());
    }

    @Test
    void getPostById_ShouldThrowException_WhenNotFound() {
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> postService.getPostById(999L));
    }

    @Test
    void createPost_ShouldSaveAndReturnPost() {
        Post newPost = new Post();
        newPost.setTitle("New Post");
        newPost.setContent("New Content");
        newPost.setCategory(Category.SCIENCE);

        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post saved = invocation.getArgument(0);
            saved.setId(2L);
            return saved;
        });

        Post created = postService.createPost(newPost);

        assertNotNull(created);
        assertEquals(2L, created.getId());
        assertEquals("New Post", created.getTitle());
        assertNotNull(created.getCreatedAt());
        assertNotNull(created.getUpdatedAt());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void updatePost_ShouldUpdateAndReturnPost() {
        Post updatedPost = new Post();
        updatedPost.setTitle("Updated Title");
        updatedPost.setContent("Updated Content");
        updatedPost.setCategory(Category.EDUCATION);

        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post result = postService.updatePost(1L, updatedPost);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Content", result.getContent());
        assertEquals(Category.EDUCATION, result.getCategory());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    void deletePost_ShouldDelete_WhenExists() {
        when(postRepository.existsById(1L)).thenReturn(true);

        postService.deletePost(1L);

        verify(postRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePost_ShouldThrowException_WhenNotFound() {

        when(postRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> postService.deletePost(999L));
    }

    @Test
    void getAllPosts_WithSearchTerm_ShouldReturnFilteredPosts() {

        String searchTerm = "Test";
        List<Post> posts = Arrays.asList(testPost);
        Page<Post> page = new PageImpl<>(posts);

        when(postRepository.searchByTerm(eq(searchTerm), any(Pageable.class))).thenReturn(page);

        Page<Post> actualPage = postService.getAllPosts(searchTerm, 0, 10, "createdAt", "desc");

        assertNotNull(actualPage);
        assertEquals(1, actualPage.getContent().size());
        verify(postRepository, times(1)).searchByTerm(eq(searchTerm), any(Pageable.class));
    }
}