package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    void insert(Orders orders);
    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);
    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 更新订单状态
     * @param orderStatus
     * @param orderPaidStatus
     * @param check_out_time
     * @param id
     */
    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} where id = #{id}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, Long id);

    /**
     * 根据状态和下单时间查找订单
     * @param Status
     * @param time
     * @return
     */
    @Select("select * from orders where status=#{Status} and order_time<#{time}")
    List<Orders> getByStatusAndOrderTime(Integer Status, LocalDateTime time);

    /**
     * 根据订单id查询订单
     * @param id
     * @return
     */
    @Select("select  * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据状态和时间计算营业额
     * @param map
     * @return
     */
    Double getSumAcountByMap(Map map);

    /**
     * 根据状态和起始时间统计订单
     * @param status
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer getByStatusAndBeginEndTime(Integer status, LocalDateTime beginTime, LocalDateTime endTime);
}
