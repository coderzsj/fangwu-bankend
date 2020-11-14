package com.yechrom.cloud.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yechrom.cloud.dto.mapper.BuyHouseMapper;
import com.yechrom.cloud.dto.mapper.SellHouseMapper;
import com.yechrom.cloud.dto.pojo.BuyHouse;
import com.yechrom.cloud.dto.pojo.SellHouse;
import com.yechrom.cloud.dto.vo.response.ResponseBaseVo;
import com.yechrom.cloud.util.ResponseVoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DataService {


    @Resource
    private SellHouseMapper sellHouseMapper;

    @Resource
    private BuyHouseMapper buyHouseMapper;

    /**
     * 给求租订单+1
     *
     * @param order
     * @return
     */
    public ResponseBaseVo pickBuy(String order) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("is_delete", 0);
        columnMap.put("buy_order", order);
        List<BuyHouse> buyHouses = buyHouseMapper.selectByMap(columnMap);
        if (buyHouses.size() > 0) {
            int pick = buyHouses.get(0).getBuyPick();
            pick++;
            BuyHouse house = new BuyHouse();
            house.setBuyPick(pick);
            UpdateWrapper<BuyHouse> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("is_delete", 0);
            updateWrapper.set("buy_order", order);
            int result = buyHouseMapper.update(house, updateWrapper);
            ResponseBaseVo response = ResponseVoUtil.getResponse(result, "成功", "数据采集失败");
            return response;
        }
        return ResponseVoUtil.getResponse(0, "成功", "数据采集失败");
    }


    /**
     * 给在租订单+1
     *
     * @param order
     * @return
     */
    public ResponseBaseVo pickSell(String order) {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("is_delete",0);
        columnMap.put("sell_statue",1);
        columnMap.put("sell_order",order);
        List<SellHouse> sellHouses = sellHouseMapper.selectByMap(columnMap);

        if (sellHouses.size() > 0) {
            int pick = sellHouses.get(0).getSellPick();
            pick++;
            SellHouse sellHouse = new SellHouse();
            sellHouse.setSellPick(pick);
            QueryWrapper<SellHouse> queryWrapper = new QueryWrapper<>();
            SellHouse sellHouse1 = new SellHouse();
            sellHouse1.setIsDelete(0);
            sellHouse1.setSellOrder(order);
            queryWrapper.setEntity(sellHouse1);
            int result = sellHouseMapper.update(sellHouse, queryWrapper);
            ResponseBaseVo response = ResponseVoUtil.getResponse(result, "成功", "数据采集失败");
            return response;
        }
        return ResponseVoUtil.getResponse(0, "成功", "数据采集失败");
    }
}
