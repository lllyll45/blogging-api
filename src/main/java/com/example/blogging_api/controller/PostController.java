package com.example.blogging_api.controller;

import com.example.blogging_api.model.Post;
import com.example.blogging_api.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@Tag(name = "Посты", description = "Управление блог-постами")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Получить все посты", description = "Возвращает список постов с пагинацией и возможностью поиска")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список постов"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса")
    })
    @GetMapping
    public Page<Post> getPosts(
            @Parameter(description = "Поисковый термин (ищет в title, content, category)")
            @RequestParam(required = false) String term,

            @Parameter(description = "Номер страницы (начиная с 0)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Количество постов на странице")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Поле для сортировки (createdAt, title, id)")
            @RequestParam(defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Направление сортировки (asc, desc)")
            @RequestParam(defaultValue = "desc") String sortDir) {

        return postService.getAllPosts(term, page, size, sortBy, sortDir);
    }

    @Operation(summary = "Получить пост по ID", description = "Возвращает один пост по его уникальному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пост найден"),
            @ApiResponse(responseCode = "404", description = "Пост не найден")
    })
    @GetMapping("/{id}")
    public Post getPostById(
            @Parameter(description = "ID поста")
            @PathVariable Long id) {
        return postService.getPostById(id);
    }

    @Operation(summary = "Создать новый пост", description = "Создаёт новый пост с переданными данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пост успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные (пустой title или content)")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(
            @Parameter(description = "Данные для создания поста")
            @Valid @RequestBody Post newPost) {
        return postService.createPost(newPost);
    }

    @Operation(summary = "Обновить пост", description = "Обновляет существующий пост по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пост обновлён"),
            @ApiResponse(responseCode = "404", description = "Пост не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные")
    })
    @PutMapping("/{id}")
    public Post updatePost(
            @Parameter(description = "ID поста для обновления")
            @PathVariable Long id,

            @Parameter(description = "Обновлённые данные")
            @Valid @RequestBody Post updatedPost) {
        return postService.updatePost(id, updatedPost);
    }

    @Operation(summary = "Удалить пост", description = "Удаляет существующий пост по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Пост удалён"),
            @ApiResponse(responseCode = "404", description = "Пост не найден")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(
            @Parameter(description = "ID поста для удаления")
            @PathVariable Long id) {
        postService.deletePost(id);
    }
}