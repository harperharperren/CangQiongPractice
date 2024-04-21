package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.service.impl.SetmealServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiOperation("套餐分页查询功能")
    @GetMapping("/page")
    public Result<PageResult> Page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("要分页查询的信息为{}",setmealPageQueryDTO);
        PageResult pageResult=setmealServiceImpl.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    @ApiOperation("批量删除套餐")
    @DeleteMapping()
    public Result delete(@RequestParam("ids")  List<Long> setmealIds){
        log.info("要删除的套餐id为{}",setmealIds);
        setmealServiceImpl.delete(setmealIds);
        return Result.success();
    }
    @ApiOperation("套餐启售停售功能")
    @PostMapping("/status/{status}")
    public Result startOrEnd(@PathVariable("status") Integer status,Integer id){
        log.info("要修改状态的菜品id为：{},其要修改的状态为{}",id,status==1?"启售":"停售");
        setmealServiceImpl.startOrEnd(status,id);
        return Result.success();
    }
}
