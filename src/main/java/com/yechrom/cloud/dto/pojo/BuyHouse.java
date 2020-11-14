package com.yechrom.cloud.dto.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class BuyHouse {
    private String buyOrder;
    private Integer buyStatue;
    private Date buyTime;
    private String buyUuid;
    private String sellUuid;
    private String buyAddress;
    private String buyIntroduction;
    private Long buyPrice;
    private Integer buyPick;
    private Integer isDelete;
    private String flag1;
    private String flag2;
    private String flag3;
    private String flag4;
    private String flag5;
}
