package dev.challduck.portfolio.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Base64;

@Slf4j
public class CookieUtil {

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name){
        Cookie[] cookies = request.getCookies();
        if(cookies == null) return;

        for (Cookie cookie: cookies) {
            if(name.equals(cookie.getName())){
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String serializeToBase64(Object obj) {
        try {
            return Base64.getEncoder().encodeToString(objectMapper.writeValueAsBytes(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // 예외 처리를 적절히 수행하십시오.
            return null;
        }
    }

    public static <T> T deserializeFromBase64(Cookie cookie, Class<T> valueType) {
        try {
            byte[] bytes = Base64.getDecoder().decode(String.valueOf(cookie));
            log.info("cookie : ", bytes);
            return objectMapper.readValue(bytes, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            // 예외 처리를 적절히 수행하십시오.
            return null;
        }
    }
}
