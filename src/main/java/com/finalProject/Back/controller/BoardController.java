package com.finalProject.Back.controller;

import com.finalProject.Back.dto.request.ReqBoardDto;
import com.finalProject.Back.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BoardController {

    @Autowired
    private BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<?> writeBoard(@RequestBody ReqBoardDto.WriteBoardDto dto) {
        System.out.println(dto);
        return ResponseEntity.ok().body(boardService.writeBoard(dto));
    }
}
