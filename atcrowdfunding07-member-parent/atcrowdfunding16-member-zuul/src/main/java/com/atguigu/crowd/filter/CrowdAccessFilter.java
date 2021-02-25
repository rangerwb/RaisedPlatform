package com.atguigu.crowd.filter;

import com.atguigu.crowd.constant.AccessPassResources;
import com.atguigu.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author wangbo
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {

    @Override
    public String filterType() {
        // 这里返回"pre"意思是在目标微服务前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 2.通过RequestContext对象获取当前请求对象(框架底层是借助ThreadLocal从当前线程上获取事先绑定的request对象)
        HttpServletRequest request = requestContext.getRequest();

        // 3.获取servletPath值
        String servletPath = request.getServletPath();

        // 4.根据servletPath判断当前请求是否对应可以直接放行的特定功能
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);
        if (containsResult){
            return false;
        }

        // 5.判断是否为静态资源
        boolean judgeStaticResult = AccessPassResources.judgeCurrentServletPathWhetherStaticResource(servletPath);
        if (judgeStaticResult){
            return false;
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {
        // 1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        // 2.获取当前session对象
        HttpSession session = request.getSession();

        // 3.尝试从Session对象中获取已登录的用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

        // 4.判断loginMember是否为空
        if(loginMember == null){
            // 5.从requestContext对象中获取Response对象
            HttpServletResponse response = requestContext.getResponse();

            // 6.将提示消息存入Session域
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDEN);

            // 7.重定向到auth-consumer工程中的登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
