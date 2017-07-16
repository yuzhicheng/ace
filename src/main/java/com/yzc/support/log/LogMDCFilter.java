package com.yzc.support.log;

import org.apache.log4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * @author hackingwu.
 * @since 2015/8/20
 */
public class LogMDCFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Random random = new Random();
        int beginIndex = random.nextInt(16);

        StringBuilder RequestId = new StringBuilder(UUID.randomUUID().toString().replaceAll("\\-", "").substring(beginIndex, beginIndex + 16));
        RequestId.append("@");
        RequestId.append(Thread.currentThread().getId());
        MDC.put("RequestId", RequestId.toString());
        filterChain.doFilter(request, response);
    }
}
