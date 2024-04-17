package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api("店铺营业状态操作")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺营业状态
     * @param status
     * @return
     */
    @ApiOperation("设置店铺营业状态")
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态为{}",status==1?"营业中":"打样中");
        redisTemplate.opsForValue().set("Shop_Status",status);
        return Result.success();
    }

    /**
     * 查询店铺营业状态
     * @return
     */
    @ApiOperation("查询店铺营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus(){
        Integer shop_status =(Integer) redisTemplate.opsForValue().get("Shop_Status");
        log.info("店铺的营业状态为{}",shop_status==1?"营业中":"打烊中");
        return Result.success(shop_status);
    }

}
