package com.spring.mybatis.dao;

import com.spring.mybatis.base.BaseDao;
import com.spring.mybatis.dto.Page;
import com.spring.mybatis.dto.PageInfo;
import com.spring.mybatis.dto.UserPageParam;
import com.spring.mybatis.entity.User;
import com.spring.mybatis.entity.UserCriteria;
import com.spring.mybatis.mapper.UserMapper;
import com.spring.mybatis.mapperExt.UserExtMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends BaseDao<UserMapper, UserExtMapper, User> {

    @Override
    protected void setMapperClass() {
        setMapperClass(UserMapper.class);
        setExtMapperClass(UserExtMapper.class);
    }

    @Override
    protected void setEntityClass() {
        setEntityClass(User.class);
    }

    public User queryUserById(long id){
        return this.getMapper().selectByPrimaryKey(id);
    }

    public User queryUserByCondition(long id){
        /*UserCriteria c = new UserCriteria();
        UserCriteria.Criteria cc = c.createCriteria();
        cc.andIdEqualTo(id);
        List<User> list = this.getMapper().selectByExample(c);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }*/
        return this.getExtMapper().queryUserByCondition(id);
    }

    public PageInfo<User> queryUserInfoByPage(UserPageParam param){
        //查询总记录数
        int total = this.getExtMapper().selectTotalCount(param, null);
        //初始化pageInfo
        PageInfo<User> pageInfo = new PageInfo<User>();
        pageInfo.setCount(total);
        pageInfo.setCurrentPage(param.getCurrentPage());
        pageInfo.setPageSize(param.getPageSize());
        //初始请求参数
        Page page = new Page(param.getCurrentPage(), param.getPageSize());
        List<User> resultList = this.getExtMapper().queryUserInfoByPage(param, page);
        //设置rows
        pageInfo.setRows(resultList);
        return pageInfo;
    }

}
