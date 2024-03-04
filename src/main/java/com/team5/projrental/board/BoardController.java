package com.team5.projrental.board;


import com.team5.projrental.board.model.*;
import com.team5.projrental.common.model.ResVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService service;



    @Operation(summary = "게시글 등록", description = "게시판에 게시글 등록<br><br> 결과값 - result: 등록된 게시글의 pk값(1이상 값 나오면 등록성공)")
    @Parameters(value = {
            @Parameter(name = "title", description = "제목"),
            @Parameter(name = "contents", description = "내용")})
    @Validated
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResVo postBoard(@RequestPart(required = false) List<MultipartFile> storedPic, @RequestPart @Validated BoardInsDto dto) {
        dto.setStoredPic(storedPic);
        return service.postBoard(dto);

    }

    @Operation(summary = "전체 게시글 목록", description = "결과값 :" +
            "[{" +
            "게시판 목록<br>nick:닉네임" +
            "<br>isLikes: [0]좋아요 안함, [1]좋아요 누름" +
            "<br>iboard: 게시글 pk" +
            "<br>title: 게시글 제목" +
            "<br>view: 게시글 조회수" +
            "<br>createdAt: 등록 날짜" +
            "}]")
    @Parameters(value = {
            @Parameter(name = "page", description = "페이지, min:1 / 게시글 12개씩 나옴"),
            @Parameter(name = "search", description = "search(검색어)가 제공될 경우 해당 키워드가 포함된 게시글만 조회<br>" +
                    "search(검색어) 와 type(제목, 제목+내용, 닉네임) 은 항상 동시에 값이 있거나 동시에 값이 없어야함."),
            @Parameter(name = "type", description = "type 은 search 하는 조건임<br>" +
                    "type 에는 title:1, title+contents:2, nick:3 (제목, 제목+내용, 닉네임)의 3종류가 있음 (숫자로 받고, 백엔드에서 해석)<br>" +
                    "type=1 이면 1에 해당하는 종류중 search 에 해당하는 키워드가 포함되어 있는 게시물을 페이징해서 넘겨주는 개념임."),
            @Parameter(name = "sort", description = "sort:0 (default) - 최신순<br>sort:1 - 좋아요순<br>sort:2 - 조회수 많은순")})
    @Validated
    @GetMapping
    public BoardListVo getBoardList(@RequestParam(defaultValue = "1") @Min(1)
                                             int page,
                                             @RequestParam(defaultValue = "0")
                                             int sort,
                                             @RequestParam(name = "search", required = false)
                                             String search,
                                             @RequestParam(name = "type", required = false)
                                             Integer type)
    {
        BoardListSelDto dto = new BoardListSelDto();

        dto.setPage(page);
        dto.setSort(sort);
        dto.setSearch(search); //값을 받아왔으면 꼭 넣어주자
        dto.setType(type);
        return service.getBoardList(dto);
    }


    @Operation(summary = "게시글 입장", description = "특정 게시글 입장")
    @Parameters(value = {
            @Parameter(name = "iboard", description = "입장 할 게시글pk")})
    @GetMapping("/{iboard}")
    public BoardSelVo getBoard(@PathVariable int iboard) {
        return service.getBoard(iboard);
    }

    @Operation(summary = "게시글 수정", description = "특정 게시글 수정<br>result:1(수정 성공)")
    @Parameters(value = {
            @Parameter(name = "iboard", description = "수정 할 게시글pk"),
            @Parameter(name = "title", description = "수정 할 게시글 제목"),
            @Parameter(name = "contents", description = "수정 할 게시글 내용"),
            @Parameter(name = "storedPic", description = "수정할 게시글 사진")})
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResVo putBoard(@RequestPart(required = false) List<MultipartFile> storedPic, @RequestPart @Validated BoardPutDto dto) {
        dto.setStoredPic(storedPic);
        return service.putBoard(dto);
    }


    @Operation(summary = "게시글 삭제", description = "내가 쓴 게시글 삭제(- ACTIVED (0) :존재함(default)\n" +
            "- DELETE (-1): 삭제됨(숨김))<br> result:1 삭제됨")
    @Parameters(value = {
            @Parameter(name = "iboard", description = "삭제 할 게시글pk")})
    @DeleteMapping("/{iboard}")
    public ResVo delUserBoard(@PathVariable long iboard) {
        return service.delBoard(iboard);
    }


    @Operation(summary = "게시글 좋아요 처리", description = "게시글 좋아요 토글<br> result:1(좋아요 누름), result:-1(좋아요 취소)")
    @Parameters(value = {
            @Parameter(name = "iboard", description = "좋아요 처리 할 게시판pk")})
    @GetMapping("/like/{iboard}")
    public ResVo toggleLike(@PathVariable long iboard) {
        return service.toggleLike(iboard);

    }


}
