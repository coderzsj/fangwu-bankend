package com.yechrom.cloud.quartz;

import com.alibaba.fastjson.JSONObject;
import com.yechrom.cloud.dto.mapper.BuyHouseMapper;
import com.yechrom.cloud.dto.mapper.SellHouseMapper;
import com.yechrom.cloud.dto.pojo.BuyHouse;
import com.yechrom.cloud.dto.pojo.BuyHouseExample;
import com.yechrom.cloud.dto.pojo.SellHouse;
import com.yechrom.cloud.dto.pojo.SellHouseExample;
import com.yechrom.cloud.dto.vo.response.ResponseVo;
import com.yechrom.cloud.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GetData {

    @Resource
    BuyHouseMapper buyHouseMapper;

    @Resource
    SellHouseMapper sellHouseMapper;

    @Autowired
    RedisUtil redis;

    public void run(){
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("buy_statue",2);
        List<BuyHouse>     buyHouses = buyHouseMapper.selectByMap(columnMap);

        Map<String,Object> columnMap1 = new HashMap<>();
        columnMap1.put("buy_statue",2);
        List<SellHouse> sellHouses = sellHouseMapper.selectByMap(columnMap1);

        //获取总成交额度
        long buyCount = 0;
        if(buyHouses.size() != 0){
            for (BuyHouse buyHouse :
                    buyHouses) {
                buyCount += buyHouse.getBuyPrice();
            }
        }
        long sellCount = 0;
        if(buyHouses.size() != 0){
            for (SellHouse sellHouse :
                    sellHouses) {
                buyCount += sellHouse.getSellPrice();
            }
        }
        long countMoney = sellCount + buyCount;

        //获取最火房源
        Map<String,Object> columnMap2 = new HashMap<>();
        columnMap1.put("is_delete",0);
        columnMap1.put("sell_statue",1);
        List<SellHouse> fireHouse = sellHouseMapper.selectByMap(columnMap2);

        String address = "-";
        int pick = 0;

        if (fireHouse.size() != 0) {
            address = fireHouse.get(0).getSellAddress();
            pick = fireHouse.get(0).getSellPick();
        }

        JSONObject data = new JSONObject();
        data.put("firePick" , pick);
        data.put("address" , address);
        data.put("countMoney", countMoney);

        redis.set("house-control-system-data-dashboard" , data.toJSONString());

        log.info("~定时任务结束~ 结束报文为{}" ,data.toJSONString());
    }
}
