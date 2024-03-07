package com.team5.projrental.board;


import com.team5.projrental.board.comment.BoardCommentMapper;
import com.team5.projrental.board.model.*;
import com.team5.projrental.common.Const;
import com.team5.projrental.common.exception.BadWordException;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.NoSuchUserException;
import com.team5.projrental.common.exception.base.BadInformationException;
import com.team5.projrental.common.exception.base.NoSuchDataException;
import com.team5.projrental.common.exception.checked.FileNotContainsDotException;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.model.ResVo;
import com.team5.projrental.common.security.AuthenticationFacade;
import com.team5.projrental.common.utils.CommonUtils;
import com.team5.projrental.common.utils.MyFileUtils;
import com.team5.projrental.entities.Board;
import com.team5.projrental.entities.BoardPic;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.enums.BoardStatus;
import com.team5.projrental.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.team5.projrental.common.exception.ErrorCode.*;
import static com.team5.projrental.common.exception.ErrorMessage.ILLEGAL_RANGE_EX_MESSAGE;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final AuthenticationFacade authenticationFacade;
    private final MyFileUtils myFileUtils;
    private final UserRepository userRepository;
    private final BoardMapper mapper;
    private final BoardCommentMapper commentMapper;

    @Transactional
    public ResVo postBoard(BoardInsDto dto) {
        CommonUtils.ifContainsBadWordThrow(BadWordException.class, BAD_WORD_EX_MESSAGE,
                dto.getTitle(), dto.getContents()); //욕설 필터링
        /*int result = mapper.insBoard(dto);
        BoardPicInsDto pDto = new BoardPicInsDto();
        if (dto.getStoredPic() != null && !dto.getStoredPic().isEmpty()) {
            try {
                for (MultipartFile file : dto.getStoredPic()) {
                    String picName = myFileUtils.savePic(file, "board", String.valueOf(dto.getIboard()));
                    List<String> dd = new ArrayList();
                    dd.add(picName);
                    pDto.setStoredPic(dd);
                }
            } catch (FileNotContainsDotException e) {
                throw new ClientException(BAD_PIC_EX_MESSAGE);
            }
            int result2 = mapper.insBoardPics(pDto);
        }
        return new ResVo((long)dto.getIboard());*/
        long zero = 0;
        User user = userRepository.getReferenceById(authenticationFacade.getLoginUserPk());
        Board board = Board.builder()
                .user(user)
                .title(dto.getTitle())
                .contents(dto.getContents())
                .view(zero)
                .status(BoardStatus.ACTIVATED)
                .build();
        boardRepository.save(board);
        /*board.setUser(user);
        board.setTitle(dto.getTitle());
        board.setContents(dto.getContents());*/

        String stringId= String.valueOf(board.getId());
        BoardPicInsDto boardPicInsDto = new BoardPicInsDto();
        boardPicInsDto.setIboard(board.getId().intValue());

        if(dto.getStoredPic() != null && !dto.getStoredPic().isEmpty()) {
            try {
                board.setBoardPicList(myFileUtils.savePic(dto.getStoredPic(), "board", stringId)
                        .stream()
                        .map(picName -> BoardPic.builder()
                                .board(board)
                                .storedPic(picName)
                                .build()).collect(Collectors.toList()));
            } catch (FileNotContainsDotException e) {
                throw new ClientException(BAD_PIC_EX_MESSAGE);
            }
        }
        return new ResVo((long)boardPicInsDto.getIboard());
    }

    public BoardListVo getBoardList (BoardListSelDto dto){ //전체 게시글
        long loginIuser = authenticationFacade.getLoginUserPk();    // 좋아요 많이 받은순을 좋아요리스트로 해버림 수정 필요
        dto.setLoginIuser(loginIuser);

        BoardStatus boardStatus = BoardStatus.ACTIVATED;
        String status = boardStatus.name(); //enum을 문자열로
        dto.setStatus(status);

        BoardAllCount count = mapper.selBoardCount(status);
        List<BoardListSelVo> list = mapper.selBoardList(dto);

        BoardListVo vo = BoardListVo.builder()
                .boardList(list)
                .totalBoardCount(count.getTotalBoardCount())
                .build();
        LocalDateTime createdAt = LocalDateTime.now();
        System.out.println(createdAt);
        //hi
        return vo;
    }

    public BoardSelVo getBoard (int iboard){
        BoardStatus boardStatus = BoardStatus.ACTIVATED;
        String status = boardStatus.name(); //enum을 문자열로

        mapper.viewCount(iboard);
        BoardSelVo vo = mapper.selBoard(iboard, status);

        vo.setPic(mapper.selBoardPicList(iboard));
        List<BoardPicSelVo> picList = mapper.selBoardPicList(iboard);
        vo.setPic(picList);

        List<BoardCommentSelVo> boardCommentList = commentMapper.selCommentList(iboard);
        vo.setComments(boardCommentList);

        return vo;
    }

    @Transactional
    public ResVo putBoard (BoardPutDto dto) { //gg
        Board board = boardRepository.getReferenceById((long)dto.getIboard());

        if(dto.getIpics() != null && dto.getIpics().get(0) != 0) {
            List<Integer> ipics = dto.getIpics();
            for(int intIpics : ipics ) {
                int affectedRow = mapper.delBoardPics(dto.getIboard(), intIpics);
                if(affectedRow == 0) {
                    throw new ClientException(
                            ErrorCode.ILLEGAL_EX_MESSAGE,
                            "존재하지 않는 사진pk값 입니다.");
                }
            }
        }
        if(dto.getTitle() != null && dto.getTitle() != "") {
            board.setTitle(dto.getTitle());
        }
        if(dto.getContents() != null && dto.getContents() != "") {
            board.setContents(dto.getContents());
        }

        String stringId= String.valueOf(board.getId());
        BoardPicInsDto boardPicInsDto = new BoardPicInsDto();
        boardPicInsDto.setIboard(board.getId().intValue());
        if(dto.getStoredPic() != null && !dto.getStoredPic().isEmpty()) {
            try {
                board.setBoardPicList(myFileUtils.savePic(dto.getStoredPic(), "board", stringId)
                        .stream()
                        .map(picName -> BoardPic.builder()
                                .board(board)
                                .storedPic(picName)
                                .build()).collect(Collectors.toList()));
            } catch (FileNotContainsDotException e) {
                throw new ClientException(BAD_PIC_EX_MESSAGE);
            }
        }
        return new ResVo(Const.SUCCESS);
    }


    @Transactional
    public ResVo delBoard (long iboard){
        // jpa 게시글 삭제
        /*Board board = boardRepository.getReferenceById(iboard);
        User user = userRepository.getReferenceById(authenticationFacade.getLoginUserPk());
        board.setUser(user);
        board.setStatus(BoardStatus.DELETED);*/

        //board.setStatus(BoardStatus.DELETED);
            /*long result = mapper.delBoard(iboard);
            return new ResVo(result);*/

        BoardStatus boardStatus = BoardStatus.DELETED;
        String deleteStatus = boardStatus.name(); //enum을 문자열로

        BoardStatus boardStatus2 = BoardStatus.ACTIVATED;
        String activatedStatus = boardStatus2.name();

        long loginIuser = authenticationFacade.getLoginUserPk();
        int affecedRow = mapper.delBoard(iboard, loginIuser, deleteStatus, activatedStatus);
        if(affecedRow == 1) {
            return new ResVo(Const.SUCCESS);
        }
        throw new ClientException(
                ErrorCode.ILLEGAL_EX_MESSAGE,
                "잘못된 정보 입니다.");
    }

    public ResVo toggleLike (long iboard) {
        /*User user = userRepository.getReferenceById(authenticationFacade.getLoginUserPk());
        return null;*/
        long loginIuser = authenticationFacade.getLoginUserPk();
        BoardToggleLikeDto dto = new BoardToggleLikeDto();
        dto.setIboard(iboard);
        dto.setLoginIuser(loginIuser);

        int affectedRow = mapper.delLike(dto);
        if (affectedRow == 0) {
            mapper.insLike(dto);
            return new ResVo(Const.SUCCESS);
        }
        if (affectedRow == 1) {
            long result = -1;
            return new ResVo(result);
        }
        throw new ClientException(
                ErrorCode.ILLEGAL_EX_MESSAGE);
    }
}

