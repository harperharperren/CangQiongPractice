package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    TurnoverReportVO sumTurnOver(LocalDate begin, LocalDate end);

    UserReportVO sumUser(LocalDate begin, LocalDate end);

    OrderReportVO sumOrder(LocalDate begin, LocalDate end);
}
