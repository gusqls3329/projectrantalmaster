package com.team5.projrental.board.comment;


import com.team5.projrental.board.comment.model.BoardCommentDelDto;
import com.team5.projrental.board.comment.model.BoardCommentInsDto;
import com.team5.projrental.board.comment.model.BoardCommentPatchDto;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.enums.BoardStatus;
import com.team5.projrental.entities.enums.ProductStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardCommentService {
    private final BoardCommentReposity boardCommentReposity;
    private final BoardCommentMapper mapper;
    private final AuthenticationFacade authenticationFacade;

    public ResVo postComment(BoardCommentInsDto dto) {
        long loginIuser = authenticationFacade.getLoginUserPk();
        dto.setLoginIuser(loginIuser);
        long result = mapper.insBoardComment(dto);

        //dto.setCreatedAt(LocalDateTime.now()); // createdAt 현재시각

        ProductStatus status = ProductStatus.getByNum(1);
        String name = status.name();// "ACTIVATED" //enum 문자열로

        BoardStatus boardStatus = BoardStatus.getByNum(1);
        boardStatus.name();

        return new ResVo(dto.getIboardComment());
    }

    public ResVo patchComment(BoardCommentPatchDto dto) {
        long loginIuser = authenticationFacade.getLoginUserPk();
        dto.setLoginIuser(loginIuser);
        long result = mapper.patchBoardComment(dto);
        return new ResVo(result);
    }

    public ResVo delComment(int iboardComment) {
        long loginIuser = authenticationFacade.getLoginUserPk();
        long result = mapper.delBoardComment(iboardComment, loginIuser);
        return new ResVo(result);
    }
}
