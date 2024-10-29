package com.finalProject.Back.service;

import com.finalProject.Back.dto.request.ReqReviewDto;
import com.finalProject.Back.entity.Cafe.CafeDetail;
import com.finalProject.Back.entity.Review.Review;
import com.finalProject.Back.entity.Review.ReviewCategory;
import com.finalProject.Back.exception.AccessDeniedException;
import com.finalProject.Back.repository.ReviewCategoryMapper;
import com.finalProject.Back.repository.ReviewMapper;
import com.finalProject.Back.security.principal.PrincipalUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ReviewCategoryMapper reviewCategoryMapper;

    @Transactional(rollbackFor = SQLException.class)
    public void write(ReqReviewDto.ReqWriteDto dto) {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Review review = dto.toEntity(principalUser.getId());
        reviewMapper.save(review);
        Long reviewId = review.getId();
        System.out.println(reviewId);

        List<Long> categoryIds = dto.getCategoryIds();
        for(Long categoryId: categoryIds) {
            ReviewCategory reviewCategory = new ReviewCategory(reviewId, categoryId);
            reviewCategoryMapper.save(reviewCategory);
        }
    }

    public CafeDetail getDetail(Long cafeId) {
        return reviewMapper.findByCafeId(cafeId);
    }

    public void modify(ReqReviewDto.ReqModifyDto dto) {
        authorityCheck(dto.getReviewId());
        reviewMapper.modify(dto.toEntity());
    }

    public void delete(Long reviewId) {
        authorityCheck(reviewId);
        reviewMapper.delete(reviewId);
    }

    private void authorityCheck(Long reviewId) {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long writerId = reviewMapper.findById(reviewId);
        if(!principalUser.getId().equals(writerId)) {
            throw new AccessDeniedException();
        }
    }
}
