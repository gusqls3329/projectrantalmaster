package com.team5.projrental.common.sse.third;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.thrid.ClientException;
import com.team5.projrental.common.sse.enums.SseCode;
import com.team5.projrental.common.sse.enums.SseKind;
import com.team5.projrental.common.sse.model.SseMessageInfo;
import com.team5.projrental.entities.Push;
import com.team5.projrental.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.team5.projrental.entities.QPush.push;

@Slf4j
@RequiredArgsConstructor
public class PushQueryRepositoryImpl implements PushQueryRepository{
    private final JPAQueryFactory query;
    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public long deleteUseIn(List<Push> findPush) {

        long executed = query.delete(push)
                .where(push.in(findPush))
                .execute();
        // delete from push where ipush in (1, 5, 7, 8, ...)
        return executed;
    }
}
