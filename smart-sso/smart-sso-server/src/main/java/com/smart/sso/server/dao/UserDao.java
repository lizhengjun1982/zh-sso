package com.smart.sso.server.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.smart.mvc.dao.mybatis.Dao;
import com.smart.mvc.model.Pagination;
import com.smart.sso.server.model.User;

/**
 * 用户持久化接口
 *
 * @author Joe
 */
public interface UserDao extends Dao<User, Integer> {

    int enable(@Param("isEnable") Boolean isEnable, @Param("idList") List<Integer> idList);

    int resetPassword(@Param("password") String password, @Param("idList") List<Integer> idList);

    List<User> findPaginationByAccount(@Param("account") String account, Pagination<User> p);

    User findByAccount(@Param("account") String account);

    User findById(@Param("id") int id);

    User findByWeiXinOpenId(@Param("wxOpenId") String wxOpenId);

    User findByWeiXinWebOpenId(@Param("wxWebOpenId") String wxWebOpenId);

    /**
     * 更新用户登录相关信息
     *
     * @param user
     */
    void updateLoginInfo(User user);
}
