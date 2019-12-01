package com.baizhi.dao;

import com.baizhi.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {
    @Insert("insert into user(login,account_id,token,gmt_create,gmt_modified) values(#{login},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insertUser(User user);

    @Select("select * from user where token=#{token}")
    User selectByToken(String token);
}
