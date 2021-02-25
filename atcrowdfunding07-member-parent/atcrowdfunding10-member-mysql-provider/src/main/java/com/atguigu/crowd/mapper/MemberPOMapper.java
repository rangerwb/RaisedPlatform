package com.atguigu.crowd.mapper;

import java.util.List;

import com.atguigu.crowd.PO.MemberPO;
import com.atguigu.crowd.PO.MemberPOExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MemberPOMapper {
    int countByExample(MemberPOExample example);

    int deleteByExample(MemberPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MemberPO record);

    int insertSelective(MemberPO record);

    List<MemberPO> selectByExample(MemberPOExample example);

    MemberPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MemberPO record, @Param("example") MemberPOExample example);

    int updateByExample(@Param("record") MemberPO record, @Param("example") MemberPOExample example);

    int updateByPrimaryKeySelective(MemberPO record);

    int updateByPrimaryKey(MemberPO record);
}