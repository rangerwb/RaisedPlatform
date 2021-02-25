package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseForUpdateException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    @Override
    public void saveAdmin(Admin admin) {
        // 1.密码加密
        String userPswd = admin.getUserPswd();
        // userPswd = CrowdUtil.md5(userPswd);
        userPswd = passwordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);

        // 2.生成创建时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = dateFormat.format(date);
        admin.setCreateTime(createTime);

        // 3.执行保存
        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();

            logger.info("异常全类名=" + e.getClass().getName());

            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginAcct, String userPswd) {

        // 1.根据登录账号查询Admin对象
        // 1.1 创建AdminExample对象
        AdminExample adminExample = new AdminExample();
        // 1.2 创建Critical对象
        AdminExample.Criteria criteria = adminExample.createCriteria();
        // 1.3 在Critical对象中封装查询条件
        criteria.andLoginAcctEqualTo(loginAcct);
        // 1.4 调用AdminMapper的方法执行查询
        List<Admin> list = adminMapper.selectByExample(adminExample);

        // 2.判断Admin对象是否为null
        if (list == null || list.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        if (list.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }

        Admin admin = list.get(0);

        // 3.如果Admin对象为null则抛出异常
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 4.如果Admin对象不为null则将数据库密码从Admin对象中取出
        String userPswdDB = admin.getUserPswd();

        // 5.将表单提交的明文密码进行加密
        String userPswdForm = CrowdUtil.md5(userPswd);

        // 6.对密码进行比较
        if (!Objects.equals(userPswdDB, userPswdForm)) {
            // 7.如果比较结果不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }

        // 8.若果一致则返回Admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {

        // 1.调用PageHelper的静态方法开启分页功能
        // 这里体现出PageHelper的"非侵入式"设计，本身的查询不需要进行任何修改
        PageHelper.startPage(pageNum, pageSize);

        // 2.执行查询
        List<Admin> list = adminMapper.selectAdminByKeyword(keyword);

        // 3.封装到PageInfo对象中
        return new PageInfo<>(list);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        // Selective：有选择的进行更新，值为null的不更新
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
        } catch (Exception e) {
            e.printStackTrace();

            logger.info("异常全类名=" + e.getClass().getName());

            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseForUpdateException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // 旧数据：
        // adminId      roleId
        // 1            1（删除）
        // 1            2（删除）
        // 1            3
        // 1            4
        // 1            5
        // 新数据：
        // adminId      roleId
        // 1            3
        // 1            4
        // 1            5
        // 1            6（新增）
        // 1            7（新增）
        // 为了简化操作：先根据adminId删除旧的数据，在根据roleIdList保存全部新的数据

        // 1.根据adminId删除旧的关联关系数据
        adminMapper.deleteOldRelationship(adminId);

        // 2.根据roleIdList和adminId保存新的关联关系
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }

    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        // 构件AdminExample对象
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(username);

        // 查询
        List<Admin> list = adminMapper.selectByExample(adminExample);
        Admin admin = list.get(0);
        return admin;
    }
}
