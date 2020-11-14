package com.yechrom.cloud.dto.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class QuestionOrder {
    private String quesOrder;
    private Integer quesStatue;
    private Date quesTime;
    private String quesAddress;
    private String quesIntruduction;
    private String quesSeller;
    private String quesUser;
    private Integer isDelete;
    private String flag1;
    private String flag2;
    private String flag3;
    private String flag4;
    private String flag5;
}
