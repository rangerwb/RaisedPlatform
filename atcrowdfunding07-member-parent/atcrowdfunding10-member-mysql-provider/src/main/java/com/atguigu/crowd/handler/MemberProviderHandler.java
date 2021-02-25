package com.atguigu.crowd.handler;

import com.atguigu.crowd.PO.MemberPO;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.service.api.MemberService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

/**
 * @author wangbo
 */
@RestController
public class MemberProviderHandler {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/memberpo/by/loginacct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginAcct") String loginAcct) {
        try {
            MemberPO memberPO = memberService.getMemberPOByLoginAcctRemote(loginAcct);
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){
        try {
            memberService.saveMember(memberPO);

            return ResultEntity.successWithoutData();
        } catch (Exception e) {
           if (e instanceof DuplicateKeyException){
               return ResultEntity.fail(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
           }
           return ResultEntity.fail(e.getMessage());
        }
    }

}
