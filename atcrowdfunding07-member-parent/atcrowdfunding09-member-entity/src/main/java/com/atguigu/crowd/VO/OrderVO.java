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
public class OrderVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String orderNum;
    private String payOrderNum;
    private Double orderAmount;
    private Integer invoice;
    private String invoiceTitle;
    private String orderRemark;
    private String addressId;
    private OrderProjectVO orderProjectVO;
}
