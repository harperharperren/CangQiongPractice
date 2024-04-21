package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.service.impl.SetmealServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/setmeal")
@Api("套餐管理接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealServiceImpl setmealServiceImpl;

    @ApiOperation("添加套餐")
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("添加的套餐信息为{}",setmealDTO);
        setmealServiceImpl.addSetmeal(setmealDTO);
        return Result.success();
    }
}
