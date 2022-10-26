package com.mealkit.repository;

import com.mealkit.domain.post.user.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
}
