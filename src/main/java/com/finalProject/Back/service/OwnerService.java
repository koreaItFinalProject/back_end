package com.finalProject.Back.service;

import com.finalProject.Back.dto.request.ReqBoardDto;
import com.finalProject.Back.dto.response.*;
import com.finalProject.Back.dto.response.Board.RespBoardDto;
import com.finalProject.Back.dto.response.Board.RespBoardInfoDto;
import com.finalProject.Back.dto.response.Cafe.RespAdminCafeListDto;
import com.finalProject.Back.dto.response.Cafe.RespCafeListDto;
import com.finalProject.Back.dto.response.Comment.RespCommentInfoDto;
import com.finalProject.Back.dto.response.Review.RespReviewInfoDto;
import com.finalProject.Back.entity.Message;
import com.finalProject.Back.entity.Review.Review;
import com.finalProject.Back.entity.board.BoardList;
import com.finalProject.Back.repository.*;
import com.finalProject.Back.security.principal.PrincipalUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class OwnerService {

    @Autowired
    private OwnerMapper ownerMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CafeMapper cafeMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private OAuth2UserMapper OAuth2UserMapper;

    public List<RespGetUserDto> getUsers(){
        return ownerMapper.getUsers();
    }

    public List<RespGetOwnerDto> getOwners(){
        return ownerMapper.getOwners();
    }

    @Transactional(rollbackFor = SQLException.class)
    public Long deleteUser(Long id){
        OAuth2UserMapper.deleteOauth2ByUserId(id);
        commentMapper.deleteByUserId(id);
        reviewMapper.deleteByUserId(id);
        boardMapper.deleteByUserId(id);
        return ownerMapper.deleteUser(id);
    }

    public Long deleteCafe(Long id){
        return ownerMapper.deleteCafe(id);
    }

    public RespInfoDto getInfo(Long id){
        RespUserInfoDto userInfoDto = userMapper.findUserInfoById(id);
        List<RespBoardInfoDto> boardInfoDto = boardMapper.getBoardInfoById(id);
        List<RespCommentInfoDto> commentInfoDto = commentMapper.findCommentById(id);
        List<RespReviewInfoDto> reviewInfoDto = reviewMapper.getReviewInfoById(id);
        List<RespBoardCommentInfoDto> boardCommentInfoDto = commentMapper.findBoardCommentById(id);
        List<RespAdminCafeListDto> cafeListDtos = cafeMapper.getCafeList();
        List<Message> messages = messageMapper.findByUserId(id);
        return RespInfoDto.builder()
                .user(userInfoDto)
                .board(boardInfoDto)
                .review(reviewInfoDto)
                .comment(commentInfoDto)
                .boardComment(boardCommentInfoDto)
                .cafemanager(cafeListDtos)
                .alarm(messages)
                .build();
    }

    public RespBoardDto.RespBoardListDto getNoticeList(ReqBoardDto.BoardListDto dto) {
        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Long startIndex = (dto.getPage() - 1) * dto.getLimit();
        Map<String, Object> params = Map.of(
                "startIndex", startIndex,
                "limit", dto.getLimit(),
                "searchValue", dto.getSearchValue() == null ? "" : dto.getSearchValue(),
                "userId", principalUser.getId()
        );
        List<BoardList> boardLists = boardMapper.getNoticeListByOwnerId(params);
        Integer boardTotalCount = boardMapper.getTotalCountByOwnerId(principalUser.getId());
        return RespBoardDto.RespBoardListDto.builder()
                .boards(boardLists)
                .totalCount(boardTotalCount)
                .build();
    }

    @Transactional(rollbackFor = SQLException.class)
    public RespRecentDto getRecent() {
        RespRecentDto dto = RespRecentDto.builder()
                .reviewList(reviewMapper.getRecent())
                .boardList(boardMapper.getRecent())
                .commentList(commentMapper.getRecent())
                .userList(userMapper.getRecent())
                .build();

        System.out.println(boardMapper.getRecent());
        System.out.println(dto);
        return RespRecentDto.builder()
                .reviewList(reviewMapper.getRecent())
                .boardList(boardMapper.getRecent())
                .commentList(commentMapper.getRecent())
                .userList(userMapper.getRecent())
                .build();
    }

}
