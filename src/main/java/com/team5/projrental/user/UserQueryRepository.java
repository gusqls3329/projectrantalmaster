package com.team5.projrental.user;

import com.team5.projrental.entities.Dispute;
import com.team5.projrental.entities.User;
import com.team5.projrental.entities.VerificationInfo;
import com.team5.projrental.user.model.UserModel;

import java.util.List;

public interface UserQueryRepository {
    User findByVerificationInfo(VerificationInfo info);
    User exFindByVerificationInfo(VerificationInfo info);
}
