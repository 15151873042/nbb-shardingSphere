package com.nbb.tableHorizontal.framework;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Properties;

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
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<LocalDateTime> rangeShardingValue) {
        return null;
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
