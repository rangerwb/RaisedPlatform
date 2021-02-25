package com.atguigu.crowd.service.api;

import com.atguigu.crowd.VO.DetailProjectVO;
import com.atguigu.crowd.VO.PortalTypeVO;
import com.atguigu.crowd.VO.ProjectVO;

import java.util.List;

/**
 * @author wangbo
 */
public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);
    List<PortalTypeVO> getPortalTypeVO();
    DetailProjectVO getDetailProjectVO(Integer projectId);
}
