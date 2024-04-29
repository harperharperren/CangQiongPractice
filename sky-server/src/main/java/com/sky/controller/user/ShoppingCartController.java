package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api("用户购物车相关操作")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("用户添加购物车操作")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("要添加到购物车的信息是{}",shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }
    @GetMapping("/list")
    @ApiOperation("查看购物车功能")
    public Result<List<ShoppingCart>> list(){
        log.info("查看购物车功能");
        List<ShoppingCart> list=shoppingCartService.list();
        return Result.success(list);
    }
    @ApiOperation("清除购物车")
    @DeleteMapping("/clean")
    public Result deleteAll(){
        log.info("删除购物车");
        shoppingCartService.deleteAll();
        return Result.success();
    }
}
