package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.PO.AddressPO;
import com.atguigu.crowd.PO.AddressPOExample;
import com.atguigu.crowd.PO.OrderPO;
import com.atguigu.crowd.PO.OrderProjectPO;
import com.atguigu.crowd.VO.AddressVO;
import com.atguigu.crowd.VO.OrderProjectVO;
import com.atguigu.crowd.VO.OrderVO;
import com.atguigu.crowd.mapper.AddressPOMapper;
import com.atguigu.crowd.mapper.OrderPOMapper;
import com.atguigu.crowd.mapper.OrderProjectPOMapper;
import com.atguigu.crowd.service.api.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangbo
 */
// 在类上使用@Transactional(readOnly = true)针对查询操作设置事务属性
@Transactional(readOnly = true)
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderPOMapper orderPOMapper;

    @Autowired
    private OrderProjectPOMapper orderProjectPOMapper;

    @Autowired
    private AddressPOMapper addressPOMapper;

    @Override
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectPOMapper.selectOrderProjectVO(returnId);
    }

    @Override
    public List<AddressVO> getAddressVOList(Integer memberId) {
        AddressPOExample example = new AddressPOExample();
        example.createCriteria().andMemberIdEqualTo(memberId);
        List<AddressPO> addressPOList = addressPOMapper.selectByExample(example);
        List<AddressVO> addressVOList = new ArrayList<>();
        for(AddressPO addressPO : addressPOList){
            AddressVO addressVO = new AddressVO();
            BeanUtils.copyProperties(addressPO, addressVO);
            addressVOList.add(addressVO);
        }
        return addressVOList;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);
        addressPOMapper.insert(addressPO);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    @Override
    public void saveOrder(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);

        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderVO.getOrderProjectVO(), orderProjectPO);

        orderPOMapper.insert(orderPO);
        Integer id = orderPO.getId();
        orderProjectPO.setOrderId(id);
        orderProjectPOMapper.insert(orderProjectPO);
    }

}
