package com.zimug.courses.security.basic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zimug.commons.exception.AjaxResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Value("${spring.security.logintype}")
    private String loginType;

    private static ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String errorMsg = "用户名或者密码输入错误！";
        if (exception instanceof SessionAuthenticationException) {
            errorMsg = exception.getMessage();
        }


        if (loginType.equalsIgnoreCase("JSON")) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(
                    AjaxResponse.userInputError(errorMsg)));
        } else {
            response.setContentType("text/html;charset=UTF-8");
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
