package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
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
}
