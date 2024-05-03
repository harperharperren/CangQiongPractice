package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 查询营业额
     * @param begin
     * @param end
     * @return
     */
    public TurnoverReportVO sumTurnOver(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList();//构造dateList数据
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);//+1操作，返回值必须赋给begin，他是创建了一个新的LocalDate对象
            dateList.add(begin);
        }
        List<Double> turnOverList=new ArrayList();//构造每日营业额数据
        for (LocalDate date : dateList) {
            LocalDateTime beginTime= LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);
            Map map=new HashMap();//这里用map传数据到mapper里，感觉和对象的用法差不多
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double sum=orderMapper.getSumAcountByMap(map);
            sum=(sum==null)?0.0:sum;//没有营业额查出来是null，手动赋值为0.0
            turnOverList.add(sum);
        }
        String dateListStr= StringUtils.join(dateList,",");//java.lang3的StringUtils
        String turnOverListStr=StringUtils.join(turnOverList,",");//转为前端需要的数据格式
        TurnoverReportVO res = TurnoverReportVO.builder()
                .dateList(dateListStr)
                .turnoverList(turnOverListStr)
                .build();
        return res;
    }

    /**
     * 统计用户数量
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO sumUser(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList();//拼凑出日期
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> newUserList=new ArrayList();//拼凑出新增用户数量
        List<Integer> totalUserList=new ArrayList();//拼凑出总用户数量
        for (LocalDate date : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);
            Integer newUser=userMapper.getSumByDate(beginTime,endTime);//获取每日新增用户数
            Integer totalUser=userMapper.getSumByDate(null,endTime);//获取截止目前的用户总数
            newUser=(newUser==null)?0:newUser;
            totalUser=(totalUser==null)?0:totalUser;
            newUserList.add(newUser);
            totalUserList.add(totalUser);
        }
        String dateListStr=StringUtils.join(dateList,",");
        String newUserListStr=StringUtils.join(newUserList,",");
        String totalUserListStr=StringUtils.join(totalUserList,",");
        UserReportVO userReportVO = UserReportVO.builder()
                .dateList(dateListStr)
                .newUserList(newUserListStr)
                .totalUserList(totalUserListStr)
                .build();
        return userReportVO;
    }

    /**
     * 统计订单数量
     * @param begin
     * @param end
     * @return
     */
    public OrderReportVO sumOrder(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList();//构造日期
        dateList.add(begin);
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList=new ArrayList();//构造订单数列表
        List<Integer> validOrderCountList=new ArrayList();//构造有效订单数列表
        Integer totalOrderCount=0;
        Integer totalValidOrderCount=0;
        for (LocalDate date : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(date,LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);
            Integer orderCount=orderMapper.getByStatusAndBeginEndTime(null,beginTime,endTime);
            Integer validOrderCount=orderMapper.getByStatusAndBeginEndTime(Orders.COMPLETED,beginTime,endTime);
            totalOrderCount+=orderCount;
            totalValidOrderCount+=validOrderCount;
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        String dateListStr=StringUtils.join(dateList,",");
        String orderCountListStr=StringUtils.join(orderCountList,",");
        String validOrderCountListStr=StringUtils.join(validOrderCountList,",");
        Double rate=0.0;
        if(totalOrderCount!=0){//这里是避免总订单数为0出现异常
            rate=totalValidOrderCount.doubleValue()/ totalOrderCount;
        }
        OrderReportVO orderReportVO = OrderReportVO.builder()
                .dateList(dateListStr)
                .orderCountList(orderCountListStr)
                .validOrderCountList(validOrderCountListStr)
                .totalOrderCount(totalOrderCount)
                .validOrderCount(totalValidOrderCount)
                .orderCompletionRate(rate)
                .build();
        return orderReportVO;
    }

    /**
     * 统计top10菜品
     * @param begin
     * @param end
     * @return
     */
    public SalesTop10ReportVO salesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime=LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime=LocalDateTime.of(end,LocalTime.MAX);
        List<GoodsSalesDTO> top10Sales=orderMapper.getSalesTop10(beginTime,endTime);//查找top10菜品
        //把查出来的结果分离，使用Stream流，最后用collect方法处理成集合
        List<String> names = top10Sales.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> number = top10Sales.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String namesStr = StringUtils.join(names, ",");
        String numberStr = StringUtils.join(number, ",");
        SalesTop10ReportVO salesTop10ReportVO = SalesTop10ReportVO.builder()
                .nameList(namesStr)
                .numberList(numberStr)
                .build();
        return salesTop10ReportVO;
    }
}
