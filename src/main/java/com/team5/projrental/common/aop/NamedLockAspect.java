package com.team5.projrental.common.aop;

import com.team5.projrental.common.aop.anno.NamedLock;
import com.team5.projrental.common.exception.ErrorCode;
import com.team5.projrental.common.exception.base.WrapRuntimeException;
import com.team5.projrental.common.exception.thrid.ClientException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;

@Aspect
@Component
@Slf4j
public class NamedLockAspect {

    /* TODO 2024-02-8 목 21:45
        JPA 적용시 JPA 로 변경 (@Query)
        --by Hyunmin
    */
    private static final String GET_LOCK = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(?)";
    private final DataSource dataSource;

    public NamedLockAspect(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Transactional
    @Around("@annotation(namedLock)")
    public Object withLock(ProceedingJoinPoint joinPoint,
                           NamedLock namedLock) throws Throwable {
        try (Connection conn = dataSource.getConnection()) {
            String userLockName = namedLock.value();
            int timeoutSeconds = namedLock.timeout();
            try {
                log.debug("NamedLockAspect.executeWithLock()");
                getLock(conn, userLockName, timeoutSeconds);
                Object result = joinPoint.proceed();
                log.debug("NamedLockAspect.withLock() result: {}", result);
                return result;
            } finally {
                releaseLock(conn, userLockName);
            }
        } catch (Throwable e) {
            throw e;
        }
    }

    private void getLock(Connection conn, String userLockName, int timeoutSeconds) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(GET_LOCK)) {
            ps.setString(1, userLockName);
            ps.setInt(2, timeoutSeconds);
            checkResultSet(userLockName, ps, "GetLock_");
            log.debug("NamedLockAspect.getLock() ps: {}", ps);
        } catch (SQLTimeoutException e) {
            log.debug("NamedLockAspect.getLock() SQLTimeoutException", e);
            throw new ClientException(ErrorCode.ILLEGAL_EX_MESSAGE, "이미 처리중인 날짜입니다. 잠시 후 다시 시도해주세요.");

        }
    }

    private void releaseLock(Connection conn, String userLockName) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(RELEASE_LOCK)) {
            ps.setString(1, userLockName);
            checkResultSet(userLockName, ps, "ReleaseLock_");
        }
    }

    private void checkResultSet(String userLockName,
                                PreparedStatement ps,
                                String type) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                log.debug("NamedLockAspect.{}{}", type, userLockName);
                throw new WrapRuntimeException(ErrorCode.SERVER_ERR_MESSAGE);
            }
            int result = rs.getInt(1);
            log.debug("NamedLockAspect.checkResultSet() result: {}", result);
            if (result != 1) {
                log.debug("NamedLockAspect.{}{}", type, userLockName);
                throw new WrapRuntimeException(ErrorCode.SERVER_ERR_MESSAGE);
            }
        }
    }
}
