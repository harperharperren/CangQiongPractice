package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.impl.DishServiceImpl;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){
        log.info("新增菜品{}",dishDTO);
        dishServiceImpl.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @return
     */
    @ApiOperation("菜品分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询{}",dishPageQueryDTO);
        PageResult pageResult=dishServiceImpl.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){//获取前端传来的数组
        log.info("批量删除菜品....");
        dishServiceImpl.delete(ids);
        return Result.success();
    }

    /**
     * 根据菜品id查找菜品
     * @param id
     * @return
     */
    @ApiOperation("根据id查找菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id {}查找菜品",id);
        DishVO dishVO=dishServiceImpl.getWithFlavorById(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("新增菜品{}",dishDTO);
        dishServiceImpl.update(dishDTO);
        return Result.success();
    }

    /**
     * 菜品启售停售
     * @param status
     * @param dishId
     * @return
     */
    @ApiOperation("菜品启售停售")
    @PostMapping("/status/{status}")
    public Result startOrEndDish(@PathVariable("status") Integer status,@RequestParam("id") Integer dishId){
        log.info("要修改的菜品id为{}，其要修改为{状态}",dishId,status==1?"启售":"停售");
        dishServiceImpl.startOrEndDish(status,dishId);
        return Result.success();
    }

    /**
     * 根据分类id查找菜品
     * @param setmealId
     * @return
     */
    @ApiOperation("根据分类id查找菜品功能")
    @GetMapping("/list")
    public Result<List<Dish>> getDishesBySetmealId(Long setmealId){
        log.info("要查找的菜品分类id为{}",setmealId);
        List<Dish> dishes=dishServiceImpl.getdishesBySetmealId(setmealId);
        return Result.success(dishes);
    }


}
