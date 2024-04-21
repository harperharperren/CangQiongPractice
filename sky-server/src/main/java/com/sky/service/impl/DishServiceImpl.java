package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {//对dish表进行插入，对dishflavor表进行插入
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);//准备插入dish表的数据
        dishMapper.saveWithFlavor(dish);
        Long dishId = dish.getId();//获取新生成的dishid，后面dishFlavor用
        List<DishFlavor> flavors = dishDTO.getFlavors();//准备插入dishFlavor表的数据
        if(flavors!=null&&flavors.size()>0){//Flavor的子元素是dishFlavor对象
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));//给每个dishFlavor对象加入dishid
            dishFlavorMapper.saveFlavors(flavors);
        }


    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> p=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(p.getTotal(),p.getResult());
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional//多表操作，使用事务
    public void delete(List<Long> ids) {
        //1.检查是否是禁售
        for (Long id : ids) {
            Dish dish=dishMapper.getById(id);
            if(dish.getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //2.检查是否和套餐关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdbyDishId(ids);
        if(setmealIds!=null&&setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //3.删除菜品
        dishMapper.deleteByIds(ids);
        //删除口味
        dishFlavorMapper.deleteByDishIds(ids);

    }

    /**
     * 根据id查找dish以及Flavor
     * @param id
     * @return
     */
    @Transactional
    public DishVO getWithFlavorById(Long id) {
        Dish dish=dishMapper.getById(id);
        List<DishFlavor> flavors=dishFlavorMapper.getFlavorById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    public void update(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);//修改菜品
        dishFlavorMapper.deleteByDishId(dish.getId());//删除口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors!=null&&flavors.size()>0){//新增口味
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));//给每个dishFlavor对象加入dishid
            dishFlavorMapper.saveFlavors(flavors);
        }
    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    @Transactional
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getFlavorById(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }

    /**
     * 菜品启售停售
     * @param status
     * @param dishId
     */
    public void startOrEndDish(Integer status, Integer dishId) {
        dishMapper.startOrEndDish(status,dishId);
    }

    /**
     * 根据分类id查找菜品
     * @param setmealId
     * @return
     */
    public List<Dish> getdishesBySetmealId(Long setmealId) {
        Dish dish = Dish.builder()
                .categoryId(setmealId).build();
        List<Dish> dishes = dishMapper.list(dish);
        return dishes;
    }
}
