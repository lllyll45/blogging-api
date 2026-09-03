package com.example.blogging_api.repository;

import com.example.blogging_api.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p WHERE " +
            "LOWER(p.title) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(p.content) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Post> searchByTerm(@Param("term") String term, Pageable pageable);
}