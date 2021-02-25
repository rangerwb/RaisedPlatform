package com.atguigu.crowd.handler;

import com.atguigu.crowd.VO.AddressVO;
import com.atguigu.crowd.VO.MemberLoginVO;
import com.atguigu.crowd.VO.OrderProjectVO;
import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author wangbo
 */
@Controller
public class OrderHandler {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo
            (
                    @PathVariable("projectId") Integer projectId,
                    @PathVariable("returnId") Integer returnId,
                    HttpSession session
            ){
        ResultEntity<OrderProjectVO> resultEntity = mySQLRemoteService.getOrderProjectVORemote(projectId, returnId);
        if(ResultEntity.SUCCESS.equals(resultEntity.getResult())) {
            OrderProjectVO orderProjectVO = resultEntity.getData();
            // 为了能够在后续操作中保持 orderProjectVO 数据，存入 Session 域
            session.setAttribute("orderProjectVO", orderProjectVO);
        }
        return "confirm_return";
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo
            (
                    @PathVariable("returnCount") Integer returnCount,
                    HttpSession session
            ) {
        // 1.把接收到的回报数量合并到 Session 域
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO", orderProjectVO);
        // 2.获取当前已登录用户的 id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();
        // 3.查询目前的收货地址数据
        ResultEntity<List<AddressVO>> resultEntity = mySQLRemoteService.getAddressVORemote(memberId);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            List<AddressVO> list = resultEntity.getData();
            session.setAttribute("addressVOList", list);
        }
        return "confirm_order";
    }

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session){
        ResultEntity<String> resultEntity = mySQLRemoteService.saveAddressRemote(addressVO);
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:http://localhost:80/order/confirm/order/" + returnCount;
    }

}
