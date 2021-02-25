package com.atguigu.crowd;

import com.atguigu.crowd.PO.MemberPO;
import com.atguigu.crowd.VO.DetailProjectVO;
import com.atguigu.crowd.VO.DetailReturnVO;
import com.atguigu.crowd.VO.PortalProjectVO;
import com.atguigu.crowd.VO.PortalTypeVO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Atcrowdfunding10MemberMysqlProviderApplication.class})
public class MyBatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    private Logger logger = LoggerFactory.getLogger(MyBatisTest.class);

    @Test
    public void testLoadDetailProjectVO() {
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(3);
        logger.info(detailProjectVO.toString());
        List<String> detailPicturePathList = detailProjectVO.getDetailPicturePathList();
        logger.info(detailPicturePathList.toString());
        List<DetailReturnVO> detailReturnVOList = detailProjectVO.getDetailReturnVOList();
        logger.info(detailReturnVOList.toString());
    }

    @Test
    public void testLoadTypeData(){
        List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();
        for (PortalTypeVO p : portalTypeVOList) {
            String name = p.getName();
            String remark = p.getRemark();
            logger.info("name=" + name + ", remark=" + remark);

            List<PortalProjectVO> portalProjectVOList = p.getPortalProjectVOList();
            for (PortalProjectVO projectVO : portalProjectVOList){
                if (projectVO == null){
                    continue;
                }
                logger.info(projectVO.toString());
            }
        }
    }

    @Test
    public void test1() throws SQLException {
        Connection connection = dataSource.getConnection();
        logger.debug(connection.toString());
    }

    @Test
    public void testMapper(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "123123";
        String encode = passwordEncoder.encode(source);
        MemberPO memberPO = new MemberPO(null, "jack1", encode, " 杰-克 ", "jack@qq.com",
                1, 1, "杰-克", "123123", 2);
        memberPOMapper.insert(memberPO);
    }

}
