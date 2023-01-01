package com.mealkit.service;

import com.mealkit.domain.UserAccount;
import com.mealkit.domain.post.admin.AdminPost;
import com.mealkit.domain.post.admin.AdminPostComment;
import com.mealkit.domain.post.admin.Dto.AdminPostCommentDto;
import com.mealkit.repository.AdminPostCommentRepository;
import com.mealkit.repository.AdminPostRepository;
import com.mealkit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminPostCommentService {


    private final AdminPostCommentRepository adminPostCommentRepository;
    private final UserRepository userRepository;
    private final AdminPostRepository adminPostRepository;

    @Transactional(readOnly = true)
    public List<AdminPostCommentDto> searchAdminPostComment(Long adminPostId) {
        return adminPostCommentRepository.findById(adminPostId).stream().map(AdminPostCommentDto::from).toList();

    }

    @Transactional
    public void saveAdminPostComment(AdminPostCommentDto dto) {
        try {

            AdminPost adminPost = adminPostRepository.getReferenceById(dto.adminPostId());
            System.out.println("댓글 확인1");
            UserAccount userAccount = userRepository.getReferenceById(dto.userAccountDto().userId());
            System.out.println("댓글 확인2");
            AdminPostComment adminPostComment= dto.toEntity(adminPost, userAccount);
            System.out.println("댓글 확인3");
            if(dto.parentCommentId() !=null){
                 AdminPostComment parentComment = adminPostCommentRepository.getReferenceById(dto.parentCommentId());
                parentComment.addChildComment(adminPostComment);
                System.out.println("댓글 확인4");
             }else{
                 adminPostCommentRepository.save(adminPostComment);
             }
        }catch(EntityNotFoundException e){
            log.warn("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }

    }


    public void deleteAdminPostComment(Long adminPostCommentId, Long userId) {
        adminPostCommentRepository.deleteByAdminPostCommentIdAndUserAccount_UserId(adminPostCommentId, userId);
    }

}
