package com.yechrom.cloud.dto.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class SellHouse {
    private String sellOrder;
    private Integer sellStatue;
    private Date sellTime;
    private String sellUuid;
    private String sellAddress;
    private String sellImgs;
    private String sellIntroduction;
    private Long sellPrice;
    private String buyUser;
    private Integer sellPick;
    private Integer isDelete;
    private String flag1;
    private String flag2;
    private String flag3;
    private String flag4;
    private String flag5;
}
