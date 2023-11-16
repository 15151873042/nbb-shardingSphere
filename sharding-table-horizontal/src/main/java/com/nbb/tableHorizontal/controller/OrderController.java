package com.nbb.tableHorizontal.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbb.tableHorizontal.entity.AjaxResult;
import com.nbb.tableHorizontal.entity.Order;
import com.nbb.tableHorizontal.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2023-11-15
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("/list")
    public AjaxResult list(LocalDateTime startTime, LocalDateTime endTime) {
        startTime = startTime != null ? startTime: LocalDateTimeUtil.parse("2023-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        endTime = endTime != null ? endTime: LocalDateTimeUtil.parse("2024-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");

        LambdaQueryWrapper<Order> queryWrapper = Wrappers.<Order>lambdaQuery()
                .ge(Order::getCreateTime, startTime)
                .lt(Order::getCreateTime, endTime);
        List<Order> list = orderService.list(queryWrapper);
        return AjaxResult.data(list);
    }

    @RequestMapping("/save")
    public AjaxResult save() {
        Order order = new Order();
        order.setCreateTime(LocalDateTimeUtil.parse("2023-10-10 10:10:10", "yyyy-MM-dd HH:mm:ss"));
        orderService.save(order);
        Order order2 = new Order();
        order2.setCreateTime(LocalDateTimeUtil.parse("2023-11-10 10:10:10", "yyyy-MM-dd HH:mm:ss"));
        orderService.save(order2);
        return AjaxResult.ok();
    }

}
