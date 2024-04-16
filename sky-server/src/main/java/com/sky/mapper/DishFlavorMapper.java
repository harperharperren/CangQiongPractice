package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

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
     * @param ids
     */
    void deleteByDishIds(List<Long> dishIds);
}
