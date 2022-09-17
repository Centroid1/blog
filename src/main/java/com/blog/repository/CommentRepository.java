package com.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.blog.domain.*;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}