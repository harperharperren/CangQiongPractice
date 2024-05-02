package com.sky.task;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class MyTask {
    @Autowired
    private OrderMapper orderMapper;
    @Scheduled(cron = "0 * * * * ? ")//系统每分钟检查一次数据库里面是否有超时订单
    public void processTimeOutOrder(){
        log.info("检查是否有超时订单.....");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> list=orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,time);//查找是否有超时订单
        if(list!=null&&list.size()>0){//查到有超时订单了
            for (Orders orders : list) {//对超时订单进行数据更新
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("支付超时");
                orderMapper.update(orders);
            }
        }
    }
    //@Scheduled(cron = "0/5 * * * * ? ") //测试，每5秒一次，方便测试
    @Scheduled(cron = "0 0 1 * * ? ")//每天凌晨1点处理一下昨天订单里面还在派送中的订单
    public void processDeliveryOrder(){
        log.info("处理前一天还在派送中的订单");
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> list = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);
        if(list!=null&&list.size()>0){
            for (Orders orders : list) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
