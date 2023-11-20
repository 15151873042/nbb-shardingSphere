package com.nbb.readWriteSplit.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nbb.readWriteSplit.entity.AjaxResult;
import com.nbb.readWriteSplit.entity.ReadWrite;
import com.nbb.readWriteSplit.service.IReadWriteService;
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
@RequestMapping("/readWrite")
public class ReadWriteController {

    @Autowired
    private IReadWriteService readWriteService;

    @RequestMapping("/list")
    public AjaxResult list() {
        List<ReadWrite> list = readWriteService.list();
        return AjaxResult.data(list);
    }

    @RequestMapping("/add")
    public AjaxResult add() {

        ReadWrite entity = new ReadWrite();
        entity.setCreateTime(LocalDateTime.now());

        readWriteService.save(entity);
        return AjaxResult.ok();
    }

    @RequestMapping("/update")
    public AjaxResult update(Long id) {


        ReadWrite entity = new ReadWrite();
        entity.setId(id);
        entity.setCreateTime(LocalDateTime.now());

        readWriteService.updateById(entity);
        return AjaxResult.ok();
    }

}
