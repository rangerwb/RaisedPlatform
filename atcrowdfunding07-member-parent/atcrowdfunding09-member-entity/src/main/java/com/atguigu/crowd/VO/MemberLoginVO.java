package com.atguigu.crowd.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangbo
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginVO implements Serializable {
    private static final long serialVersionUID = 8840008058500822018L;
    private Integer id;
    private String username;
    private String email;
}
