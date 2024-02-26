package com.team5.projrental.board.comment;


import com.team5.projrental.board.comment.model.BoardCommentDelDto;
import com.team5.projrental.board.comment.model.BoardCommentInsDto;
import com.team5.projrental.board.comment.model.BoardCommentPatchDto;
import com.team5.projrental.common.model.ResVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCommentService {
    private final BoardCommentReposity boardCommentReposity;
    private final BoardCommentMapper mapper;


    public ResVo postComment(BoardCommentInsDto dto) {
        long result = mapper.insBoardComment(dto);
        return new ResVo(result);
    }

    public ResVo patchComment(BoardCommentPatchDto dto) {
        long result = mapper.patchBoardComment(dto);
        return new ResVo(result);
    }

    public ResVo delComment(int iboardComment) {
        long result = mapper.delBoardComment(iboardComment);
        return new ResVo(result);
    }


}
