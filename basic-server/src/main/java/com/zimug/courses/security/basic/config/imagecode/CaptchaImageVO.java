package com.zimug.courses.security.basic.config.imagecode;

import java.time.LocalDateTime;

public class CaptchaImageVO {

    private String code;

    private LocalDateTime expireTime;

    public CaptchaImageVO(String code, int expireAfterSeconds) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireAfterSeconds);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }
}
