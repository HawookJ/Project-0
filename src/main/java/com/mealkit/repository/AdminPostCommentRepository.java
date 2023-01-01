package com.mealkit.repository;

import com.mealkit.domain.post.admin.AdminPostComment;
import com.mealkit.domain.post.admin.QAdminPostComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AdminPostCommentRepository extends JpaRepository<AdminPostComment,Long>,
        QuerydslPredicateExecutor<AdminPostComment> //기본검색기능
        ,QuerydslBinderCustomizer<QAdminPostComment>
{

    List<AdminPostComment> findByAdminPost_HomeId(Long homeId);
    void deleteByAdminPostCommentIdAndUserAccount_UserId(Long adminCommentId, Long userId);
    @Override
    default void customize(QuerydslBindings bindings, QAdminPostComment root){ //직접 구현체 만들지 않고 인터페이스만 가지고?
        bindings.excludeUnlistedProperties(true); //listing 된것만
        bindings.including(root.adminCommentContent,root.createdAt, root.createdBy);
        bindings.bind(root.adminCommentContent)
                .first(StringExpression::containsIgnoreCase); //SimpleExpression보다는 문자열이라서 StringExpression /%wildcard 사용하는 like 기능도 있다
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy)
                .first(StringExpression::containsIgnoreCase);

    }

}
