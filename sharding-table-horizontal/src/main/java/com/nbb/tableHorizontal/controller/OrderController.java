package com.nbb.tableHorizontal.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.nbb.tableHorizontal.entity.AjaxResult;
import com.nbb.tableHorizontal.entity.Order;
import com.nbb.tableHorizontal.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hupeng
 * @since 2023-11-15
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

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
