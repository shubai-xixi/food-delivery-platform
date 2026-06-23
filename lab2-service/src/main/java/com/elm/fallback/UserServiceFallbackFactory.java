package com.elm.fallback;

import com.elm.common.Result;
import com.elm.dto.AddressDTO;
import com.elm.dto.RegisterDTO;
import com.elm.feign.UserServiceFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Map;

@Component
public class UserServiceFallbackFactory implements FallbackFactory<UserServiceFeign> {

    private static final Logger log = LoggerFactory.getLogger(UserServiceFallbackFactory.class);

    @Override
    public UserServiceFeign create(Throwable cause) {
        // 1. 判断降级的具体原因
        String reason;
        if (cause instanceof SocketTimeoutException) {
            reason = "响应超时";
            log.error("【服务降级】用户服务响应超时: {}", cause.getMessage());
        } else if (cause instanceof ConnectException) {
            reason = "连接失败（服务宕机或网络不通）";
            log.error("【服务降级】用户服务连接失败: {}", cause.getMessage());
        } else if (cause instanceof feign.FeignException) {
            reason = "Feign调用异常";
            log.error("【服务降级】Feign调用异常: {}", cause.getMessage());
        } else {
            reason = "未知异常";
            log.error("【服务降级】未知异常: ", cause);
        }

        // 2. 返回兜底实现
        return new UserServiceFeign() {
            @Override
            public ResponseEntity<Result<Map<String, Object>>> getUser(Long userId) {
                log.warn("[降级] getUser → {}", reason);
                Result<Map<String, Object>> result = Result.fail(500, "用户服务不可用（" + reason + "）");
                return ResponseEntity.ok(result);
            }

            @Override
            public Result<String> register(RegisterDTO dto) {
                log.warn("[降级] register → {}", reason);
                return Result.fail(500, "注册服务不可用（" + reason + "）");
            }

            @Override
            public Result<?> updateAddress(Long id, AddressDTO dto) {
                log.warn("[降级] updateAddress → {}", reason);
                return Result.fail(503, "地址服务繁忙（" + reason + "）");
            }

            @Override
            public Result<?> deleteAddress(Long id) {
                log.warn("[降级] deleteAddress → {}", reason);
                return Result.fail(429, "操作太频繁（" + reason + "）");
            }
        };
    }
}