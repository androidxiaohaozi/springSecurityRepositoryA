package com.zimug.courses.security.basic.test;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncorderTest {

    @Test
    public void bCryptPasswordTest(){
        PasswordEncoder passwordEncorder = new BCryptPasswordEncoder();

        String rawPassword = "123456";
        String encodedPassword = passwordEncorder.encode(rawPassword);

        System.out.println("原始密码：" + rawPassword);
        System.out.println("加密之后的hash密码：" + encodedPassword);

        System.out.println(rawPassword + "是否匹配" + encodedPassword + ":" +
                passwordEncorder.matches(rawPassword, encodedPassword));

        System.out.println("654321" + "是否匹配" + encodedPassword + ":" +
                passwordEncorder.matches("654321", encodedPassword));




    }
}
