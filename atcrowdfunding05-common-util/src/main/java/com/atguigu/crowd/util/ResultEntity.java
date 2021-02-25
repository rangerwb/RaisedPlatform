package com.atguigu.crowd.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一整个项目中Ajax请求返回的结果(未来也可以用于分布式架构各个模块间调用时返回统一类型)
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultEntity<T> {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";

    //封装当前请求处理的结果是成功还是失败
    private String result;
    //请求处理失败时返回的错误消息
    private String message;
    //要返回的数据
    private T data;

    /**
     * 请求处理成功，且不需要返回数据时使用的工具方法
     *
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithoutData() {
        return new ResultEntity<Type>(SUCCESS, null, null);
    }

    /**
     * 请求处理成功，且需要返回数据时使用的工具方法
     *
     * @param data   需要返回的数据
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(Type data) {
        return new ResultEntity<Type>(SUCCESS, null, data);
    }

    /**
     * 请求处理失败使用的工具方法
     *
     * @param message 失败的错误消息
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> fail(String message) {
        return new ResultEntity<Type>(FAILED, message, null);
    }
}
