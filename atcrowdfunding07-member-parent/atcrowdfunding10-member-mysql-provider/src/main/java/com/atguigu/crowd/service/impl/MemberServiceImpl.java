package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.PO.MemberPO;
import com.atguigu.crowd.PO.MemberPOExample;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangbo
 */
// 在类上使用@Transactional(readOnly = true)针对查询操作设置事务属性
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Override
    public MemberPO getMemberPOByLoginAcctRemote(String loginAcct) {

        MemberPOExample example = new MemberPOExample();
        MemberPOExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(loginAcct);
        List<MemberPO> memberPOList = memberPOMapper.selectByExample(example);

        if (memberPOList == null || memberPOList.size() == 0){
            return null;
        }

        return memberPOList.get(0);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
