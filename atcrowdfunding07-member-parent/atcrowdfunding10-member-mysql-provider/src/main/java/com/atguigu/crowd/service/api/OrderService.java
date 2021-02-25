package com.atguigu.crowd.service.api;

import com.atguigu.crowd.VO.AddressVO;
import com.atguigu.crowd.VO.OrderProjectVO;
import com.atguigu.crowd.VO.OrderVO;

import java.util.List;

/**
 * @author wangbo
 */
public interface OrderService {
    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);
}
