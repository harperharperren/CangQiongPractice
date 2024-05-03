package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.service.impl.ReportServiceImpl;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Api("数据统计相关操作")
@Slf4j
public class ReportController {
    @Autowired
    private ReportServiceImpl reportServiceImpl;

    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        log.info("营业额统计的起始日期为{}.{}",begin,end);
        return Result.success(reportServiceImpl.sumTurnOver(begin,end));
    }
    @GetMapping("/userStatistics")
    @ApiOperation("用户数量统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end){
        log.info("统计用户数据的起始时间为{}，{}",begin,end);
        return Result.success(reportServiceImpl.sumUser(begin,end));
    }
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计操作")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end){
        log.info("统计订单的起始时间为{},{}",begin,end);
        return Result.success(reportServiceImpl.sumOrder(begin,end));
    }
    @GetMapping("/top10")
    @ApiOperation("top10菜品统计操作")
    public Result<SalesTop10ReportVO> salesTop10Statistics(
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate end){
        log.info("top10菜品统计的起始时间为{},{}",begin,end);
        return Result.success(reportServiceImpl.salesTop10(begin,end));
    }
}
