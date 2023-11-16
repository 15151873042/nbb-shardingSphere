package com.nbb.tableHorizontal.framework;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class CustomAlgorithm implements StandardShardingAlgorithm<LocalDateTime> {

    /**
     * 精确路由算法
     *
     * @param availableTargetNames 可用的表列表（配置文件中配置的 actual-data-nodes会被解析成 列表被传递过来）
     * @param shardingValue        精确的值
     * @return 结果表
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<LocalDateTime> shardingValue) {
        LocalDateTime value = shardingValue.getValue();
        // 根据精确值获取路由表
        String actuallyTableName = shardingValue.getLogicTableName() + LocalDateTimeUtil.format(value, "yyyyMM");
        return actuallyTableName;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<LocalDateTime> shardingValue) {
        List<LocalDateTime> rangeDateTime = new ArrayList<>();

        // 给一个开始时间下限
        LocalDateTime startDateTime = LocalDateTimeUtil.parse("2023-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        if (shardingValue.getValueRange().hasLowerBound()) {
            startDateTime = shardingValue.getValueRange().lowerEndpoint();
        }
        rangeDateTime.add(startDateTime);

        // 给一个结束时间下限
        LocalDateTime endDateTime = LocalDateTimeUtil.parse("2024-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        if (shardingValue.getValueRange().hasUpperBound()) {
            endDateTime = shardingValue.getValueRange().upperEndpoint();
        }
        rangeDateTime.add(endDateTime);



        // 计算开始日和结束日所在月份相差几个月
        int monthBetween = endDateTime.getMonthValue() - startDateTime.getMonthValue();

        if (monthBetween > 0) {
            // 如果中间间隔多个月，这几个月都需要添加
            final LocalDateTime beginDateTime = startDateTime;
            List<LocalDateTime> betweenMonth = IntStream.range(1, monthBetween)
                    .mapToObj(num -> beginDateTime.plusMonths(num))
                    .collect(Collectors.toList());
            rangeDateTime.addAll(betweenMonth);
        }

        List<String> rangeTableName = rangeDateTime.stream()
                // 逻辑表名拼月份
                .map(time -> shardingValue.getLogicTableName() + LocalDateTimeUtil.format(time, "yyyyMM"))
                .collect(Collectors.toList());


        return rangeTableName;

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
        return "HIS_DATA_SPI_BASED";
    }
}
