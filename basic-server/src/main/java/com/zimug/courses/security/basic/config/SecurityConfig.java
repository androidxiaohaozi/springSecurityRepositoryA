package com.zimug.courses.security.basic.config;

import com.zimug.courses.security.basic.config.imagecode.CaptchaCodeFilter;
import com.zimug.courses.security.basic.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    @Resource
    private MyUserDetailsService myUserDetailsService;

    @Resource
    private MyLogoutSuccessHandler myLogoutSuccessHandler;

    @Resource
    private CaptchaCodeFilter captchaCodeFilter;

    //这个方法的作用是进行安全认证及授权规则配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        //开启httpBasic认证
//        http.httpBasic()
//                .and()
//                .authorizeRequests()
//                .anyRequest()
//                .authenticated(); //所有请求都需要登录认证才能访问。
        //对csrf功能关闭 disable()
        http.addFilterBefore(captchaCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
//                .logoutUrl("/signout")
//                .logoutSuccessUrl("/login.html")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .and().rememberMe()
//                    .rememberMeParameter("remember-me-new")
                    .rememberMeCookieName("remember-me-cookie")
                    .tokenValiditySeconds(2 * 24 * 60 * 60)
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login.html") // 一旦用户的请求没有权限就跳转到这个界面
                .loginProcessingUrl("/login") //登录表单form中action的参数，也就是处理认证请求的路径
                .usernameParameter("username") //登录表单from中用户名输入框
                .passwordParameter("password")
                //.defaultSuccessUrl("/") //登录认证成功后默认跳转的路径 在springboot中resources底下的index就是\路径
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler)


        .and()
                .authorizeRequests()
                .antMatchers("/login.html","login","/kaptcha").permitAll() //如果访问是login或者login.html则就允许访问
/*                .antMatchers("/","/biz1","biz2")
                    .hasAnyAuthority("ROLE_user", "ROLE_admin") //user角色和admin角色才能访问 biz1,biz2
//                .antMatchers("/syslog","sysuser")
//                    .hasAnyRole("admin")
                .antMatchers("/syslog").hasAnyAuthority("/syslog")
                .antMatchers("/sysuser").hasAnyAuthority("/sysuser")
        .anyRequest()
        .authenticated()//只有admin角色才能访问 syslog和sysuser资源*/
                //对于所有的请求都要用access里面的表达式的规则进行校验-hasPermission的方法如果返回true则允许访问，如果返回false不允许访问
                .anyRequest().access("@rbacService.hasPermission(request,authentication)")
        .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredSessionStrategy(new CustomExpiredSessionStrategy());
//                .invalidSessionUrl("invalidSession.html");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth/*.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("123456"))
                .roles("user")

             .and() //每个and一个用户
                .withUser("admin")
                .password(passwordEncoder().encode("123456"))
                .authorities("sys:log","sys:user")
//                .roles("admin")*/
             .userDetailsService(myUserDetailsService)
//             .and()//每个and代表一个用户
                .passwordEncoder(passwordEncoder());
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        //将项目中静态资源路径开发出来
        web.ignoring().antMatchers("/css/**","/fonts/**","/img/**","/js/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
