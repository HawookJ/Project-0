package com.mealkit.repository;

import com.mealkit.domain.post.user.UserPostComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentRepository extends JpaRepository<UserPostComment, Long> {
}
