package com.yechrom.cloud.dto.pojo;

import lombok.Data;

@Data
public class User {
    private String uuid;
    private String username;
    private String password;
    private String name;
    private Integer roles;
    private String introduction;
    private String avater;
    private Integer isDelete;
    private String flag1;
    private String flag2;
    private String flag3;
    private String flag4;
    private String flag5;
}
