package com.zimug.courses.security.basic.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("rbacService")
public class MyRBACService  {

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
           UserDetails userDetails =  (UserDetails)principal;

           //构造一个授权访问资格
           SimpleGrantedAuthority simpleGrantedAuthority =
                   new SimpleGrantedAuthority(request.getRequestURI());

           return userDetails.getAuthorities().contains(simpleGrantedAuthority);
        }
        return false;
    }
}
