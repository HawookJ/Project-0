package com.mealkit.repository;

import com.mealkit.config.JpaConfig;
import com.mealkit.domain.post.admin.AdminPost;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("TestResult")
@Import(JpaConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaRepositoryTest {

    private UserPostRepository userPostRepository;

    private AdminPostRepository adminPostRepository;
    private UserCommentRepository userCommentRepository;

    private AdminCommentRepository adminCommentRepository;


    public JpaRepositoryTest(
            @Autowired UserPostRepository userPostRepository,
            @Autowired UserCommentRepository userCommentRepository,
            @Autowired AdminPostRepository adminPostRepository,
            @Autowired AdminCommentRepository adminCommentRepository) {
        this.userPostRepository = userPostRepository;
        this.userCommentRepository = userCommentRepository;
        this.adminPostRepository = adminPostRepository;
        this.adminCommentRepository = adminCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void AdminPost_Select_Test() {

        List<AdminPost> adminPost = adminPostRepository.findAll();
        System.out.println("userPost : " + adminPost);
        assertThat(adminPost)
                .isNotNull()
                .hasSize(50);

    }

    @DisplayName("insert 테스트")
    @Test
    void AdminPost_Insert_Test() {

        // Given
        Long previousCount = adminPostRepository.count();
        AdminPost adminPost = AdminPost.of(
                "love", "paju", "0103",
                true, 332, 3, "here",
                "this", "mant",
                31L, "hello", "lovehose");


        // When
        AdminPost savedAdmin = adminPostRepository.save(adminPost);


        // Then
        assertThat(adminPostRepository.count()).isEqualTo(previousCount + 1);


    }

    @DisplayName("update 테스트")
    @Test
    void AdminPost_Update_Test() {

        AdminPost adminPost = adminPostRepository.findById(1L).orElseThrow();
        String updateHomeAddress = "pajuuuuu";
        adminPost.setHomeAddress(updateHomeAddress);

        AdminPost savedAdminPost = adminPostRepository.saveAndFlush(adminPost);

        adminPost = adminPostRepository.findById(1L).orElseThrow();
        System.out.println(adminPost);

        assertThat(savedAdminPost).hasFieldOrPropertyWithValue("HomeAddress", updateHomeAddress);
    }

    @DisplayName("delete 테스트")
    @Test
    void AdminPost_Delete_Test() {

        AdminPost adminPost = adminPostRepository.findById(1L).orElseThrow();

        long previousCount = adminPostRepository.count();
        long previousCommentCount= adminCommentRepository.count();
        int deletedCommentSize = adminPost.getAdminComments().size();


        adminPostRepository.delete(adminPost);

        assertThat(adminPostRepository.count()).isEqualTo(previousCount-1);
        assertThat(previousCommentCount).isEqualTo(previousCommentCount- deletedCommentSize);
    }
}