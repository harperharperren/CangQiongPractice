package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
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
}
