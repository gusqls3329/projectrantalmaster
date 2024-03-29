package com.team5.projrental.board.comment;


import com.team5.projrental.board.comment.model.BoardCommentPatchDto;
import com.team5.projrental.board.comment.model.BoardCommentInsDto;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board/comment")
public class BoardCommentController {
    private final BoardCommentService service;

    @Operation(summary = "댓글 등록", description = "게시글에 댓글 등록")
    @Parameters(value = {
            @Parameter(name = "comment", description = "댓글 내용")})
    @Validated
    @PostMapping
    public ResVo postComment(@RequestBody @Validated BoardCommentInsDto dto) {
        return service.postComment(dto);
    }

    @Operation(summary = "댓글 수정", description = "수정할 댓글 등록")
    @Parameters(value = {
            @Parameter(name = "comment", description = "댓글 내용")})
    @Validated
    @PatchMapping
    public ResVo patchComment(@RequestBody @Validated BoardCommentPatchDto dto) {
        return service.patchComment(dto);
    }

    @Operation(summary = "댓글 삭제", description = "내가 쓴 댓글 삭제(숨김)")
    @Parameters(value = {
            @Parameter(name = "iboardComment", description = "삭제할 댓글pk")})
    @DeleteMapping("{iboardComment}")
    public ResVo delComment(@PathVariable int iboardComment) {
        return service.delComment(iboardComment);
    }
}
