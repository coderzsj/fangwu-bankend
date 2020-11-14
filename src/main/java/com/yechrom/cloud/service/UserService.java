package com.yechrom.cloud.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yechrom.cloud.dto.mapper.UserMapper;
import com.yechrom.cloud.dto.pojo.BuyHouse;
import com.yechrom.cloud.dto.pojo.SellHouse;
import com.yechrom.cloud.dto.pojo.User;
import com.yechrom.cloud.dto.vo.AddUserVo;
import com.yechrom.cloud.dto.vo.LoginVo;
import com.yechrom.cloud.dto.vo.ShowUserVo;
import com.yechrom.cloud.dto.vo.UpdateUserVo;
import com.yechrom.cloud.exception.exceptions.ParamIsNullException;
import com.yechrom.cloud.exception.exceptions.TokenNullException;
import com.yechrom.cloud.exception.exceptions.UsernameOrPasswordIsNullException;
import com.yechrom.cloud.util.RedisUtil;
import com.yechrom.cloud.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class UserService {


    @Resource
    private UserMapper user;

    @Autowired
    private RedisUtil redisUtil;

    public List<User> getUsers() {
        Map<String, Object> columnMap2 = new HashMap<>();
        columnMap2.put("uuid", "d84162ee1ce64d2fb361a4012a6d7d0c");
        return user.selectByMap(columnMap2);
//        return user.selectAll();
    }

    /**
     * 登录接口调用的业务类
     *
     * @param loginVo
     * @return
     * @throws Exception
     */
    public String login(LoginVo loginVo) throws Exception {

        if (StringUtils.isAllBlank(loginVo.getUsername()) || StringUtils.isAllBlank(loginVo.getPassword())) {
            throw new UsernameOrPasswordIsNullException("用户名密码为空");
        }
        Map<String, Object> columnMap1 = new HashMap<>();
        columnMap1.put("username", loginVo.getUsername());
        columnMap1.put("password", loginVo.getPassword());
        columnMap1.put("is_delete", 0);
        List<User> result = user.selectByMap(columnMap1);

        //token
        String token = "";

        //用户名或密码错误
        if (result.size() == 0) {
            return token;
        }
        //生成token 暂定 待优化
        token = UUIDUtil.getUserUUID();
        //信息存入redis
        JSONObject json    = new JSONObject();
        String     roles[] = new String[1];
        if (result.get(0).getRoles() == 1) {
            roles[0] = "user";
        } else if (result.get(0).getRoles() == 2) {
            roles[0] = "seller";
        } else if (result.get(0).getRoles() == 0) {
            roles[0] = "admin";
        } else if (result.get(0).getRoles() == -1) {
            roles[0] = "yechrom";
        }
        json.put("roles", roles);
        json.put("name", result.get(0).getName());
        json.put("introduction", result.get(0).getIntroduction());
        json.put("avatar", result.get(0).getAvater());
        json.put("uuid", result.get(0).getUuid());
        redisUtil.setEx(token, json.toJSONString(), 1, TimeUnit.DAYS);
        return token;
    }

    /**
     * 从redis中获取用户信息
     *
     * @param token
     * @return
     */
    public JSONObject getInfo(String token) throws Exception {
        String userInfo = "";
        userInfo = redisUtil.get(token);

        if (userInfo.length() == 0) {
            throw new UsernameOrPasswordIsNullException("reids中不存在info~");
        }
        return JSONObject.parseObject(userInfo);
    }

    /**
     * 登出用户
     *
     * @param token
     * @return
     * @throws Exception
     */
    public int logout(String token) throws Exception {
        if (StringUtils.isEmpty(token)) {
            throw new TokenNullException("token不存在~");
        }
        //判断是否存在token
        Boolean hasKey = redisUtil.hasKey(token);
        //如果存在 删除
        if (hasKey) {
            redisUtil.delete(token);
            return 1;
        }
        return 0;
    }

    /**
     * 查询所有的用户
     *
     * @return
     */
    public List<ShowUserVo> show() {

        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("is_delete", 0);
        List<User> users = user.selectByMap(columnMap);
        List<ShowUserVo> result = new ArrayList<ShowUserVo>();
        //遍历 获取你想要的
        for (User user :
                users) {
            ShowUserVo vo = new ShowUserVo();
            vo.setUuid(user.getUuid());
            vo.setName(user.getName());
            vo.setPassword(user.getPassword());
            vo.setUsername(user.getUsername());
            vo.setIntroduction(user.getIntroduction());
            vo.setAvatar(user.getAvater());

            int role = user.getRoles();
            if (role == 1) {
                String[] roleArray = {"user"};
                vo.setRoles(roleArray);
            } else if (role == 2) {
                String[] roleArray = {"seller"};
                vo.setRoles(roleArray);
            } else if (role == 0) {
                String[] roleArray = {"admin"};
                vo.setRoles(roleArray);
            } else {
                String[] roleArray = {"yechrom"};
                vo.setRoles(roleArray);
            }
            result.add(vo);
        }
        return result;
    }

    /**
     * 更新用户信息
     *
     * @return
     */
    public int updateUser(UpdateUserVo userVo) throws Exception {

        if (!StringUtils.isNotBlank(userVo.getPassword()) || !StringUtils.isNotBlank(userVo.getName())) {
            throw new ParamIsNullException("姓名或密码不能为空~");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();;
        updateWrapper.set("is_delete", 0);
        updateWrapper.set("uuid", userVo.getUuid());
        User userPo = new User();
        userPo.setName(userVo.getName());
        userPo.setIntroduction(userVo.getIntroduction());
        userPo.setPassword(userVo.getPassword());

        return user.update(userPo, updateWrapper);
    }

    /**
     * 删除用户
     *
     * @param uuid
     * @return
     * @throws Exception
     */
    public int deleteUser(String uuid) throws Exception {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("is_delete", 0);
        updateWrapper.set("uuid", uuid);
        User userPo = new User();
        userPo.setIsDelete(1);
        return user.update(userPo, updateWrapper);
    }

    /**
     * 添加用户
     *
     * @param userVo
     * @return
     * @throws Exception
     */
    public int addUser(AddUserVo userVo) throws Exception {
        User userPo = new User();
        userPo.setIsDelete(0);
        userPo.setName(userVo.getName());
        userPo.setPassword(userVo.getPassword());
        userPo.setUsername(userVo.getUsername());
        userPo.setRoles(userVo.getRoles());
        userPo.setUuid(UUIDUtil.getUserUUID());
        userPo.setAvater("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return user.insert(userPo);
    }
}
