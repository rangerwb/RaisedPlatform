package com.atguigu.crowd.mapper;

import java.util.List;
import com.atguigu.crowd.PO.MemberLaunchInfoPO;
import com.atguigu.crowd.PO.MemberLaunchInfoPOExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface MemberLaunchInfoPOMapper {
    int countByExample(MemberLaunchInfoPOExample example);

    int deleteByExample(MemberLaunchInfoPOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MemberLaunchInfoPO record);

    int insertSelective(MemberLaunchInfoPO record);

    List<MemberLaunchInfoPO> selectByExample(MemberLaunchInfoPOExample example);

    MemberLaunchInfoPO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MemberLaunchInfoPO record, @Param("example") MemberLaunchInfoPOExample example);

    int updateByExample(@Param("record") MemberLaunchInfoPO record, @Param("example") MemberLaunchInfoPOExample example);

    int updateByPrimaryKeySelective(MemberLaunchInfoPO record);

    int updateByPrimaryKey(MemberLaunchInfoPO record);
}