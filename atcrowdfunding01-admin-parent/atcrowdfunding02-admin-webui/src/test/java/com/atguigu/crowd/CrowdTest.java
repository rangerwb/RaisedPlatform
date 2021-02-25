package com.atguigu.crowd;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//再类上标记必要的注解，Spring整合Junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml", "classpath:spring-persist-tx.xml"})
public class CrowdTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void insertRoleValue() {
        for (int i = 0; i < 150; i++) {
            roleMapper.insert(new Role(null, "role" + i));
        }
    }

    @Test
    public void test() {
        for (int i = 0; i < 238; i++) {
            adminMapper.insert(new Admin(null, "loginAcct" + i, "userPswd" + i, "userName" + i, "email@qq.com", null));
        }
    }

    @Test
    public void testTx() {
        Admin admin = new Admin(null, "jerry", "123456", "杰瑞", "jerry@qq.com", null);
        adminService.saveAdmin(admin);
    }

    //测试日志系统
    @Test
    public void testLog() {
        //获取Logger对象，这里传入的Class对象就是当前打印日志的类
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);

        //根据不同级别打印日志
        logger.debug("debug level");
        logger.debug("debug level");
        logger.debug("debug level");

        logger.info("info level");
        logger.info("info level");
        logger.info("info level");

        logger.warn("warn level");
        logger.warn("warn level");
        logger.warn("warn level");

        logger.error("error level");
        logger.error("error level");
        logger.error("error level");

    }

    @Test
    public void testInsertAdmin() {
        Admin admin = new Admin(null, "tom", "123", "汤姆", "tom@qq.com", null);
        int insert = adminMapper.insert(admin);

        //如果在实际开发中，所有想查看数值的地方都使用sout方式打印，会给项目上线运行带来问题
        //sout本质是个IO操作，过多sout会对性能产生很大影响
        //上线前专门删除也恐有遗漏
        //使用日志系统，控制日志级别就可控制信息打印
        System.out.println(insert + "插入成功");
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }

    /**
     * 编程式事务范本
     */
    public class ProgrammedTransaction {

        @Autowired
        private DataSource dataSource;

        public void Test1() throws SQLException {
            Connection connection = dataSource.getConnection();
            try {
                //开始事务(关闭自动提交)
                connection.setAutoCommit(false);//对应Spring AOP通知：前置通知

                //核心操作
                //adminService.saveAdmin(admin);

                //提交事务
                connection.commit();//对应Spring AOP通知：返回通知
            } catch (Exception e) {
                //回滚事务
                connection.rollback();//对应Spring AOP通知：异常通知
            } finally {
                //释放数据库连接
                connection.close();//对应Spring AOP通知：后置通知
            }
            //整体被称为环绕通知
        }

    }

}
