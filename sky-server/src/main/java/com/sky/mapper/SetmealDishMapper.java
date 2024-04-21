package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
}
