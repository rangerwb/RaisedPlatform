package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Student;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @ResponseBody
    @RequestMapping("test/ajax/async.html")
    public String testAsync() throws InterruptedException {
        Thread.sleep(2000);
        return "successs";
    }

    @RequestMapping(value = "test/ssm.html")
    public String testSSM(ModelMap modelMap, HttpServletRequest request) {
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult=" + judgeResult);

        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList", adminList);

        System.out.println(10 / 0);

        return "target";
    }

    @ResponseBody
    @RequestMapping(value = "send/array/one.html")
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> array) {
        for (Integer number : array) {
            System.out.println("number=" + number);
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "send/array/two.html")
    public String testReceiveArrayTwo(@RequestParam("array") List<Integer> array) {
        for (Integer number : array) {
            logger.info("number=" + number);
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "send/array/three.html")
    public String testReceiveArrayThree(@RequestBody List<Integer> array) {
        for (Integer number : array) {
            logger.info("number=" + number);
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping(value = "/send/compose/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student, HttpServletRequest request) {
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult=" + judgeResult);

        logger.info(student.toString());

        //将student对象封装到ResultEntity中返回
        ResultEntity<Student> resultEntity = ResultEntity.successWithData(student);
        return resultEntity;
    }

}
