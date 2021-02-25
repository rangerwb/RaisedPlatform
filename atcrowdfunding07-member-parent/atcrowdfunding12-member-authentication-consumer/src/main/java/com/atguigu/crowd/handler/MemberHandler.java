package com.atguigu.crowd.handler;

import com.atguigu.crowd.PO.MemberPO;
import com.atguigu.crowd.VO.MemberLoginVO;
import com.atguigu.crowd.VO.MemberVO;
import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.config.ShortMessageProperties;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author wangbo
 */
@Controller
public class MemberHandler {

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired(required = false)
    private RedisRemoteService redisRemoteService;

    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {
        // 1.发送验证码到phoneNum手机
        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendShortMessage(
                shortMessageProperties.getHost(), shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(), phoneNum, shortMessageProperties.getAppCode(),
                shortMessageProperties.getSign(), shortMessageProperties.getSkin());

        // 2.判断短信发送结果
        if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            // 3.如果发送成功，则将验证码存入redis
            String code = sendMessageResultEntity.getData();
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);

            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }

        } else {
            return sendMessageResultEntity;
        }
    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {
        // 1.获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();

        // 2.拼redis中存储验证码的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // 3.从redis读取key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);

        // 4.检查查询操作是否有效
        String result = resultEntity.getResult();

        if (ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }

        String redisCode = resultEntity.getData();

        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }

        // 5.如果从redis能够查询到value则比较表单验证码和redis验证码
        String formCode = memberVO.getCode();

        if (!Objects.equals(formCode, redisCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }
        
        // 6.如果验证码一致，则从redis删除
        redisRemoteService.removeRedisKeyRemote(key);

        // 7.执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswdBeforeEncoded = memberVO.getUserpswd();
        String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncoded);
        memberVO.setUserpswd(userpswdAfterEncode);

        // 8.执行保存
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO, memberPO);
        ResultEntity<String> saveMemberResultEntity  = mySQLRemoteService.saveMember(memberPO);

        if(ResultEntity.FAILED.equals(saveMemberResultEntity)){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());
            return "member-reg";
        }
        return "redirect:http://localhost:80/auth/member/to/login/page";
    }

    @RequestMapping("/auth/member/do/login")
    public String Login(@RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String userpswd,
                        ModelMap modelMap, HttpSession httpSession){
        // 1.调用远程接口根据登录账号查询MemberPO对象
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        if(ResultEntity.FAILED.equals(resultEntity.getResult())){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-login";
        }
        MemberPO memberPO = resultEntity.getData();
        if (memberPO == null){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 2.比较密码
        String userpswdDatabase = memberPO.getUserpswd();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matchesResult = bCryptPasswordEncoder.matches(userpswd, userpswdDatabase);
        if (!matchesResult){
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 3.创建MemberLoginVO对象存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        httpSession.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
        return "redirect:http://localhost:80/auth/member/to/center/page";
    }

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "redirect:http://localhost:80/";
    }

}