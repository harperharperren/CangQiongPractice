package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param dishIds
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long dishIds);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

//    /**
//     * 根据id删除套餐
//     * @param setmealId
//     */
//    @Delete("delete from setmeal where id=#{setmealId}")
//    void deleteById(Long setmealId);

    /**
     * 根据套餐id查询套餐信息
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal where id=#{setmealId}")
    Setmeal getById(Long setmealId);

    /**
     * 根据套餐id批量删除套餐
     * @param setmealIds
     */
    void deleteByIds(List<Long> setmealIds);

    /**
     * 根据套餐id修改启售停售
     * @param status
     * @param id
     */
    @Update("update setmeal set status=#{status} where id=#{id}")
    void startOrEnd(Integer status, Integer id);

    /**
     * 传入新的setmeal，来修改setmeal
     * @param setmeal
     */
    void update(Setmeal setmeal);
}
