package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wangbo
 */
@Controller
public class AuthHandler {

    @Autowired
    private AuthService authService;

}
