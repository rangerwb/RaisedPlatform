package com.atguigu.crowd.service.api;

import com.atguigu.crowd.PO.MemberPO;

/**
 * @author wangbo
 */
public interface MemberService {

    MemberPO getMemberPOByLoginAcctRemote(String loginAcct);

    void saveMember(MemberPO memberPO);
}
