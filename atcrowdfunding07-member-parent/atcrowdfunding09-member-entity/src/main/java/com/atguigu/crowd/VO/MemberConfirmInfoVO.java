package com.atguigu.crowd.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberConfirmInfoVO implements Serializable {
    private static final long serialVersionUID = 6069258739423899049L;
    // 易付宝账号
    private String paynum;
    // 法人身份证号
    private String cardnum;
}
