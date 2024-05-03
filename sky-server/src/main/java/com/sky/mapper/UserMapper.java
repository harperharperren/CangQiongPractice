package com.sky.mapper;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface UserMapper {



    /**
     * 根据Openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    public User getByUserId(String openid);

    /**
     * 创建用户
     * @param user
     */
    void insertUser(User user);

    /**
     * 根据id查找用户
     * @param userId
     * @return
     */
    @Select("select * from user where id=#{id}")
    User getById(Long userId);

    /**
     * 根据日期查询用户数量
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer getSumByDate(LocalDateTime beginTime, LocalDateTime endTime);
}
