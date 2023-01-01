package com.mealkit.repository.querydsl;

import com.mealkit.domain.QHashtag;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.QAdminPost;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
import java.util.List;

public class AdminPostRepositoryCustomImpl extends QuerydslRepositorySupport implements AdminPostRepositoryCustom{

    public AdminPostRepositoryCustomImpl() {
        super(AdminPost.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {
        QAdminPost article = QAdminPost.adminPost;

        return from(article)
                .distinct()
                .select(article.hashtags.any().hashtagName)
                .fetch();
    }

    @Override
    public Page<AdminPost> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QHashtag hashtag = QHashtag.hashtag;
        QAdminPost adminPost = QAdminPost.adminPost;

        JPQLQuery<AdminPost> query = from(adminPost)
                .innerJoin(adminPost.hashtags, hashtag)
                .where(hashtag.hashtagName.in(hashtagNames));
        List<AdminPost> adminPosts = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(adminPosts, pageable, query.fetchCount());
    }

}
