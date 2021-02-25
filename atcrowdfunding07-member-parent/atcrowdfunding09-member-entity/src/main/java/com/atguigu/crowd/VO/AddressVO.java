package com.atguigu.crowd.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wangbo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String receiveName;
    private String phoneNum;
    private String address;
    private Integer memberId;
}
