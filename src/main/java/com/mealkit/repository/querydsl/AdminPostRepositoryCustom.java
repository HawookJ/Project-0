package com.mealkit.repository.querydsl;

import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.Dto.AdminPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface AdminPostRepositoryCustom {
    @Deprecated
    List<String> findAllDistinctHashtags();
    Page<AdminPost> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable);
}
