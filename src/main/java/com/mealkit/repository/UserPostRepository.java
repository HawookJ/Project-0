package com.mealkit.repository;

import com.mealkit.domain.post.user.QUserPost;
import com.mealkit.domain.post.user.UserPost;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserPostRepository extends JpaRepository<UserPost, Long>,
        QuerydslPredicateExecutor<UserPost> //기본검색기능
        , QuerydslBinderCustomizer<QUserPost> {

    Page<UserPost> findAllByTitleContaining(String title, Pageable pageable);
    Page<UserPost> findAllByPostContentContaining(String postContent, Pageable pageable);
    Page<UserPost>findByUserAccount_UserIdContaining(String postContent, Pageable pageable);

    Page<UserPost> findByUserAccount_NickNameContaining(String nickName, Pageable pageable);


    @Override
    default void customize(QuerydslBindings bindings, QUserPost root){ //직접 구현체 만들지 않고 인터페이스만 가지고?
        bindings.excludeUnlistedProperties(true); //listing 된것만
        bindings.including(root.title, root.postContent, root.createdAt, root.createdBy);
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); //SimpleExpression보다는 문자열이라서 StringExpression /%wildcard 사용하는 like 기능도 있다
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }

    void deleteByUserPostIdAndUserAccount_UserId                                            (Long userPostId, Long userId);
}
