package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    /**
     * 用户下单功能
     * @param ordersSubmitDTO
     * @return
     */
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        //1.先判断业务异常（是否地址为空、是否购物车为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if(addressBook==null){//判断地址是否为空
            throw new AddressBookBusinessException("地址不能为空");
        }
        List<ShoppingCart> list = shoppingCartMapper.findByUserAndDishId(shoppingCart);
        if(list==null||list.size()==0){//判断购物车不能为空
            throw new ShoppingCartBusinessException("购物车不能为空");
        }
        //2.向Order表插入1条数据
        Orders orders = new Orders();//创建orders对象，用于插入表中
        BeanUtils.copyProperties(ordersSubmitDTO,orders);//进行一些数据的拷贝
        orders.setNumber(String.valueOf(System.currentTimeMillis()));//插入一些数据
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orderMapper.insert(orders);
        //3.向Order_details表插入n条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();//这里创建order_details集合，批量插入
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.findByUserAndDishId(shoppingCart);
        for (ShoppingCart cart : shoppingCartList) {//遍历购物车里的菜品
            OrderDetail orderDetail = new OrderDetail();//创建orderDetail对象，用于插入表中
            orderDetail.setOrderId(orders.getId());//把Orderdetails和Order关联起来
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insert(orderDetailList);//批量插入
        //4.清空购物车
        shoppingCartMapper.delete(BaseContext.getCurrentId());
        //5.返回OrderSubmitVO数据
        OrderSubmitVO orderSubmitVO= OrderSubmitVO.builder()
                .id(orders.getId())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
        return orderSubmitVO;
    }
}
