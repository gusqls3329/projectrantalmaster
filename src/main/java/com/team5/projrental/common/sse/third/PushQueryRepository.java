package com.team5.projrental.common.sse.third;


import com.team5.projrental.entities.Push;

import java.util.List;

public interface PushQueryRepository {
    long deleteUseIn(List<Push> findPush);
}
