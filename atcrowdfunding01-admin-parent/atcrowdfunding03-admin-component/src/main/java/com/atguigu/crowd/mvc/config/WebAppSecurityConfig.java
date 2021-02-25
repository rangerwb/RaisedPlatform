package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangbo
 */
// 表示配置类
@Configuration
// 启用web环境下权限控制功能
@EnableWebSecurity
// 启动全局方法权限控制功能,并且设置prePostEnabled = true。
// 保证@PreAuthorize()、@PostAuthorize()、@PreFilter()、@PostFilter()
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    // 这里配置bean，xxxservice方法无法使用
    // @Bean
    // public BCryptPasswordEncoder getBCryptPasswordEncoder(){
    //     return new BCryptPasswordEncoder();
    // }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers(
                        "/admin/to/login/page.html",
                        "/static/**"
                ) // 无条件访问资源路径
                .permitAll() // 无条件访问

                .antMatchers("/admin/get/page.html") // 针对分页显示Admin数据设定访问控制
                // .hasRole("经理")
                // 要求具备“经理”和“user:get”权限二者之一
                .access("hasRole('经理') or hasAuthority('user:get')")

                .anyRequest()
                .authenticated()

                .and()

                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response,
                                       AccessDeniedException accessDeniedException)
                            throws IOException, ServletException {
                        request.setAttribute("exception", new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                        request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request, response);
                    }
                })

                .and()

                .csrf() // 防跨站请求伪造功能
                .disable() // 禁用

                .formLogin() //开启表单登录功能
                .loginPage("/admin/to/login/page.html") // 指定登录页面
                .loginProcessingUrl("/security/do/login.html") // 指定处理登录请求的地址
                .defaultSuccessUrl("/admin/to/main/page.html") // 指定登录成功后前往的地址
                .usernameParameter("loginAcct") // 账号的请求参数名称
                .passwordParameter("userPswd") // 密码的请求参数名称

                .and()

                .logout()                                        // 开启退出功能
                .logoutUrl("/security/do/logout.html")            // 指定处理退出请求的URL地址
                .logoutSuccessUrl("/admin/to/login/page.html")    // 退出成功后前往的地址
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        // 基于内存
		/*auth
            .inMemoryAuthentication()			// 在内存中完成账号、密码的检查
            .withUser("tom")			// 指定账号
            .password("123123")					// 指定密码
            .roles("admin")
            ;*/

        // 数据库版
        builder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}
