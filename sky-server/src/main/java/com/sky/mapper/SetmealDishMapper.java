package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据dish id来查询setmeal id
     * @param dishIds
     * @return
     */
    //select setmeal_id from setmealDish where dish_id in(1,2,3);
    public List<Long> getSetmealIdbyDishId(List<Long> dishIds);

    /**
     * foreach，把菜品都加到setmealDish表里面
     * @param setmealDishes
     */
    void insert(List<SetmealDish> setmealDishes);

//    /**
//     * 根据套餐id删除套餐与菜品的关联信息
//     * @param setmealId
//     */
//    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
//    void deleteById(Long setmealId);

    /**
     * 批量删除菜品与套餐关联信息
     * @param setmealIds
     */
    void deleteByIds(List<Long> setmealIds);

    /**
     * 根据setmealId查询setmealDishes
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id=#{id}")
    List<SetmealDish> getSetmealDishesBySetmealId(Long id);

    /**
     * 根据setmealId来删除SetmealDish里面的dish
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{id}")
    void deleteById(Long id);
}
