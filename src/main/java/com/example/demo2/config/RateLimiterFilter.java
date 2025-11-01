package com.example.demo2.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimiterConfig rateLimiterConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy IP của người gọi
        String ip = getClientIp(request);

        // Lấy bucket tương ứng
        Bucket bucket = rateLimiterConfig.resolveBucket(ip);

        // Thử tiêu hao 1 token
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // Còn token → cho phép đi tiếp
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            filterChain.doFilter(request, response);
        } else {
            // Hết token → chặn
            response.setStatus(429); // Too Many Requests
            response.getWriter().write("Quá nhiều yêu cầu! Vui lòng thử lại sau "
                    + probe.getNanosToWaitForRefill() / 1_000_000_000 + " giây.");
        }
    }

    // Lấy IP thật của client
    private String getClientIp(HttpServletRequest request) {
        String xf = request.getHeader("X-Forwarded-For");
        if (xf == null || xf.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xf.split(",")[0].trim();
    }
}