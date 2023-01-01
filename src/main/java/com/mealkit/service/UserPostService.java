package com.mealkit.service;


import com.mealkit.domain.UserAccount;
import com.mealkit.domain.constant.SearchType;
import com.mealkit.domain.post.user.Dto.UserPostDto;
import com.mealkit.domain.post.user.Dto.UserPostWithCommentDto;
import com.mealkit.domain.post.user.UserPost;
import com.mealkit.repository.UserPostRepository;
import com.mealkit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserPostService {
    @Autowired
    UserPostRepository userPostRepository;
    @Autowired
    UserRepository userRepository;

    @Transactional
    public Page<UserPostDto> searchUserPosts(String searchValue, SearchType searchType, Pageable pageable) {
    if(searchValue == null || searchValue.isBlank()){
        return userPostRepository.findAll(pageable).map(UserPostDto::from);
    }
        return switch (searchType) {
            case TITLE -> userPostRepository.findAllByTitleContaining(searchValue, pageable).map(UserPostDto::from);
            case CONTENT ->
                    userPostRepository.findAllByPostContentContaining(searchValue, pageable).map(UserPostDto::from);
            case ID ->
                    userPostRepository.findByUserAccount_UserIdContaining(searchValue, pageable).map(UserPostDto::from);
            case NICKNAME ->
                    userPostRepository.findByUserAccount_NickNameContaining(searchValue, pageable).map(UserPostDto::from);
  /*          case HASHTAG -> userPostRepository.findByHashtagNames(
                            Arrays.stream(searchValue.split(" ")).toList(),
                            pageable
                    )
                    .map(AdminPostDto::from);*/
            case HASHTAG -> null;
        };
    }
    public void saveUserPost(UserPostDto userPostDto) {
        System.out.println("saveUserPost : " + userPostDto);
        UserAccount userAccount = userRepository.getReferenceById(userPostDto.userAccountDto().userId());
        UserPost userPost = userPostDto.toEntity(userAccount);
        userPostRepository.save(userPost);
    }
@Transactional
    public UserPostWithCommentDto getUserPostWithComments(Long userPostId){
return userPostRepository.findById(userPostId).map(UserPostWithCommentDto::from).orElseThrow(()->new EntityNotFoundException(" 게시글 없음 of userPostId " + userPostId));

    }


    public long getUserPostCount(){
        return userPostRepository.count();
    }

    public UserPostDto getUserPost(Long userPostId) {
        return userPostRepository.findById(userPostId).map(UserPostDto::from).orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - adminPostId: " + userPostId));
    }

    public void deleteUserPost(Long userPostId, Long userId) {
       // UserPost userPost= userPostRepository.getReferenceById(userPostId);
        userPostRepository.deleteByUserPostIdAndUserAccount_UserId(userPostId, userId);
        userPostRepository.flush();

    }

    public void updateUserPost(Long userPostId, UserPostDto toDto) {
    UserPost userPost= userPostRepository.getReferenceById(userPostId);
    UserAccount userAccount= userRepository.getReferenceById(toDto.userAccountDto().userId());
    if(userPost.getUserAccount().equals(userAccount)){

    }
    }
}
