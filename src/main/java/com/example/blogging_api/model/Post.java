package com.example.blogging_api.model;

import java.util.Date;
import java.util.List;

public class Post {

    private Long id;
    private String title;
    private String content;
    private Category category;
    private List<String> tags;
    private Date createdAt;
    private Date updatedAt;

    public Post() {};

    public Post(Long id, String title, String content, Category category,
                List<String> tags, Date createdAt, Date updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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
