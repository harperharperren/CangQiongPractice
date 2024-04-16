package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.mapper.DishMapper;
import com.sky.result.Result;
import com.sky.service.impl.DishServiceImpl;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin/dish")
@Api("菜品类操作")
public class DishController {
    @Autowired
    private DishServiceImpl dishServiceImpl;
    /**
     * 新增菜品
     * @return
     */
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品{}",dishDTO);
        dishServiceImpl.saveWithFlavor(dishDTO);
        return Result.success();
    }
}
