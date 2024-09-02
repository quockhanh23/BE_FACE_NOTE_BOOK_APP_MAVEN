package com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.component;

import com.example.BE_FACE_NOTE_BOOK_APP_MAVEN.exeption.RequestLimitException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class RequestLimitingInterceptor implements HandlerInterceptor {
    private Map<String, Integer> requestCountMap = new HashMap<>();
    private long lastRequestTime;
    private long firstRequestTime;

    //    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String uri = request.getRequestURI();
//        String method = request.getMethod();
//
//        int maxRequestLimit = 10;
//        if (("PUT".equals(method) || "POST".equals(method)) && (uri.contains("PUT") || uri.contains("POST"))) {
//        }
//        if (requestCountMap.containsKey(uri)) {
//
//            long currentTime = System.currentTimeMillis();
//            int count = requestCountMap.get(uri);
//            if (count >= maxRequestLimit) {
//                // Chặn request và trả về lỗi hoặc thông báo khác tùy ý
//                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Request limit exceeded");
//                return false;
//            } else {
//                requestCountMap.put(uri, count + 1);
//            }
//        } else {
//            setLastRequestTime(System.currentTimeMillis());
//            requestCountMap.put(uri, 1);
//        }
//        return true;
//    }

    // Đếm số lượng request
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        int maxRequestLimit = 10000;

        // Kiểm tra xem requestCountMap có chứa thông tin về URI không
        long currentTime = System.currentTimeMillis();
        if (requestCountMap.containsKey(uri)) {
            // Nếu thời gian giữa 2 request lớn hơn 1 ngày (86400000 milliseconds = 24 giờ)
            if (currentTime - firstRequestTime > 86400000) {
                // Reset lại số lần request và thời gian gọi request cuối cùng
                lastRequestTime = currentTime;
                requestCountMap.put(uri, 1);
            } else {
                int count = requestCountMap.getOrDefault(uri, 0);
                if (count >= maxRequestLimit) {
                    // Chặn request và trả về lỗi hoặc thông báo khác tùy ý
                    throw new RequestLimitException("Request limit exceeded");
                } else {
                    // Tăng số lần request và cập nhật thời gian gọi request cuối cùng
                    requestCountMap.put(uri, count + 1);
                    lastRequestTime = currentTime;
                }
            }
        } else {
            // Nếu không có thông tin về URI trong requestCountMap, thêm vào và đặt thời gian gọi request
            firstRequestTime = currentTime;
            lastRequestTime = currentTime;
            requestCountMap.put(uri, 1);
        }
        return true;
    }
}
