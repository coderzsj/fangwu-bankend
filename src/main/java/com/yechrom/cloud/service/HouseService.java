package com.yechrom.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yechrom.cloud.dto.mapper.BuyHouseMapper;
import com.yechrom.cloud.dto.mapper.SellHouseMapper;
import com.yechrom.cloud.dto.mapper.UserMapper;
import com.yechrom.cloud.dto.pojo.*;
import com.yechrom.cloud.dto.vo.*;
import com.yechrom.cloud.dto.vo.response.ResponseBaseVo;
import com.yechrom.cloud.dto.vo.response.ResponseErrorVo;
import com.yechrom.cloud.dto.vo.response.ResponseVo;
import com.yechrom.cloud.util.UUIDUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Component
public class HouseService {

    @Resource
    SellHouseMapper sellHouseMapper;

    @Resource
    BuyHouseMapper buyHouseMapper;

    @Resource
    UserMapper userMapper;

    /**
     * 添加房源by房东
     */
    public int addHouseBySeller(AddHouseBySellerVo requestVo){

        SellHouse house = new SellHouse();
        house.setSellOrder(UUIDUtil.getOrderID(UUIDUtil.Order.SELLER));
        house.setSellStatue(1);
        house.setSellTime(new Date());
        house.setBuyUser(requestVo.getSell_uuid());
        house.setSellAddress(requestVo.getSell_address());
        house.setSellIntroduction(requestVo.getSell_intruduction());
        house.setSellPrice(requestVo.getSell_price());
        house.setSellUuid(requestVo.getSell_uuid());
        house.setIsDelete(0);
        house.setSellPick(0);
        return sellHouseMapper.insert(house);
    }


    /**
     * 添加房源by顾客
     */
    public int addHouseByUser(AddHouseBySellerVo requestVo){
        BuyHouse house = new BuyHouse();

        house.setBuyOrder(UUIDUtil.getOrderID(UUIDUtil.Order.USER));
        house.setBuyStatue(1);
        house.setBuyTime(new Date());
        house.setBuyUuid(requestVo.getSell_uuid());
        house.setBuyAddress(requestVo.getSell_address());
        house.setBuyIntroduction(requestVo.getSell_intruduction());
        house.setBuyPrice(requestVo.getSell_price());
        house.setSellUuid(requestVo.getSell_uuid());
        house.setIsDelete(0);
        house.setBuyPick(0);

        return buyHouseMapper.insert(house);
    }


    /**
     * 查询所有的求租订单
     * @param requestVo
     * @return
     */
    public ResponseBaseVo showAllBuyHouse(ShowAllSellHouseVo requestVo){
        //查数据库
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("is_delete", 0);
        columnMap.put("buy_statue", 1);
        List<BuyHouse> buyHouses = buyHouseMapper.selectByMap(columnMap);
        List<ShowAllHouseBackVo> allList = new ArrayList<ShowAllHouseBackVo>();

        //遍历获取vo
        for (BuyHouse buyHouse :
                buyHouses) {
            ShowAllHouseBackVo vo = new ShowAllHouseBackVo();

            vo.setBuy_order(buyHouse.getBuyOrder());
            vo.setBuy_statue(buyHouse.getBuyStatue());
            vo.setBuy_time(buyHouse.getBuyTime());
            vo.setBuy_uuid(buyHouse.getBuyUuid());
            vo.setBuy_address(buyHouse.getBuyAddress());
            vo.setBuy_introduction(buyHouse.getBuyIntroduction());
            vo.setBuy_price(buyHouse.getBuyPrice());
            vo.setBuy_pick(buyHouse.getBuyPick());

            Map<String, Object> columnMap1 = new HashMap<>();
            columnMap1.put("is_delete", 0);
            columnMap1.put("buy_house", buyHouse.getBuyUuid());

            List<User> users = userMapper.selectByMap(columnMap1);
            vo.setBuy_name(users.get(0).getName());

            allList.add(vo);
        }

        ResponseVo response = new ResponseVo();
        JSONObject data = new JSONObject();
        data.put("list" ,allList);
        data.put("total" ,allList.size());
        response.setData(data);
        response.setErrorcode(1);

        return response;
    }


    /**
     * 封装返回值
     * @param result
     * @return
     */
    public ResponseBaseVo getResponse( int result , String success , String fail){
        ResponseBaseVo response ;
        if(result == 0){
            response = new ResponseErrorVo();
            response.setErrorcode(0);
            ((ResponseErrorVo) response).setError(fail);
            return response;
        }

        response = new ResponseVo();
        response.setErrorcode(1);
        JSONObject data = new JSONObject();
        data.put("msg" , success);
        ((ResponseVo) response).setData(data);
        return response;
    }

    /**
     * 求租房源被房东卖了
     * @param
     * @return
     */
    public ResponseBaseVo BuyHouseHaveASeller(String sellorder , String sellid){
        UpdateWrapper<BuyHouse> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_delete",0);
        updateWrapper.set("ques_order",sellorder);
        BuyHouse house = new BuyHouse();
        house.setBuyStatue(2);
        house.setSellUuid(sellid);
        int result = buyHouseMapper.update(house ,updateWrapper);

        return getResponse(result , "操作成功" , "操作失败");
    }


    /**
     * 查询所有的在租订单
     * @param requestVo
     * @return
     */
    public ResponseBaseVo showAllSellHouse(ShowAllSellHouseVo requestVo){
        //查数据库
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("sell_statue",1);
        columnMap.put("is_delete",0);
        List<SellHouse> sellHouses = sellHouseMapper.selectByMap(columnMap);

        List<ShowAllHouseBackSellVo> allList = new ArrayList<ShowAllHouseBackSellVo>();

        //遍历获取vo
        for (SellHouse sellHouse :
                sellHouses) {
            ShowAllHouseBackSellVo vo = new ShowAllHouseBackSellVo();

            vo.setSell_order(sellHouse.getSellOrder());
            vo.setSell_statue(sellHouse.getSellStatue());
            vo.setSell_time(sellHouse.getSellTime());
            vo.setSell_uuid(sellHouse.getSellUuid());
            vo.setSell_address(sellHouse.getSellAddress());
            vo.setSell_introduction(sellHouse.getSellIntroduction());
            vo.setSell_price(sellHouse.getSellPrice());
            vo.setSell_pick(sellHouse.getSellPick());

            Map<String, Object> columnMap1 = new HashMap<>();
            columnMap.put("is_delete", 0);
            columnMap.put("sell_order", sellHouse.getSellUuid());
            List<User> users = userMapper.selectByMap(columnMap1);
            vo.setSell_name(users.get(0).getName());

            allList.add(vo);
        }

        ResponseVo response = new ResponseVo();
        JSONObject data = new JSONObject();
        data.put("list" ,allList);
        data.put("total" ,allList.size());
        response.setData(data);
        response.setErrorcode(1);

        return response;
    }


    /**
     * 在租房源被顾客买了
     * @param
     * @return
     */
    public ResponseBaseVo BuyHouseHaveAUser(String buyorder , String buyid){
        SellHouse house = new SellHouse();
        house.setSellStatue(2);
        house.setBuyUser(buyid);
        UpdateWrapper<SellHouse> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_delete",0);
        updateWrapper.set("sell_order",buyorder);
        int result = sellHouseMapper.update(house ,updateWrapper);
        return getResponse(result , "操作成功" , "操作失败");
    }

}
