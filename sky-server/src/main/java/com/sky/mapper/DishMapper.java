package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品+口味
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    void saveWithFlavor(Dish dish);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查找菜品
     * @param id
     * @return
     */
    @Select("select * from dish where id=#{id}")
    Dish getById(Long id);



    /**
     * 根据id批量删除菜品
     * @param dishIds
     */
    void deleteByIds(List<Long> dishIds);

    /**
     * 修改菜品
     * @param dish
     */
    void update(Dish dish);

    /**
     * 根据类别id查找菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);



    /**
     * 启用禁用菜品
     * @param status
     * @param dishId
     */
    @Update("update dish set status=#{status} where id=#{dishId}")
    void startOrEndDish(Integer status, Integer dishId);

    /**
     * 根据菜品类别id查找菜品
     * @param id
     * @return
     */
    @Select("select d.* from dish as d left join setmeal_dish as sd  on d.id=sd.dish_id")
    List<Dish> getBySetmealId(Integer id);
}
