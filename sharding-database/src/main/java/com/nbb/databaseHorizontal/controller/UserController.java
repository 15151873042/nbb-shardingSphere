package com.nbb.databaseHorizontal.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbb.databaseHorizontal.entity.AjaxResult;
import com.nbb.databaseHorizontal.entity.User;
import com.nbb.databaseHorizontal.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author hupeng
 * @since 2023-11-15
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/list")
    public AjaxResult list(LocalDateTime startTime, LocalDateTime endTime) {
        startTime = startTime != null ? startTime: LocalDateTimeUtil.parse("2023-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        endTime = endTime != null ? endTime: LocalDateTimeUtil.parse("2024-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");

        LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery()
                .ge(User::getCreateTime, startTime)
                .lt(User::getCreateTime, endTime);
        List<User> list = userService.list(queryWrapper);
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

        // 创建12个用户，创建时间为每个月1号
        List<User> UserList = createTimeList.stream()
                .map(crateTime -> new User().setCreateTime(crateTime))
                .collect(Collectors.toList());

        // 根据创建时间，上半年的数据会插入到ds_1，下半年的数据会插入到ds_2
        userService.saveBatch(UserList);
        return AjaxResult.ok();
    }

    @RequestMapping("/update")
    public AjaxResult update(Long id, LocalDateTime createTime) {
        // 更新数据时必须带上createTime
        LambdaUpdateWrapper<User> updateWrapper = Wrappers.<User>lambdaUpdate()
                .eq(User::getId, id)
                .eq(User::getCreateTime, createTime)
                .set(User::getCreateTime, createTime.plusDays(1));

        userService.update(updateWrapper);
        return AjaxResult.ok();
    }

}
