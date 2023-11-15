package com.nbb.tableHorizontal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nbb.tableHorizontal.entity.Order;
import com.nbb.tableHorizontal.mapper.OrderMapper;
import com.nbb.tableHorizontal.service.IOrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hupeng
 * @since 2023-11-15
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
