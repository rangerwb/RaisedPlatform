package com.atguigu.crowd.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wangbo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortalTypeVO {
    private Integer id;
    private String name;
    private String remark;

    private List<PortalProjectVO> portalProjectVOList;
}
