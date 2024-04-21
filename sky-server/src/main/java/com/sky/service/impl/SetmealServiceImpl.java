package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        Long id=setmeal.getId();//获取套餐id，为后面插入setmealdish做准备
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {//为每个菜品绑定套餐id
            setmealDish.setSetmealId(id);
        }
        setmealDishMapper.insert(setmealDishes);//把菜品和套餐关联
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> p=setmealMapper.page(setmealPageQueryDTO);
        return new PageResult(p.getTotal(),p.getResult());
    }

    /**
     * 根据套餐id批量删除套餐
     * @param setmealIds
     */
    @Transactional
    public void delete(List<Long> setmealIds) {
        //检查是否是启售中的套餐，启售中不能删
        for (Long setmealId : setmealIds) {
            Setmeal setmeal=setmealMapper.getById(setmealId);
            if(setmeal.getStatus()==StatusConstant.DISABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
            //批量删除setmeal表信息
            setmealMapper.deleteByIds(setmealIds);
            //批量删除setmealDish表信息
            setmealDishMapper.deleteByIds(setmealIds);
    }

    /**
     * 套餐的启售停售
     * @param status
     * @param id
     */

    public void startOrEnd(Integer status, Integer id) {
        //启售套餐时需检验：套餐里是否有停售菜品，如果有要抛异常
        if(status==StatusConstant.ENABLE){
            List<Dish> dishes=dishMapper.getBySetmealId(id);
            if(dishes!=null&& dishes.size()>0){
                for (Dish dish : dishes) {
                    if(dish.getStatus()==StatusConstant.DISABLE){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                }
            }
        }
        setmealMapper.startOrEnd(status,id);
    }

    /**
     * 根据setmealId查询套餐信息
     * @param id
     * @return
     */
    public SetmealVO getBySetmealId(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes=setmealDishMapper.getSetmealDishesBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;

    }
}
