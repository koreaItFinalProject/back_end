package com.finalProject.Back.entity.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardLike {
    private Long id;
    private Long boardId;
    private Long userId;
}
