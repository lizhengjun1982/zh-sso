package com.smart.sso.client;

import com.alibaba.fastjson.JSONObject;

public interface UserMng {
    boolean existsUser(int userId);

    void syncAddUserFromSSO(int userId, JSONObject data);
}
