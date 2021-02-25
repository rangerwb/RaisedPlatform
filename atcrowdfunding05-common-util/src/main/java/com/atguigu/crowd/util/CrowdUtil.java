package com.atguigu.crowd.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import com.atguigu.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 通用工具方法类
 *
 * @author wangbo
 */
public class CrowdUtil {

    /**
     * 判断当前请求是否为Ajax请求
     *
     * @param request 请求对象
     * @return 该请求是否为Ajax
     */
    public static boolean judgeRequestType(HttpServletRequest request) {

        // 1.获取请求消息头
        String accpetHeadler = request.getHeader("Accpet");
        String xRequestHandler = request.getHeader("X-Requested-With");

        // 2.判断
        return (accpetHeadler != null && accpetHeadler.contains("application/json") ||
                xRequestHandler != null && xRequestHandler.contains("XMLHttpRequest"));
    }

    /**
     * 对明文字符串进行md5加密
     *
     * @param source 传入的明文字符串
     * @return 加密的结果
     */
    public static String md5(String source) {
        // 1.判断source是否有效
        if (source == null || source.length() == 0) {
            // 2.如果不是有效的字符串抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_LOGIN_STRING_INVALID);
        }
        try {
            // 3.获取MessageDigest对象
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            // 4.获取明文字符串对应的字节数组
            byte[] input = source.getBytes();

            // 5.执行加密
            byte[] output = messageDigest.digest(input);

            // 6.创建BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);

            // 7.按照16进制将bigInteger的值转换为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix).toUpperCase();

            return encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 给远程第三方短信接口发送请求把验证码发送到用户手机上
     *
     * @param host      短信接口调用的URL地址
     * @param path      具体发送短信功能的地址
     * @param method    请求方式
     * @param phoneNum  手机号
     * @param appcode   调用第三方API的AppCode
     * @param sign      签名编号
     * @param skin      模板编号
     * @return
     */
    public static ResultEntity<String> sendShortMessage(
            String host, String path, String method,
            String phoneNum, String appcode, String sign, String skin
    ) {
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);

        // 生成验证码
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int random = (int) (Math.random() * 10);
            builder.append(random);
        }

        String code = builder.toString();
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("param", code);
        querys.put("phone", phoneNum);
        querys.put("sign", sign);
        querys.put("skin", skin);

        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            //System.out.println(response.toString());如不输出json, 请打开这行代码，打印调试头部状态码。
            //状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
            //获取response的body
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            String reasonPhrase = statusLine.getReasonPhrase();

            if (statusCode == 200) {
                return ResultEntity.successWithData(code);
            }

            return ResultEntity.fail(reasonPhrase);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    /**
     * 专门负责上传文件到 OSS 服务器的工具方法
     *
     * @param endpoint        OSS 参数
     * @param accessKeyId     OSS 参数
     * @param accessKeySecret OSS 参数
     * @param inputStream     要上传的文件的输入流
     * @param bucketName      OSS 参数
     * @param bucketDomain    OSS 参数
     * @param originalName    要上传的文件的原始文件名
     * @return 包含上传结果以及上传的文件在 OSS 上的访问路径
     */
    public static ResultEntity<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName) {
        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成上传文件在 OSS 服务器上保存时的文件名
        // 原始文件名：beautfulgirl.jpg
        // 生成文件名：wer234234efwer235346457dfswet346235.jpg
        // 使用 UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目录、文件主体名称、文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;
        try {
            // 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName, inputStream);
            // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
            // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {
                // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
                // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
                // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功，获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
                // 当前方法返回失败
                return ResultEntity.fail(" 当 前 响 应 状 态 码 =" + statusCode + " 错 误 消 息 =" + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 当前方法返回失败
            return ResultEntity.fail(e.getMessage());
        } finally {
            if (ossClient != null) {
                // 关闭 OSSClient。
                ossClient.shutdown();
            }
        }
    }

}
