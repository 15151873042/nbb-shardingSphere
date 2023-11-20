package com.nbb.tableHorizontal.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbb.tableHorizontal.entity.AjaxResult;
import com.nbb.tableHorizontal.entity.Order;
import com.nbb.tableHorizontal.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        endTime = endTime != null ? endTime: LocalDateTimeUtil.parse("2023-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");

        LambdaQueryWrapper<Order> queryWrapper = Wrappers.<Order>lambdaQuery()
                .ge(Order::getCreateTime, startTime)
                .lt(Order::getCreateTime, endTime);
        List<Order> list = orderService.list(queryWrapper);
        return AjaxResult.data(list);
    }

    @RequestMapping("/add")
    public AjaxResult add() {
        // 生成每月1号的日期
        LocalDateTime firstMonth = LocalDateTimeUtil.parse("2023-01-01 10:10:10", "yyyy-MM-dd HH:mm:ss");
        List<LocalDateTime> createTimeList = IntStream.range(1, 12)
                .mapToObj(num -> firstMonth.plusMonths(num))
                .collect(Collectors.toList());
        createTimeList.add(0, firstMonth);

        // 每月1个订单
        List<Order> orderList = createTimeList.stream()
                .map(crateTime -> new Order().setCreateTime(crateTime))
                .collect(Collectors.toList());

        // 根据订单时间所在月垂直拆分，订单会插入到不同的表中
        orderService.saveBatch(orderList);
        return AjaxResult.ok();
    }

    @RequestMapping("/update")
    public AjaxResult update(Long id, LocalDateTime createTime) {
        // 更新数据时必须带上createTime
        LambdaUpdateWrapper<Order> updateWrapper = Wrappers.<Order>lambdaUpdate()
                .eq(Order::getId, id)
                .eq(Order::getCreateTime, createTime)
                .set(Order::getCreateTime, createTime.plusDays(1));

        orderService.update(updateWrapper);
        return AjaxResult.ok();
    }

}
