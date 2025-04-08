package org.zerock.stocktrading.util;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import java.time.Duration;

public class RateLimiterConfigFactory {
    private static final RateLimiterConfig config = RateLimiterConfig.custom()
            .limitForPeriod(2) // 초당 2개 요청 허용
            .limitRefreshPeriod(Duration.ofSeconds(1))
            .timeoutDuration(Duration.ofSeconds(2)) // 2초 대기 후 실패
            .build();

    public static RateLimiter create() {
        return RateLimiter.of("stock-api", config);
    }
}