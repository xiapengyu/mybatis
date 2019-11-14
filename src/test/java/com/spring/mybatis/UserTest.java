package com.spring.mybatis;

import com.spring.mybatis.dao.UserDao;
import com.spring.mybatis.dto.PageInfo;
import com.spring.mybatis.dto.UserPageParam;
import com.spring.mybatis.entity.User;
import com.spring.mybatis.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MybatisApplication.class})
public class UserTest {

    @Autowired
    UserDao userDao;

    @Test
    public void testQuery(){
        /*User user = userDao.queryUserById(1L);
        System.out.println(user);*/

        User u = userDao.queryUserByCondition(1L);
        System.out.println(u);
    }

    @Test
    public void testQueryByPage(){
        Map<String, Object> resultMap = new HashMap<>();
        UserPageParam param = new UserPageParam();
        param.setPageSize(2);
        param.setCurrentPage(1);
        PageInfo<User> pageInfo = userDao.queryUserInfoByPage(param);
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("currentPage", pageInfo.getCurrentPage());
        resultMap.put("pageSize", pageInfo.getPageSize());
        resultMap.put("pages", (pageInfo.getTotal() % pageInfo.getPageSize() == 0) ?
                pageInfo.getTotal() / pageInfo.getPageSize() : pageInfo.getTotal() / pageInfo.getPageSize() + 1);
        resultMap.put("rows", pageInfo.getRows());
        System.out.println(JsonUtil.toJsonString(resultMap));
    }
}
