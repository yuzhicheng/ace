package com.yzc.core.fillter;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yzc on 2017/7/10.
 */
public class LogMDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        Random random = new Random();
//        int beginIndex = random.nextInt(16);
//
//        StringBuilder RequestId = new StringBuilder(UUID.randomUUID().toString().replaceAll("\\-", "").substring(beginIndex, beginIndex + 16));
//        RequestId.append("@");
//        RequestId.append(Thread.currentThread().getId());
//        MDC.put("RequestId", RequestId.toString());

        if (StringUtils.isNotBlank(request.getHeader("X-B3-TraceId"))) {
            MDC.put("RequestId", request.getHeader("X-B3-TraceId"));
        } else {
            StringBuilder url = new StringBuilder(request.getRequestURI());
            if (request.getQueryString() != null) {
                url.append("?");
                url.append(request.getQueryString());
            }
            url.append("#");
            url.append(System.currentTimeMillis());

            MDC.put("RequestId", MD5(url.toString()) + "@" + Thread.currentThread().getId());
        }
        filterChain.doFilter(request, response);
    }

    private static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuilder buf = new StringBuilder(32);
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().substring(8, 24);
            //System.out.println("MD5(" + sourceStr + ",32) = " + result);
            //System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e);
        }
        return result;
    }
}
