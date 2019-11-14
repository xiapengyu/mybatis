package com.spring.mybatis.mapperExt;

import com.spring.mybatis.dto.Page;
import com.spring.mybatis.dto.UserPageParam;
import com.spring.mybatis.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserExtMapper {

    public User queryUserByCondition(long id);

    /**
     * 查询总记录数
     * @param param
     * @param page
     * @return
     */
    public int selectTotalCount(@Param("param") UserPageParam param, @Param("page") Page page);

    /**
     * 分页查询用户信息
     * @param param
     * @param page
     * @return
     */
    public List<User> queryUserInfoByPage(@Param("param") UserPageParam param, @Param("page") Page page);

}
