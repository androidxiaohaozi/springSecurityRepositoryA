package com.zimug.courses.security.basic.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.zimug.courses.security.basic.config.imagecode.CaptchaImageVO;
import com.zimug.courses.security.basic.service.utils.MyContants;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
public class CaptchaController {

    @Resource
    DefaultKaptcha captchaProducer;


    @RequestMapping(value = "/kaptcha",method = RequestMethod.GET)
    public void kaptcha(HttpSession session, HttpServletResponse httpServletResponse) throws IOException {

        httpServletResponse.setDateHeader("Expires",0);
        httpServletResponse.setHeader("Cache_Control", "no-store, no-cache, must-revalidate");
        httpServletResponse.addHeader("Cache_Control", "post-check=0, pre-check=0");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setContentType("image/ipeg");


        String catText = captchaProducer.createText();

        session.setAttribute(MyContants.CAPTCHA_SESSION_KEY,
                new CaptchaImageVO(catText, 2 * 60));

        try(ServletOutputStream out = httpServletResponse.getOutputStream()){
            BufferedImage image = captchaProducer.createImage(catText);
            ImageIO.write(image, "jpg",out);
            out.flush();

        }

    }
}
