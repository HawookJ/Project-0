package com.mealkit.service;


import com.mealkit.domain.Hashtag;
import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.City;
import com.mealkit.domain.constant.SearchType;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.Dto.AdminPostDto;
import com.mealkit.domain.post.admin.Dto.AdminPostWithCommentDto;
import com.mealkit.repository.AdminPostRepository;
import com.mealkit.repository.HashtagRepository;
import com.mealkit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AdminPostService {

    private final AdminPostRepository adminPostRepository;
    private final HashtagService hashtagService;
    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public Page<AdminPostDto> searchAdminPosts(SearchType searchType,
                                               String searchKeyword,
                                               City city,
                                               Pageable pageable) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            return adminPostRepository.findAll(pageable).map(AdminPostDto::from);
        }
        return switch (searchType) {
            case TITLE -> adminPostRepository.findByHomeTitleContaining(searchKeyword, pageable).map(AdminPostDto::from);
            case CONTENT ->
                    adminPostRepository.findByHomeDetailsContaining(searchKeyword, pageable).map(AdminPostDto::from);
            case ID ->
                    adminPostRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(AdminPostDto::from);
            case NICKNAME ->
                    adminPostRepository.findByUserAccount_NickNameContaining(searchKeyword, pageable).map(AdminPostDto::from);
            case HASHTAG -> adminPostRepository.findByHashtagNames(
                            Arrays.stream(searchKeyword.split(" ")).toList(),
                            pageable
                    )
                    .map(AdminPostDto::from);
        };
    }

    @Transactional
    public AdminPostWithCommentDto getAdminPostWithComments(Long adminPostId) {

    //    log.info("adminPost 전체로 나오나 확인  : " + adminPostRepository.findAll());
        return adminPostRepository.findById(adminPostId)
                .map(AdminPostWithCommentDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - adminPostId : " + adminPostId));
    }


    public void saveAdminPost(AdminPostDto adminPostDto) {
        UserAccount userAccount = userRepository.getReferenceById(adminPostDto.userAccountDto().userId());
        Set<Hashtag> hashtags = renewHashtagsFromContent(adminPostDto.homeDetails());
        AdminPost adminPost = adminPostDto.toEntity(userAccount);
        adminPost.addHashtags(hashtags);
        adminPostRepository.save(adminPost);
    }

    public void updateAdminPost(long adminPostId, AdminPostDto adminPostDto) {
        try {

            AdminPost adminPost = adminPostRepository.getReferenceById(adminPostId);
            System.out.println(adminPostDto);
            UserAccount userAccount = userRepository.getReferenceById(adminPostDto.userAccountDto().userId());

            System.out.println("update 확인");
            if (adminPost.getUserAccount().equals(userAccount)) {
                if (adminPostDto.homeTitle() != null) {
                    adminPost.setHomeTitle(adminPostDto.homeTitle());
                }
                if (adminPost.getHomeDetails() != null) {
                    adminPost.setHomeDetails(adminPostDto.homeDetails());
                }

                Set<Long> hashtagIds = adminPost.getHashtags().stream()
                        .map(Hashtag::getId)
                        .collect(Collectors.toUnmodifiableSet());
                adminPost.clearHashtags();
                adminPostRepository.flush();
                hashtagIds.forEach(hashtagService::deleteHashtagWithoutAdminPost);

                Set<Hashtag> hashtags = renewHashtagsFromContent(adminPostDto.homeDetails());
                adminPost.addHashtags(hashtags);
            }

        }catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }

    }

    public void deleteAdminPost(long adminPostId, Long userId) {

        AdminPost adminPost = adminPostRepository.getReferenceById(adminPostId);
        Set<Long> hashtagIds = adminPost.getHashtags().stream().map(Hashtag::getId).collect(Collectors.toUnmodifiableSet());

        adminPostRepository.deleteByHomeIdAndUserAccount_UserId(adminPostId, userId);
        adminPostRepository.flush();

        hashtagIds.forEach(hashtagService::deleteHashtagWithoutAdminPost);


    }


    public long getAdminPostCount() {
        return adminPostRepository.count();
    }

    public AdminPostDto getAdminPost(Long adminPostId) {
        return adminPostRepository.findById(adminPostId).map(AdminPostDto::from).orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - adminPostId: " + adminPostId));
    }

    @Transactional(readOnly = true)
    public Page<AdminPostDto> searchAdminPostViaHashtag(String hashtagName, Pageable pageable) {
        if (hashtagName == null || hashtagName.isBlank()) {
            return Page.empty(pageable);
        }

        return adminPostRepository.findByHashtagNames(List.of(hashtagName), pageable)
                .map(AdminPostDto::from);
    }

    public List<String> getHashtags() {
        return hashtagRepository.findAllHashtagNames(); // TODO: HashtagService 로 이동을 고려해보자.
    }


    private Set<Hashtag> renewHashtagsFromContent(String content) {
        Set<String> hashtagNamesInContent = hashtagService.parseHashtagNames(content);
        Set<Hashtag> hashtags = hashtagService.findHashtagsByNames(hashtagNamesInContent);
        Set<String> existingHashtagNames = hashtags.stream()
                .map(Hashtag::getHashtagName)
                .collect(Collectors.toUnmodifiableSet());

        hashtagNamesInContent.forEach(newHashtagName -> {
            if (!existingHashtagNames.contains(newHashtagName)) {
                hashtags.add(Hashtag.of(newHashtagName));
            }
        });

        return hashtags;
    }


}
