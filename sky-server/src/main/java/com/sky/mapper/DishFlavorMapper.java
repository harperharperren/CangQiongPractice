package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 插入口味(新增菜品操作)
     * @param flavors
     */
    void saveFlavors(List<DishFlavor> flavors);

    /**
     * 根据dish id批量删除口味
     * @param dishIds
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据id查找Flavor
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id=#{id}")
    List<DishFlavor> getFlavorById(Long id);

    /**
     * 根据dish id删除flavor
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id=#{id}")
    void deleteByDishId(Long id);
}
