package com.atguigu.crowd.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressPO {
    private Integer id;

    private String receiveName;

    private String phoneNum;

    private String address;

    private Integer memberId;
}