package com.yechrom.cloud.quartz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yechrom.cloud.dto.mapper.BuyHouseMapper;
import com.yechrom.cloud.dto.mapper.SellHouseMapper;
import com.yechrom.cloud.dto.pojo.BuyHouse;
import com.yechrom.cloud.dto.pojo.SellHouse;
import com.yechrom.cloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.util.List;
import java.util.LongSummaryStatistics;

/**
 * 定时任务
 */
@Slf4j
public class DashboardJob extends QuartzJobBean {
    @Resource
    BuyHouseMapper buyHouseMapper;

    @Resource
    SellHouseMapper sellHouseMapper;

    @Autowired
    RedisUtil redis;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("~定时任务开始运行~");
        List<BuyHouse>          buyHouses  = buyHouseMapper.selectList(new QueryWrapper<BuyHouse>().eq("buy_statue", 2));
        List<SellHouse> sellHouses = sellHouseMapper.selectList(new QueryWrapper<SellHouse>().eq("sell_statue", 2));
        //获取总成交额度
        LongSummaryStatistics buyHouseStatistics  = buyHouses.stream().mapToLong(BuyHouse::getBuyPrice).summaryStatistics();
        LongSummaryStatistics sellHouseStatistics = sellHouses.stream().mapToLong(SellHouse::getSellPrice).summaryStatistics();
        long                  buyCount            = buyHouseStatistics.getSum();
        long                  sellCount           = sellHouseStatistics.getSum();
        long                  countMoney          = sellCount + buyCount;
        //获取最火房源
        List<SellHouse> fireHouse =
                sellHouseMapper.selectList(new QueryWrapper<SellHouse>().eq("is_delete", 0).eq("sell_statue", 1).orderByDesc("sell_pick"));
        String address = "-";
        int    pick    = 0;
        if (fireHouse.size() != 0) {
            address = fireHouse.get(0).getSellAddress();
            pick = fireHouse.get(0).getSellPick();
        }
        JSONObject data = new JSONObject();
        data.put("firePick", pick);
        data.put("address", address);
        data.put("countMoney", countMoney);
        redis.set("house-control-system-data-dashboard", data.toJSONString());
        log.info("~定时任务结束~ 结束报文为{}", data.toJSONString());    }
}
