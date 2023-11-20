package com.nbb.databaseHorizontal.framework;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 按月分表的 Sharding 算法
 */
@Slf4j
@Component
public class DatabaseShardingAlgorithm implements StandardShardingAlgorithm<LocalDateTime> {

    /**
     * 精确路由算法
     *
     * @param availableTargetNames 可用的表列表（配置文件中配置的 actual-data-nodes会被解析成 列表被传递过来）
     * @param shardingValue        精确的值
     * @return 结果表
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<LocalDateTime> shardingValue) {
        LocalDateTime checkDateTimeDateTime = LocalDateTimeUtil.parse("2023-07-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        LocalDateTime value = shardingValue.getValue();

        List<String> databaseNameList = availableTargetNames.stream().collect(Collectors.toList());

        // 上半年的数据放入ds_1库，下半年的数据放入ds_2库
        String dataSourceName = value.isBefore(checkDateTimeDateTime) ? databaseNameList.get(0) : databaseNameList.get(1);

        return dataSourceName;



    }

    /**
     * 范围路由算法
     *
     * @param availableTargetNames 可用的表列表（配置文件中配置的 actual-data-nodes会被解析成 列表被传递过来）
     * @param shardingValue        值范围
     * @return 路由后的结果表
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<LocalDateTime> shardingValue) {

        // 给一个开始时间下限
        LocalDateTime startDateTime = LocalDateTimeUtil.parse("2023-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        if (shardingValue.getValueRange().hasLowerBound()) {
            startDateTime = shardingValue.getValueRange().lowerEndpoint();
        }

        // 给一个结束时间下限
        LocalDateTime endDateTime = LocalDateTimeUtil.parse("2024-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        if (shardingValue.getValueRange().hasUpperBound()) {
            endDateTime = shardingValue.getValueRange().upperEndpoint();
        }


        LocalDateTime checkDateTimeDateTime = LocalDateTimeUtil.parse("2023-07-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        List<String> databaseNameList = availableTargetNames.stream().collect(Collectors.toList());

        // 开始时间和结束实现都小于上半年，则使用ds_1
        if (startDateTime.isBefore(checkDateTimeDateTime) && endDateTime.isBefore(checkDateTimeDateTime)) {
            return Collections.singleton(databaseNameList.get(0));
        }
        // 开始时间和结束实现都大于上半年，则使用ds_2
        if (startDateTime.isBefore(checkDateTimeDateTime) && endDateTime.isBefore(checkDateTimeDateTime)) {
            return Collections.singleton(databaseNameList.get(1));
        }


        return databaseNameList;

    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void init(Properties properties) {
    }


    /**
     * SPI方式的 SPI名称，配置文件中配置的时候需要用到
     */
    @Override
    public String getType() {
        return "hupeng_sharding_database";
    }
}
