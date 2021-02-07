package com.zimug.courses.security.basic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 实现自定义策略
 */
public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    // 页面跳转的处理逻辑
//    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // jackson 的JSON处理对象
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * session超时或者session被踢下线的session策略
//     * @param sessionInformationExpiredEvent sessionInformationExpiredEvent
//     * @throws IOException IOException
//     * @throws ServletException ServletException
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
            throws IOException, ServletException {
//        redirectStrategy.sendRedirect(event.getRequest(),event.getResponse(), "");

        Map<String,Object> map = new HashMap<>();

        map.put("code", 403);
        map.put("msg", "您的登录已经超时或者已经在另一台机器登录，您被迫下线。" +
                event.getSessionInformation().getLastRequest());
        String json = objectMapper.writeValueAsString(map);

        event.getResponse().setContentType("application/json;charset=utf-8");
        event.getResponse().getWriter().write(json);
    }
}
