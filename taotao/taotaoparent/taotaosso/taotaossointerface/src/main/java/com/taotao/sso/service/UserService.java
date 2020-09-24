package com.taotao.sso.service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import org.apache.commons.lang3.StringUtils;

public interface UserService {
    TaotaoResult checkData(String data, int type);
    TaotaoResult register(TbUser user);
    TaotaoResult login(String username, String password);
    TaotaoResult getUserByToken(String token);
//    TaotaoResult loginout(String token);
}
