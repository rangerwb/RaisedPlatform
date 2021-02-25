package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理异常类
 *
 * @author wangbo
 */
// @ControllerAdvice表示当前类是一个基于注解的异常处理类
@ControllerAdvice
public class CrowdExceptionResolver {

    /**
     * 更新Admin时，如果检测到登录账号重复抛出这个异常
     *
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(
            LoginAcctAlreadyInUseForUpdateException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        //返回
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 保存Admin时，如果检测到登录账号重复抛出这个异常
     *
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = LoginAcctAlreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(
            LoginAcctAlreadyInUseException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "admin-add";
        //返回
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 登录失败异常处理
     *
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(
            Exception exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        //返回
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 算术异常处理
     *
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(value = ArithmeticException.class)
    public ModelAndView resolveMathException(
            ArithmeticException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        //返回
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * 空指针异常处理
     *
     * @param exception
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    // @ExceptionHandler将一个具体的异常类和一个方法关联起来
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(
            NullPointerException exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String viewName = "system-error";
        return commonResolve(viewName, exception, request, response);
    }

    /**
     * @param viewName  返回的异常页面名
     * @param exception 实际捕获到的异常类型
     * @param request   当前请求对象
     * @param response  当前响应对象
     * @return ModelAndView对象
     * @throws IOException
     */
    private ModelAndView commonResolve(
            String viewName, Exception exception,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        // 1. 判断当前请求的类型
        boolean judgeResult = CrowdUtil.judgeRequestType(request);

        // 2. 如果是Ajax请求
        if (judgeResult) {
            // 1. 创建ResultEntity对象
            ResultEntity<Object> resultEntity = ResultEntity.fail(exception.getMessage());
            // 2. 创建Gson对象
            Gson gson = new Gson();
            // 3. 将ResultEntity对象转换成JSON对象
            String json = gson.toJson(resultEntity);
            // 4. 将json返回给浏览器
            response.getWriter().write(json);
            // 5. 由于上面代码通过原生的response对象返回了响应，此处不提供ModelAndView对象
            return null;
        }

        // 3. 不是Ajax请求
        ModelAndView modelAndView = new ModelAndView();
        // 3.1 将Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        // 3.2 设置对应的视图名称
        modelAndView.setViewName(viewName);
        // 3.4 返回ModelAndView对象
        return modelAndView;
    }
}
