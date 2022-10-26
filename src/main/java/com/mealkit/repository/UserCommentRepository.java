package com.mealkit.repository;

import com.mealkit.domain.post.user.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentRepository extends JpaRepository<UserComment, Long> {
}
