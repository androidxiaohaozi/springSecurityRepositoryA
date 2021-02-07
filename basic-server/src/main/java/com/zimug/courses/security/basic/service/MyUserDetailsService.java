package com.zimug.courses.security.basic.service;

import com.zimug.courses.security.basic.mapper.MyUserDetailsServiceMapper;
import com.zimug.courses.security.basic.model.MyUserDetails;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Resource
    MyUserDetailsServiceMapper myUserDetailsServiceMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {


        //用户基础数据查询
        MyUserDetails myUserDetails = myUserDetailsServiceMapper.findByUserName(s);
        if (myUserDetails == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }

        //用户的角色列表
        List<String> roleCodes = myUserDetailsServiceMapper.findRoleByUserName(s);

        //根据角色列表加载当前用户的权限
        List<String> authorities = myUserDetailsServiceMapper.findAuthorityByRoleCodes(roleCodes);
        roleCodes = roleCodes.stream().map(rc -> "ROLE_" + rc).collect(Collectors.toList());
        authorities.addAll(roleCodes);

        myUserDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(
                String.join(",",authorities))
        );

        return myUserDetails;
    }
}
