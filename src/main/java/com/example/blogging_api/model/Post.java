package com.example.blogging_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Заголовок обязателен")
    @Size(max = 100, message = "Заголовок не может быть длиннее 100 символов")
    private String title;

    @NotBlank(message = "Содержание обязательно")
    @Size(max = 5000, message = "Содержание не может быть длиннее 5000 символов")
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ElementCollection
    @Size(max = 10, message = "Нельзя добавить больше 10 тегов")
    private List<String> tags;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Post() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}