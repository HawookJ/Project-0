package com.mealkit.repository;

import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.QAdminPost;
import com.mealkit.repository.querydsl.AdminPostRepositoryCustom;
import com.querydsl.core.types.dsl.DateExpression;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;

import java.util.List;
import java.util.Optional;

public interface AdminPostRepository extends JpaRepository<AdminPost, Long>,
        AdminPostRepositoryCustom,
        QuerydslPredicateExecutor<AdminPost> //기본검색기능
        , QuerydslBinderCustomizer<QAdminPost> {


    Page<AdminPost> findByHomeTitleContaining(String homeTitle, Pageable pageable);
    Page<AdminPost> findByHomeDetailsContaining(String searchKeyword, Pageable pageable);

    Page<AdminPost> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<AdminPost> findByUserAccount_NickNameContaining(String nickName, Pageable pageable);

    void deleteByHomeIdAndUserAccount_UserId(Long homeId, Long userid);
    @Override
    default void customize(QuerydslBindings bindings, QAdminPost root) { //직접 구현체 만들지 않고 인터페이스만 가지고?
        bindings.excludeUnlistedProperties(true); //listing 된것만
        bindings.including(root.homeTitle, root.homeDetails, root.homeName, root.createdAt, root.createdBy);
        bindings.bind(root.homeTitle).first(StringExpression::containsIgnoreCase); //SimpleExpression보다는 문자열이라서 StringExpression /%wildcard 사용하는 like 기능도 있다
        bindings.bind(root.hashtags.any().hashtagName).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }



}
